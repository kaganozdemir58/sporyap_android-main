package com.sporyap.sporyap.view.corporate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.response.corporate.Result
import com.sporyap.sporyap.databinding.CorporateTypeFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.viewmodel.corporate.CorporateTypeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CorporateTypeFragment : Fragment() {

    private val viewModel: CorporateTypeViewModel by viewModels()
    private lateinit var mainActivity : MainActivity
    private lateinit var uiStateJob : Job
    private var selectedCorporateType = 0
    private lateinit var corporateName : String
    private var _binding : CorporateTypeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CorporateTypeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        corporateName = requireArguments().getString(Constants.BUNDLE_CORPORATE_NAME)!!
        initListeners()
        initCollect()
        viewModel.getCorporateTypes(requireContext())
    }

    private fun initListeners(){
        binding.radioGroupCorporateType.setOnCheckedChangeListener { _, checkedId ->
            selectedCorporateType = checkedId
        }
        binding.buttonCorporateTypeNext.setOnClickListener {
            if (selectedCorporateType == 0 ){
                Toast.makeText(requireContext() , getString(R.string.please_select_corporate_type), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putString(Constants.BUNDLE_CORPORATE_NAME, corporateName)
            bundle.putInt(Constants.BUNDLE_CORPORATE_TYPE, selectedCorporateType)
            Navigation.findNavController(requireView()).navigate(R.id.action_corporateTypeFragment_to_selectCorporateSportsFragment, bundle)
        }
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
            viewModel.corporateTypeUIState.collect {
                when(it){
                    is CorporateTypeViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is CorporateTypeViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(context = requireContext() , message = it.message)
                    }
                    is CorporateTypeViewModel.ViewState.OnLoadedCorporateTypes->{
                        createCorporateTypesRadioButtons(it.response.result)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun createCorporateTypesRadioButtons(result: List<Result>) {
        mainActivity.progressBar.visibility = View.VISIBLE
        if (result.isEmpty()){
            mainActivity.progressBar.visibility = View.GONE
            return
        }
        for (item in result){
            val radiobutton = RadioButton(requireContext(), null, R.attr.radioButtonStyle)
            val layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT , 120)
            layoutParams.setMargins(16,8,16,8)
            radiobutton.layoutParams = layoutParams
            radiobutton.text = item.corporateTypeName
            radiobutton.id = item.id
            binding.radioGroupCorporateType.addView(radiobutton)
        }
        mainActivity.progressBar.visibility = View.GONE
        binding.textViewPageTitle.visibility = View.VISIBLE
        binding.buttonCorporateTypeNext.visibility = View.VISIBLE
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