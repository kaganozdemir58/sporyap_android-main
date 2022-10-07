package com.sporyap.sporyap.view.corporate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.CorporateNameFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.viewmodel.corporate.CorporateNameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CorporateNameFragment : Fragment(){

    private val viewModel: CorporateNameViewModel by viewModels()
    private var _binding : CorporateNameFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CorporateNameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners(){
        binding.buttonCorporateNameNext.setOnClickListener {
            if (!viewModel.isValidCorporateName(binding.editTextCorporateName.text.toString())){
                Toast.makeText(requireContext() , getString(R.string.please_enter_corporate_name), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putString(Constants.BUNDLE_CORPORATE_NAME, binding.editTextCorporateName.text.toString())
            Navigation.findNavController(requireView()).navigate(R.id.action_corporateNameFragment_to_corporateTypeFragment, bundle)
        }
    }
}