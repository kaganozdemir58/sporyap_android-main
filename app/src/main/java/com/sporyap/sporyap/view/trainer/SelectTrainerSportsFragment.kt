package com.sporyap.sporyap.view.trainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.response.sport.Result
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.adapter.sport.SportsAdapter
import com.sporyap.sporyap.databinding.SelectTrainerSportsFragmentBinding
import com.sporyap.sporyap.adapter.listeners.OnSportItemClickListener
import com.sporyap.sporyap.viewmodel.trainer.SelectTrainerSportsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectTrainerSportsFragment : Fragment(), OnSportItemClickListener {

    private val viewModel: SelectTrainerSportsViewModel by viewModels()
    private var _binding : SelectTrainerSportsFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var sportsAdapter: SportsAdapter
    private lateinit var uiStateScope : Job
    private lateinit var mainActivity: MainActivity
    private var selectedTrainerSportIdList = mutableListOf<Int>()
    private var selectedSportIdList = mutableListOf<Int>()
    private var trainerSports : List<Result> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SelectTrainerSportsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
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
        selectedSportIdList = requireArguments().getIntArray("sportIdList")?.toMutableList()!!
    }

    private fun initListeners() {
        binding?.buttonComplete?.setOnClickListener {
            if (selectedTrainerSportIdList.isEmpty()){
                Toast.makeText(requireContext() , requireContext().getString(R.string.please_choose_at_least_one_sport) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val bundle = Bundle()
            bundle.putIntArray("athleteSports", selectedSportIdList.toIntArray())
            bundle.putInt("userType", 2)
            bundle.putIntArray("trainerSports", selectedTrainerSportIdList.toIntArray())
            Navigation.findNavController(requireView()).navigate(R.id.action_selectTrainerSportsFragment_to_userVerificationFragment, bundle)
        }
    }

    private fun initCollect(){
        uiStateScope = lifecycleScope.launch {
            viewModel.trainerSportUIState.collect {
                when(it){
                    is SelectTrainerSportsViewModel.ViewState.ShowLoading->{

                        if (it.isShow){
                            mainActivity.progressBar.visibility = View.VISIBLE
                        }else{
                            mainActivity.progressBar.visibility = View.GONE
                        }
                    }
                    is SelectTrainerSportsViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(requireContext() , it.message)
                    }
                    is SelectTrainerSportsViewModel.ViewState.OnLoaded->{
                        if (it.response.result.isEmpty()){
                            return@collect
                        }
                        trainerSports = it.response.result
                        fillSports(it.response.result)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun fillSports(result: List<Result>) {
        binding?.recyclerViewSports?.hasFixedSize()
        binding?.recyclerViewSports?.layoutManager = GridLayoutManager(requireContext() ,3 , GridLayoutManager.VERTICAL , false)
        binding?.recyclerViewSports?.itemAnimator = DefaultItemAnimator()
        sportsAdapter = SportsAdapter(result.toMutableList() , requireContext(),this)
        binding?.recyclerViewSports?.adapter = sportsAdapter
        sportsAdapter.submitList(result)
    }

    override fun onClick(sport: Result) {
        val selectedSport = trainerSports.find { x-> x.id == sport.id }
        selectedSport?.isSelected = !selectedSport?.isSelected!!

        if (selectedSport.isSelected){
            selectedTrainerSportIdList.add(sport.id)
        }
        sportsAdapter.updateSports(trainerSports)
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