package com.sporyap.sporyap.view.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.SettingsFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.MaterialDialogHelper
import com.sporyap.sporyap.utils.Prefs
import com.sporyap.sporyap.viewmodel.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private var _binding : SettingsFragmentBinding ? = null
    private val binding get() = _binding
    private lateinit var uiStateJob : Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.show()
        initCollect()
        initListeners()
    }

    private fun initCollect() {
        uiStateJob = lifecycleScope.launch {
            viewModel.settingsUIState.collect {
                when (it) {
                    is SettingsViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is SettingsViewModel.ViewState.ShowErrorMessage->{
                        MaterialDialogHelper().showDialog(requireContext() , it.message?:Constants.ERROR_MESSAGE, false)
                    }
                    is SettingsViewModel.ViewState.OnLogOut->{
                        Prefs.removeSharedPreferences(requireContext())
                        exitProcess(0)
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun initListeners(){
        binding?.textViewAccount?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_settingsFragment_to_accountSettingsFragment)
        }
        binding?.buttonLogOut?.setOnClickListener {
            showCustomQuestionDialog(requireContext() , getString(R.string.log_out))
        }
    }

    private fun showCustomQuestionDialog(context: Context, question : String){
        val view = View.inflate(context, R.layout.question_pop_up , null)
        val textViewQuestion = view.findViewById<TextView>(R.id.text_view_question)
        val buttonNo = view.findViewById<AppCompatButton>(R.id.button_no)
        val buttonYes = view.findViewById<AppCompatButton>(R.id.button_yes)

        textViewQuestion.text = question

        val dialog = MaterialAlertDialogBuilder(context, R.style.MaterialDialogStyle).setView(view)
            .setCancelable(false)
            .show()

        buttonNo.setOnClickListener {
            if (dialog.isShowing){
                dialog.dismiss()
            }
        }
        buttonYes.setOnClickListener {
            val userId = Prefs.getKeySharedPreferencesInt(context , Constants.PREF_USER_ID)
            viewModel.logOut(userId)
            dialog.dismiss()
        }
    }

    override fun onDestroy() {
        if(this::uiStateJob.isInitialized){
            uiStateJob.cancel()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}