package com.nastirlex.superfit.presentation.my_body

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentMyBodyBinding
import com.nastirlex.superfit.net.dto.PhotoDto
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class MyBodyFragment : Fragment() {
    lateinit var binding: FragmentMyBodyBinding
    private val myBodyViewModel: MyBodyViewModel by viewModels()

    private lateinit var galleryActivityLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyBodyBinding.inflate(inflater, container, false)

        myBodyStateObserve()
        setupOnWeightEditButtonClick()
        setupOnMyBodyLoadPhotoButtonClick()
        setupOnSeeAllButtonClick()

        setupGalleryLauncher()
        setupCameraLauncher()

        return binding.root
    }

    private fun myBodyStateObserve() {
        myBodyViewModel.myBodyStateLiveMutable.observe(viewLifecycleOwner) {
            when (it.parametersStatus) {
                is ParametersStatus.SuccessfulLoadingParameters -> {
                    setupWeight(resources.getString(R.string.weight, it.parametersStatus.weight))
                    setupHeight(resources.getString(R.string.height, it.parametersStatus.height))
                    if (it.leftPhoto != null)
                        setupLeftPhoto(it.leftPhoto)

                    if (it.rightPhoto != null)
                        setupRightPhoto(it.rightPhoto)
                }

                is ParametersStatus.EmptyParameters -> {
                    setupWeight(resources.getString(R.string.undefined))
                    setupHeight(resources.getString(R.string.undefined))
                    if (it.leftPhoto != null)
                        setupLeftPhoto(it.leftPhoto)

                    if (it.rightPhoto != null)
                        setupRightPhoto(it.rightPhoto)
                }

                is ParametersStatus.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is ParametersStatus.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is ParametersStatus.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }

        }
    }

    private fun setupWeight(weight: String) {
        binding.myBodyWeightTextView.text = weight
    }

    private fun setupHeight(height: String) {
        binding.myBodyHeightTextView.text = height
    }

    private fun setupLeftPhoto(leftImage: PhotoDto?) {
        if (leftImage != null) {
            binding.leftImageView.setImageBitmap(leftImage.bitmap)
            binding.leftPhotoDateTextView.text = leftImage.date
        }

    }

    private fun setupRightPhoto(rightImage: PhotoDto?) {
        if (rightImage != null) {
            binding.rightImageView.setImageBitmap(rightImage.bitmap)
            binding.rightPhotoDateTextView.text = rightImage.date
        }
    }

    private fun setupGalleryLauncher() {
        galleryActivityLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { result ->
            if (result != null) {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, result)

                myBodyViewModel.send(LoadGalleryPhotoOnServer(result))
                Log.d("uri", result.toString())
            }
        }
    }

    private fun setupCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                it?.let {
                    val bytes = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(
                        context?.contentResolver, it, "Title", null
                    )

                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            Uri.parse(path)
                        )

                    myBodyViewModel.send(LoadPhotoOnServer(bitmap))
                }
            }
    }

    private fun setupOnWeightEditButtonClick() {
        binding.weightEditTextView.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.edit_parameter_dialog, null)
            val firstTextInputLayout = view.findViewById<TextInputLayout>(R.id.filledTextField)
            val firstEditText = view.findViewById<EditText>(R.id.parameterEditText)

            val firstDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(
                    resources.getString(
                        R.string.edit_parameter_dialog_title,
                        resources.getString(R.string.weight_simple)
                    )
                )
                .setPositiveButton(getString(R.string.change), null)
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .create()

            firstDialog.setOnShowListener {
                firstDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener {
                        val weight = firstEditText.text.toString()
                        if (weight.isNotBlank() || weight.isNotEmpty()) {
                            firstDialog.dismiss()

                            val secondView =
                                layoutInflater.inflate(R.layout.edit_parameter_dialog, null)
                            val secondTextInputLayout =
                                secondView.findViewById<TextInputLayout>(R.id.filledTextField)
                            val secondEditText =
                                secondView.findViewById<EditText>(R.id.parameterEditText)

                            secondTextInputLayout.hint = resources.getString(R.string.height_label)

                            val secondDialog = AlertDialog.Builder(requireContext())
                                .setView(secondView)
                                .setTitle(
                                    resources.getString(
                                        R.string.edit_parameter_dialog_title,
                                        resources.getString(R.string.height_simple)
                                    )
                                )
                                .setPositiveButton(getString(R.string.change), null)
                                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                                .create()

                            secondDialog.setOnShowListener {
                                secondDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                    .setOnClickListener {
                                        val height = secondEditText.text.toString()
                                        if (height.isNotBlank()) {
                                            secondDialog.dismiss()
                                            myBodyViewModel.send(
                                                UpdateParameters(
                                                    weight.toInt(),
                                                    height.toInt()
                                                )
                                            )
                                        } else {
                                            secondTextInputLayout.error =
                                                resources.getString(R.string.error_dialog)
                                        }
                                    }
                            }

                            secondDialog.show()
                        } else {
                            firstTextInputLayout.error = resources.getString(R.string.error_dialog)
                        }


                    }
            }
            firstDialog.show()
        }
    }

    private fun setupOnMyBodyLoadPhotoButtonClick() {
        binding.myBodyLoadPhotoButton.setOnClickListener {
            val photoLoadView = layoutInflater.inflate(R.layout.load_photo_dialog, null)

            val galleryButton = photoLoadView.findViewById<Button>(R.id.loadPhotoGalleryButton)
            val cameraButton = photoLoadView.findViewById<Button>(R.id.loadPhotoCameraButton)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(photoLoadView)
                .create()

            galleryButton.setOnClickListener {
                galleryActivityLauncher.launch("image/*")
                dialog.dismiss()
            }

            cameraButton.setOnClickListener {
                cameraLauncher.launch()
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setupOnSeeAllButtonClick() {
        binding.myBodySeeAllTextView.setOnClickListener {
            val action = MyBodyFragmentDirections.actionMyBodyFragmentToImageListFragment()
            findNavController().navigate(action)
        }
    }

}