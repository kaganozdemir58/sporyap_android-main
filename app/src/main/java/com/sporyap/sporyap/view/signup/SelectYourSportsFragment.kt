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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.adapter.sport.SportsAdapter
import com.sporyap.sporyap.adapter.listeners.OnSportItemClickListener
import com.sporyap.sporyap.viewmodel.signup.SelectYourSportsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.sporyap.sporyap.data.network.response.sport.Result
import com.sporyap.sporyap.databinding.SelectYourSportsFragmentBinding
import com.sporyap.sporyap.utils.Constants

@AndroidEntryPoint
class SelectYourSportsFragment : Fragment(), OnSportItemClickListener {

    private val viewModel: SelectYourSportsViewModel by viewModels()
    private var _binding : SelectYourSportsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var sportsAdapter: SportsAdapter
    private lateinit var uiStateScope : Job
    private lateinit var mainActivity: MainActivity
    private var selectedSportsIds = arrayListOf<Int>()
    private var sports : List<Result> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectYourSportsFragmentBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        initListeners()
        initCollect()
        viewModel.getSports()
    }

    private fun initListeners() {
        binding.buttonComplete.setOnClickListener {
            if (selectedSportsIds.isEmpty()){
                Toast.makeText(requireContext() , requireContext().getString(R.string.please_choose_at_least_one_sport) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val userType = arguments?.getInt(Constants.ARG_USER_TYPE)
            if (userType == 2) {
                val bundle = Bundle()
                bundle.putIntArray(Constants.SPORT_ID_LIST, selectedSportsIds.toIntArray())
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_selectYourSportsFragment_to_selectTrainerSportsFragment,
                    bundle
                )
            } else {
                val bundle = Bundle()
                bundle.putIntArray(Constants.ATHLETE_SPORTS, selectedSportsIds.toIntArray())
                bundle.putInt(Constants.USER_TYPE, userType!!)
                Navigation.findNavController(requireView()).navigate(R.id.action_selectYourSportsFragment_to_userVerificationFragment, bundle)
            }
        }
    }

    private fun initCollect(){
        uiStateScope = lifecycleScope.launch {
            viewModel.userSportUIState.collect {
                when(it){
                    is SelectYourSportsViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is SelectYourSportsViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(requireContext() , it.message)
                    }
                    is SelectYourSportsViewModel.ViewState.OnLoaded->{
                        if (it.response?.result?.isEmpty()!!){
                            DialogHelper.showErrorDialog(requireContext() , getString(R.string.sport_list_not_found))
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
        sportsAdapter = SportsAdapter(sports.toMutableList() , requireContext(),this)
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