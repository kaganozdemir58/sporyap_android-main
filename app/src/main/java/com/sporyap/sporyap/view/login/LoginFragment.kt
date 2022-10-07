package com.sporyap.sporyap.view.login

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.LoginFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.utils.KeyBoardEvents
import com.sporyap.sporyap.utils.Prefs
import com.sporyap.sporyap.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding : LoginFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var mainActivity: MainActivity
    private lateinit var uiStateScope : Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        initCollect()
        initListeners()
    }

    private fun initListeners(){

        binding?.textViewForgotPassword?.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Constants.ARG_FOR_WHAT, 1)
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_userVerificationFragment, bundle)
        }

        binding?.buttonLogin?.setOnClickListener {

            if (binding?.editTextUserInformation?.text.toString().isEmpty() || binding?.editTextPassword?.text.toString().isEmpty()){
                Toast.makeText(requireContext() , getString(R.string.please_enter_user_information) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (viewModel.isValidMail(binding?.editTextUserInformation?.text.toString())){
                val email = binding?.editTextUserInformation?.text.toString()
                viewModel.login(email , "", binding?.editTextPassword?.text.toString())
                return@setOnClickListener
            }else{
                Toast.makeText(requireContext() , getString(R.string.please_enter_user_information) , Toast.LENGTH_SHORT).show()
            }

            KeyBoardEvents.hideKeyBoard(requireActivity())

            if (viewModel.isValidMobile(binding?.editTextUserInformation?.text.toString())){
                val phone = binding?.editTextUserInformation?.text.toString()
                viewModel.login("", phone, binding?.editTextPassword?.text.toString())
                return@setOnClickListener
            }else{
                Toast.makeText(requireContext() , getString(R.string.please_enter_user_information), Toast.LENGTH_SHORT).show()
            }
        }

        binding?.imageViewIcEyeOpenPassword?.setOnClickListener {
            binding?.editTextPassword?.inputType = 129
            binding?.imageViewIcEyeOpenPassword?.visibility = View.GONE
            binding?.imageViewIcEyeClosePassword?.visibility = View.VISIBLE
        }

        binding?.imageViewIcEyeClosePassword?.setOnClickListener {
            binding?.editTextPassword?.inputType = InputType.TYPE_CLASS_TEXT
            binding?.imageViewIcEyeClosePassword?.visibility = View.GONE
            binding?.imageViewIcEyeOpenPassword?.visibility = View.VISIBLE
        }
    }

    private fun initCollect(){
        uiStateScope = lifecycleScope.launch {
            viewModel.loginUIState.collect {
                when(it){
                    is LoginViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is LoginViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(context = requireContext() , message = it.message)
                    }
                    is LoginViewModel.ViewState.OnLogged->{
                        Prefs.setKeySharedPreferences(requireContext() , Constants.PREF_TOKEN , Constants.BEARER_KEY + it.response.result.jwtoken)
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_eventsFragment)
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        if (this::uiStateScope.isInitialized) {
            uiStateScope.cancel()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}