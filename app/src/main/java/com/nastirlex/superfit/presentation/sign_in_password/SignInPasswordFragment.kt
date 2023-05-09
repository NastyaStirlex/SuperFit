package com.nastirlex.superfit.presentation.sign_in_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSignInPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInPasswordFragment : Fragment() {
    private lateinit var binding: FragmentSignInPasswordBinding
    private val args: SignInPasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInPasswordBinding.inflate(inflater, container, false)

        setupUsername()

        setupOnBackButtonClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupOnBackButtonClick()
    }

    private fun setupUsername() {
        binding.usernameTextView.text = args.username
    }

    private fun setupOnBackButtonClick() {
        binding.backImageButton.setOnClickListener {
            //Navigation.findNavController(requireActivity(), R.id.activity_main_nav_host).navigateUp()

            findNavController().navigate(R.id.signInFragment)
        }
    }

}