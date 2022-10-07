package com.sporyap.sporyap.view.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.UserVerificationFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.utils.Prefs
import com.sporyap.sporyap.viewmodel.signup.UserVerificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserVerificationFragment : Fragment() {

    private val viewModel: UserVerificationViewModel by viewModels()
    private var _binding : UserVerificationFragmentBinding? = null
    private val binding get() = _binding!!
    private var userId : Int = 0
    private var athleteSports = emptyArray<Int>()
    private var trainerSports = emptyArray<Int>()
    private var corporateSports = emptyArray<Int>()
    private var corporateType = 0
    private var forWhat = 0
    private var corporateName : String? = null
    private lateinit var uiStateJob : Job
    private lateinit var mainActivity: MainActivity
    private var notificationTypeId : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserVerificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        getArgs()
        initListeners()
        initCollect()
    }

    private fun getArgs() {

        if(arguments?.getIntArray(Constants.ATHLETE_SPORTS) !=null){
            athleteSports = arguments?.getIntArray(Constants.ATHLETE_SPORTS)?.toTypedArray()!!
        }
        if(arguments?.getIntArray(Constants.TRAINER_SPORTS) !=null){
            trainerSports = arguments?.getIntArray(Constants.TRAINER_SPORTS)?.toTypedArray()!!
        }
        if(arguments?.getIntArray(Constants.CORPORATE_SPORTS) !=null){
            corporateSports = arguments?.getIntArray(Constants.CORPORATE_SPORTS)?.toTypedArray()!!
        }

        if(arguments?.getInt(Constants.BUNDLE_CORPORATE_TYPE) != 0){
            corporateType = arguments?.getInt(Constants.BUNDLE_CORPORATE_TYPE)!!
        }

        if(arguments?.getString(Constants.BUNDLE_CORPORATE_NAME) !=null){
            corporateName = arguments?.getString(Constants.BUNDLE_CORPORATE_NAME)
        }

        if (arguments?.getInt(Constants.ARG_FOR_WHAT) !=0){
            forWhat = arguments?.getInt(Constants.ARG_FOR_WHAT)!!
        }
    }

    private fun setPassiveBg(button : AppCompatButton){
        button.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
        button.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))
    }

    private fun setActiveBg(button : AppCompatButton){
        button.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_active_button)
        button.setTextColor(ContextCompat.getColor(requireContext() , R.color.white))
    }

    private fun initListeners(){

        binding.buttonSmsVerification.setOnClickListener {
            setActiveBg(binding.buttonSmsVerification)
            setPassiveBg(binding.buttonEmailVerification)
            notificationTypeId = 2
        }

        binding.buttonEmailVerification.setOnClickListener {
            setActiveBg(binding.buttonEmailVerification)
            setPassiveBg(binding.buttonSmsVerification)
            notificationTypeId = 1
        }

        binding.buttonEnter.setOnClickListener {
            if(notificationTypeId == 0){
                Toast.makeText(requireContext() , getString(R.string.please_select_verification_type) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Prefs.setKeySharedPreferencesInt(context = requireContext() , key = Constants.NOTIFICATION_TYPE_ID , notificationTypeId)
            if (forWhat == 1){
                startPasswordChange(1)
                return@setOnClickListener
            }
            sendRegister(notificationTypeId)
        }
    }

    private fun startPasswordChange(type : Int){
        val bundle = Bundle()
        bundle.putInt(Constants.ARG_TYPE, type)
        Navigation.findNavController(requireView()).navigate(R.id.action_userVerificationFragment_to_startPasswordChangeFragment , bundle)
    }

    private fun sendRegister(notificationTypeId : Int){
        viewModel.register(
            context = requireContext(),
            selectedSports = athleteSports.toMutableList(),
            trainerSports = trainerSports.toMutableList(),
            corporateSports = corporateSports.toMutableList(),
            userType = arguments?.getInt(Constants.ARG_USER_TYPE)!!,
            notificationType = notificationTypeId,
            corporateName = corporateName,
            corporateType = corporateType
        )
    }

    private fun goCompleteVerification(){
        val bundle = Bundle()
        bundle.putInt(Constants.ARG_NOTIFICATION_TYPE_ID, notificationTypeId)
        bundle.putInt(Constants.ARG_USER_ID, userId)
        Navigation.findNavController(requireView()).navigate(R.id.action_userVerificationFragment_to_completeVerificationFragment,bundle)
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
          viewModel.userVerificationUIState.collect {
              when(it){
                  is UserVerificationViewModel.ViewState.ShowLoading->{
                      mainActivity.progressBar.isVisible = it.isShow
                  }
                  is UserVerificationViewModel.ViewState.ShowErrorMessage->{
                      DialogHelper.showErrorDialog(requireContext() , it.message)
                  }
                  is UserVerificationViewModel.ViewState.OnRegistered->{
                      userId = it.response.result.userId
                      goCompleteVerification()
                  }else -> Unit
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