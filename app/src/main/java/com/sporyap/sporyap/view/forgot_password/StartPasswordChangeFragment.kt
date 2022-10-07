package com.sporyap.sporyap.view.forgot_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.StartPasswordChangeFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.viewmodel.forgot_password.StartPasswordChangeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartPasswordChangeFragment : Fragment() {

    private val viewModel: StartPasswordChangeViewModel by viewModels()
    private var _binding : StartPasswordChangeFragmentBinding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartPasswordChangeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        designPage()
        initListeners()
    }

    private fun initListeners(){
        binding.buttonSend.setOnClickListener {

        }
    }

    private fun designPage(){
        if (arguments?.getInt(Constants.ARG_TYPE) == 2){
            binding.textViewPageTitle.text = getString(R.string.your_phone)
            binding.editTextPhone.visibility = View.VISIBLE
            binding.editTextEmail.visibility = View.GONE
        }else{
            binding.textViewPageTitle.text = getString(R.string.your_email)
            binding.editTextEmail.visibility = View.VISIBLE
            binding.editTextPhone.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}