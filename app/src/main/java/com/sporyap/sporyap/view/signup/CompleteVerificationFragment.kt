package com.sporyap.sporyap.view.signup

import android.os.Bundle
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
import com.sporyap.sporyap.databinding.CompleteVerificationFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.utils.Prefs
import com.sporyap.sporyap.viewmodel.signup.CompleteVerificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompleteVerificationFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private val viewModel: CompleteVerificationViewModel by viewModels()
    private lateinit var uiStateJob : Job
    private var _binding : CompleteVerificationFragmentBinding? = null
    private val binding get() = _binding!!
    private var userId : Int = 0
    private var notificationTypeId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompleteVerificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        setArgumentValues()
        initCollect()
        initListeners()
    }

    private fun setArgumentValues() {
        userId = requireArguments().getInt(Constants.ARG_USER_ID)
        notificationTypeId = requireArguments().getInt(Constants.ARG_NOTIFICATION_TYPE_ID)
    }

    private fun initListeners() {
        binding.buttonSendCode.setOnClickListener {
            if (binding.editTextVerificationCode.text.toString().isEmpty()){
                Toast.makeText(requireContext() , getString(R.string.please_enter_verification_code), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.confirmVerificationCode(requireContext(), userId, notificationTypeId, binding.editTextVerificationCode.text.toString())
        }
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
            viewModel.completeVerificationUIState.collect {
                when(it){
                    is CompleteVerificationViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is CompleteVerificationViewModel.ViewState.OnCodeConfirmed->{
                        Prefs.setKeySharedPreferencesBoolean(requireContext() , Constants.PREF_IS_LOGGED , true)
                        Prefs.setKeySharedPreferences(context = requireContext() , key = Constants.PREF_USER_ID , userId.toString())
                        Navigation.findNavController(requireView()).navigate(R.id.action_completeVerificationFragment_to_eventsFragment)
                    }
                    is CompleteVerificationViewModel.ViewState.OnCodeReceived->{
                        DialogHelper.showErrorDialog(requireContext() , it.response.message)
                    }
                    is CompleteVerificationViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(requireContext() , it.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroy() {
        if (this::uiStateJob.isInitialized){
            uiStateJob.cancel()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}