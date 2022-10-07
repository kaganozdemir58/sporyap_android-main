package com.sporyap.sporyap.view.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.adapter.event.EventCategoriesAdapter
import com.sporyap.sporyap.adapter.event.EventOwnerTypeAdapter
import com.sporyap.sporyap.adapter.event.EventTypeAdapter
import com.sporyap.sporyap.adapter.gender.GenderTypeAdapter
import com.sporyap.sporyap.adapter.listeners.EventCategoryItemClickListener
import com.sporyap.sporyap.adapter.sport.SportsAdapter
import com.sporyap.sporyap.data.network.response.event.genders.Result
import com.sporyap.sporyap.databinding.CreateEventFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.MaterialDialogHelper
import com.sporyap.sporyap.utils.Prefs
import com.sporyap.sporyap.adapter.listeners.OnSportItemClickListener
import com.sporyap.sporyap.viewmodel.event.CreateEventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateEventFragment : Fragment(), OnSportItemClickListener , EventCategoryItemClickListener , DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {

    private val viewModel: CreateEventViewModel by viewModels()
    private var _binding : CreateEventFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var uiStateJob : Job
    private lateinit var mainActivity : MainActivity
    private lateinit var genderTypeAdapter : GenderTypeAdapter
    private lateinit var eventTypeAdapter : EventTypeAdapter
    private lateinit var sportsAdapter: SportsAdapter
    private lateinit var eventCategoriesAdapter: EventCategoriesAdapter
    private var sports : List<com.sporyap.sporyap.data.network.response.sport.Result> = listOf()
    private var sportId : Int = 0
    private lateinit var token : String
    private val calendar = Calendar.getInstance()
    private lateinit var defaultStartHour : String
    private lateinit var defaultEndHour : String
    private var isClickedStartTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = CreateEventFragmentBinding.inflate(inflater,container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        getDefaultHours()
        initCollect()
        initListeners()
        token = Prefs.getKeySharedPreferences(requireContext() , Constants.PREF_TOKEN)
        viewModel.getSports()
        viewModel.getAllGenderTypes(token)
        viewModel.getEventTypes(token)
        viewModel.getAllEventOwnerTypes(token)
    }

    private fun getDefaultHours(){
        defaultStartHour = calendar.get(Calendar.HOUR).plus(1).toString() + ":00"
        defaultEndHour = calendar.get(Calendar.HOUR).plus(2).toString() + ":00"

        binding?.textViewStartTime?.text = defaultStartHour
        binding?.textViewEndTime?.text = defaultEndHour
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
            viewModel.createEventUIState.collect{
                when(it){
                    is CreateEventViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is CreateEventViewModel.ViewState.ShowErrorMessage->{
                        MaterialDialogHelper().showCustomDialog(requireContext() , it.message , 2)
                    }
                    is CreateEventViewModel.ViewState.GenderTypesLoaded->{
                        initRecyclerView(binding?.recyclerViewGenders)
                        fillGenderTypes(it.response)
                    }
                    is CreateEventViewModel.ViewState.EventTypesLoaded->{
                        initRecyclerView(binding?.recyclerViewEventTypes)
                        fillEventTypes(it.response)
                    }
                    is CreateEventViewModel.ViewState.SportsOnLoaded->{
                        if (it.response?.isEmpty()!!){
                            MaterialDialogHelper().showCustomDialog(requireContext() , getString(R.string.sport_list_not_found) , 2)
                            return@collect
                        }
                        initRecyclerView(binding?.recyclerViewSports)
                        sports = it.response
                        fillSports(it.response)
                    }
                    is CreateEventViewModel.ViewState.EventCategoriesLoaded->{
                        initRecyclerView(binding?.recyclerViewEventCategories)
                        fillEventCategories(it.response)
                    }
                    is CreateEventViewModel.ViewState.EventOwnerTypesLoaded->{
                        initRecyclerView(binding?.recyclerViewEventOwnerTypes)
                        fillEventOwnerTypes(it.response)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initListeners(){
        binding?.editTextDate?.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext() , this , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        binding?.editTextStartTime?.setOnClickListener {
            isClickedStartTime = true
            val timePickerDialog = TimePickerDialog(requireContext() , this , calendar.get(Calendar.HOUR) , calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(requireContext()))
            timePickerDialog.show()
        }

        binding?.editTextEndTime?.setOnClickListener {
            isClickedStartTime = false
            val timePickerDialog = TimePickerDialog(requireContext() , this , calendar.get(Calendar.HOUR) , calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(requireContext()))
            timePickerDialog.show()
        }

        binding?.textViewToday?.setOnClickListener {
            binding?.textViewToday?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding?.textViewToday?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_sign_up_button)
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView?){
        recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL , false)
        recyclerView?.itemAnimator = DefaultItemAnimator()
    }

    private fun fillEventOwnerTypes(eventOwnerTypes: List<com.sporyap.sporyap.data.network.response.event.owner_types.Result>) {
        val eventOwnerTypeAdapter = EventOwnerTypeAdapter(eventOwnerTypes)
        binding?.recyclerViewEventOwnerTypes?.adapter = eventOwnerTypeAdapter
        eventOwnerTypeAdapter.submitList(eventOwnerTypes)
    }

    private fun fillEventCategories(eventCategories: List<com.sporyap.sporyap.data.network.response.event.categories.Result>) {
        eventCategoriesAdapter = EventCategoriesAdapter(eventCategories, this)
        binding?.recyclerViewEventCategories?.adapter = eventCategoriesAdapter
        eventCategoriesAdapter.submitList(eventCategories)
    }

    private fun fillSports(sports: List<com.sporyap.sporyap.data.network.response.sport.Result>) {
        sportsAdapter = SportsAdapter(sports.toMutableList() , requireContext(),this)
        binding?.recyclerViewSports?.adapter = sportsAdapter
        sportsAdapter.submitList(sports)
    }

    private fun fillEventTypes(eventTypes: List<com.sporyap.sporyap.data.network.response.event.types.Result>) {
        eventTypeAdapter = EventTypeAdapter(eventTypes)
        eventTypeAdapter.submitList(eventTypes)
        binding?.recyclerViewEventTypes?.adapter = eventTypeAdapter
    }

    private fun fillGenderTypes(genderTypes : List<Result>){
        genderTypeAdapter = GenderTypeAdapter(genderTypes)
        genderTypeAdapter.submitList(genderTypes)
        binding?.recyclerViewGenders?.adapter = genderTypeAdapter
    }

    override fun onClick(sport: com.sporyap.sporyap.data.network.response.sport.Result) {
        val selectedSport = sports.find { x-> x.id == sport.id }
        selectedSport?.isSelected = !selectedSport?.isSelected!!
        if (selectedSport.isSelected){
            sportId = selectedSport.id
            sports.forEach { x->
                if (x.id != sportId){
                    x.isSelected = false
                }
            }
        }
        sportsAdapter.updateSports(sports)
        viewModel.getAllEventCategories(token , sportId)
    }

    override fun onItemClick(eventCategory: com.sporyap.sporyap.data.network.response.event.categories.Result) {}

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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR , year)
        calendar.set(Calendar.MONTH , month)
        calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth)
        val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT , Locale.getDefault())
        val selectedDate = dateFormat.format(calendar.time)
        binding?.editTextDate?.setText(selectedDate)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY , hourOfDay)
        calendar.set(Calendar.MINUTE , minute)
        val timeFormat = SimpleDateFormat(Constants.TIME_FORMAT , Locale.getDefault())
        val selectedTime = timeFormat.format(calendar.time)
        if (isClickedStartTime){
            binding?.editTextStartTime?.setText(selectedTime)
        }else{
            binding?.editTextEndTime?.setText(selectedTime)
        }
    }
}