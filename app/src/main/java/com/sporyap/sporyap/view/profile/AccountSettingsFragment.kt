package com.sporyap.sporyap.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.data.network.response.account.profile_infos.TrainerSport
import com.sporyap.sporyap.data.network.response.account.profile_infos.UserProfileInfoResponse
import com.sporyap.sporyap.data.network.response.account.profile_infos.UsersSport
import com.sporyap.sporyap.data.network.response.sport.Result
import com.sporyap.sporyap.databinding.AccountSettingsFragmentBinding
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.adapter.sport.SportsAdapter
import com.sporyap.sporyap.adapter.listeners.OnSportItemClickListener
import com.sporyap.sporyap.viewmodel.profile.AccountSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountSettingsFragment : Fragment() , OnSportItemClickListener {

    private val viewModel: AccountSettingsViewModel by viewModels()
    private var _binding : AccountSettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var uiStateJob : Job
    private lateinit var sportsAdapter: SportsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AccountSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        initCollect()
        viewModel.getUserProfileInformation(requireContext())
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
            viewModel.accountSettingsUIState.collect {
                when (it){
                    is AccountSettingsViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is AccountSettingsViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(context = requireContext() , message = it.message)
                    }
                    is AccountSettingsViewModel.ViewState.GetUserProfileInformation->{
                        setUserInformation(it.response)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUserInformation(userProfileResponse: UserProfileInfoResponse) {
        binding.textViewName.text = userProfileResponse.result.name
        binding.textViewSurName.text = userProfileResponse.result.surName
        binding.textViewGender.text = userProfileResponse.result.genderTypeName
        binding.textViewBirthDate.text = userProfileResponse.result.birthDate
        binding.textViewMail.text = userProfileResponse.result.email
        binding.textViewPhone.text = userProfileResponse.result.phone

        if (userProfileResponse.result.userLocations.isNotEmpty()){
            binding.textViewCountry.text = userProfileResponse.result.userLocations.first().countryName
            binding.textViewCity.text = userProfileResponse.result.userLocations.first().cityName
            binding.textViewDistrict.text = userProfileResponse.result.userLocations.first().districtName
            val location = userProfileResponse.result.userLocations.first().longitude.toString() + " - " + userProfileResponse.result.userLocations.first().latitude.toString()
            binding.textViewLocation.text = location
        }

        if (userProfileResponse.result.corporate.name == null){
            binding.corporateNameLayout.visibility = View.GONE
            binding.userCorporateLayout.visibility = View.GONE
        }

        if (userProfileResponse.result.corporate.latitude == 0.0 || userProfileResponse.result.corporate.longitude == 0.0){
            binding.userCorporateLocationLayout.visibility = View.GONE
        }

        when(userProfileResponse.result.userTypeId){
            1->{
                binding.textViewSportProfile.visibility = View.VISIBLE
                binding.recyclerViewUserSports.visibility = View.VISIBLE
                binding.textViewTrainerProfile.visibility = View.GONE
                binding.recyclerViewTrainerSports.visibility = View.GONE
                binding.textViewCorporateSportProfile.visibility = View.GONE
                binding.recyclerViewCorporateSports.visibility = View.GONE
                if (userProfileResponse.result.usersSports.isNotEmpty()){
                    fillSports(binding.recyclerViewUserSports , convertSportList(userProfileResponse.result.usersSports))
                }
            }
            2->{
                binding.textViewTrainerProfile.visibility = View.VISIBLE
                binding.recyclerViewTrainerSports.visibility = View.VISIBLE
                binding.textViewSportProfile.visibility = View.VISIBLE
                binding.recyclerViewUserSports.visibility = View.VISIBLE
                binding.textViewCorporateSportProfile.visibility = View.GONE
                binding.recyclerViewCorporateSports.visibility = View.GONE
                if (userProfileResponse.result.usersSports.isNotEmpty()){
                    fillSports(binding.recyclerViewUserSports , convertSportList(userProfileResponse.result.usersSports))
                }
                if (userProfileResponse.result.trainerSports.isNotEmpty()){
                    fillSports(binding.recyclerViewTrainerSports , convertTrainerSportList(userProfileResponse.result.trainerSports))
                }
            }
            3->{
                binding.textViewSportProfile.visibility = View.GONE
                binding.recyclerViewUserSports.visibility = View.GONE
                binding.textViewTrainerProfile.visibility = View.GONE
                binding.recyclerViewTrainerSports.visibility = View.GONE
                binding.textViewCorporateSportProfile.visibility = View.VISIBLE
                binding.recyclerViewCorporateSports.visibility = View.VISIBLE
                if (userProfileResponse.result.usersSports.isNotEmpty()){
                    fillSports(binding.recyclerViewCorporateSports , convertSportList(userProfileResponse.result.usersSports))
                }
            }
        }
    }

    private fun convertSportList(sports : List<UsersSport>) : List<Result>{
        val resultList = arrayListOf<Result>()
        for (sport in sports){
            resultList.add(Result(id = sport.sportId  , image = sport.image , name = sport.name , isSelected = false))
        }
        return resultList
    }

    private fun convertTrainerSportList(sports : List<TrainerSport>) : List<Result>{
        val resultList = arrayListOf<Result>()
        for (sport in sports){
            resultList.add(Result(id = sport.sportId  , image = sport.image , name = sport.name , isSelected = false))
        }
        return resultList
    }

    private fun fillSports(recyclerView : RecyclerView, sports: List<Result>) {
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = GridLayoutManager(requireContext() ,3 , GridLayoutManager.VERTICAL , false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        sportsAdapter = SportsAdapter(sports.toMutableList() , requireContext(),this)
        recyclerView.adapter = sportsAdapter
        sportsAdapter.submitList(sports)
    }

    override fun onDestroyView() {
        if (this::uiStateJob.isInitialized){
            uiStateJob.cancel()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onClick(sport: Result) {
        Log.d("Clicked", "True")
    }
}