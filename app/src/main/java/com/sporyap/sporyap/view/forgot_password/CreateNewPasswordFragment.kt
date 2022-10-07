package com.sporyap.sporyap.view.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.CreateNewPasswordFragmentBinding
import com.sporyap.sporyap.viewmodel.forgot_password.CreateNewPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewPasswordFragment : Fragment() {

    private lateinit var viewModel: CreateNewPasswordViewModel
    private var _binding : CreateNewPasswordFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateNewPasswordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initListeners(){
        binding.buttonSave.setOnClickListener {
            if (!viewModel.isValidPasswords(password = binding.editTextPassword.text.toString() , passwordRetry = binding.editTextPasswordRetry.text.toString())){
                Toast.makeText(requireContext() , getString(R.string.create_new_password) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!viewModel.isEqualsPasswords(password = binding.editTextPassword.text.toString() , passwordRetry = binding.editTextPasswordRetry.text.toString())){
                Toast.makeText(requireContext() , getString(R.string.your_password_not_equals) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

        }
    }

}