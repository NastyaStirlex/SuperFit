package com.nastirlex.superfit.presentation.imageList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentImageListBinding
import com.nastirlex.superfit.net.dto.PhotoDto
import com.nastirlex.superfit.presentation.imageList.adapter.ImagesListAdapter
import com.nastirlex.superfit.presentation.main.adapter.LastExercisesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageListFragment : Fragment() {
    lateinit var binding: FragmentImageListBinding
    private val imagesListViewModel: ImagesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageListBinding.inflate(inflater, container, false)

        setupImagesListObserver()
        setupOnBackButtonClick()

        return binding.root
    }

    private fun setupImagesListObserver() {
        imagesListViewModel.imagesListStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is ImagesListState.FullImagesList -> {
                    setupRecyclerView(it.imagesList)
                }

                is ImagesListState.EmptyImagesList -> {}

            }
        }

    }

    private fun setupRecyclerView(imagesList: MutableMap<String, MutableList<PhotoDto>>) {
        binding.recyclerView.layoutManager =
            GridLayoutManager(this.context, 3)


        val list: MutableList<Any> =
            emptyList<Any>().toMutableList()

        imagesList.forEach {
            list.add(it.key)
            it.value.forEach {
                list.add(it)
            }
        }

        binding.recyclerView.adapter = ImagesListAdapter(imagesList = list)
        val adapter = binding.recyclerView.adapter

        (binding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter!!.getItemViewType(position)) {
                        2 -> 3
                        else -> 1
                    }
                }
            }
    }

    private fun setupOnBackButtonClick() {
        binding.backImageButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}