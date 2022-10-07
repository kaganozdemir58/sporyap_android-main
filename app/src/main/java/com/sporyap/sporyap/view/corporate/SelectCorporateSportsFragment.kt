package com.sporyap.sporyap.view.corporate

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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.response.sport.Result
import com.sporyap.sporyap.databinding.SelectCorporateSportsFragmentBinding
import com.sporyap.sporyap.adapter.sport.SportsAdapter
import com.sporyap.sporyap.utils.MaterialDialogHelper
import com.sporyap.sporyap.adapter.listeners.OnSportItemClickListener
import com.sporyap.sporyap.viewmodel.corporate.SelectCorporateSportsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectCorporateSportsFragment : Fragment(), OnSportItemClickListener {

    private val viewModel: SelectCorporateSportsViewModel by viewModels()
    private var _binding : SelectCorporateSportsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var sportsAdapter: SportsAdapter
    private lateinit var uiStateScope : Job
    private lateinit var corporateName : String
    private var corporateType : Int = 0
    private lateinit var mainActivity : MainActivity
    private var selectedSportsIds = arrayListOf<Int>()
    private var sports : List<Result> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectCorporateSportsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        setArgumentValues()
        initCollect()
        initListeners()
        viewModel.getSports(requireContext())
    }

    private fun setArgumentValues(){
        corporateName = requireArguments().getString("corporateName")!!
        corporateType = requireArguments().getInt("corporateType")
    }

    private fun initListeners() {
        binding.buttonComplete.setOnClickListener {
            if (selectedSportsIds.isEmpty()){
                Toast.makeText(requireContext() , requireContext().getString(R.string.please_choose_at_least_one_sport) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putInt("userType", 3)
            bundle.putIntArray("corporateSports", selectedSportsIds.toIntArray())
            bundle.putString("corporateName", corporateName)
            bundle.putInt("corporateType", corporateType)
            Navigation.findNavController(requireView()).navigate(R.id.action_selectCorporateSportsFragment_to_userVerificationFragment, bundle)
        }
    }

    private fun initCollect(){
        uiStateScope = lifecycleScope.launch {
            viewModel.corporateSportUIState.collect {
                when(it){
                    is SelectCorporateSportsViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is SelectCorporateSportsViewModel.ViewState.ShowErrorMessage->{
                        MaterialDialogHelper().showDialog(requireContext(), it.message , false)
                    }
                    is SelectCorporateSportsViewModel.ViewState.OnLoaded->{
                        if (it.response.result.isEmpty()){
                            return@collect
                        }
                        sports = it.response.result
                        fillSports(sports)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun fillSports(sports: List<Result>) {
        binding.recyclerViewSports.hasFixedSize()
        binding.recyclerViewSports.layoutManager = GridLayoutManager(requireContext() ,3 , GridLayoutManager.VERTICAL , false)
        binding.recyclerViewSports.itemAnimator = DefaultItemAnimator()
        sportsAdapter = SportsAdapter(sports , requireContext(),this)
        binding.recyclerViewSports.adapter = sportsAdapter
        sportsAdapter.submitList(sports)
    }

    override fun onClick(sport: Result) {
        val selectedSport = sports.find { x-> x.id == sport.id }
        selectedSport?.isSelected = !selectedSport?.isSelected!!

        if (selectedSport.isSelected){
            selectedSportsIds.add(sport.id)
        }
        sportsAdapter.updateSports(sports)
    }

    override fun onDestroy() {
        if (this::uiStateScope.isInitialized){
            uiStateScope.cancel()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}