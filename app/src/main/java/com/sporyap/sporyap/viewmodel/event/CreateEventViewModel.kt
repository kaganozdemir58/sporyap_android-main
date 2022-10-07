package com.sporyap.sporyap.viewmodel.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.data.network.response.sport.Result
import com.sporyap.sporyap.data.rp.EventRepository
import com.sporyap.sporyap.use_case.sport.SportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(private val eventRepository: EventRepository , private val sportUseCases: SportUseCases): ViewModel(){

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class SportsOnLoaded(val response : List<Result>?) : ViewState()
        class EventTypesLoaded(val response : List<com.sporyap.sporyap.data.network.response.event.types.Result>): ViewState()
        class GenderTypesLoaded(val response : List<com.sporyap.sporyap.data.network.response.event.genders.Result>) : ViewState()
        class EventCategoriesLoaded(val response : List<com.sporyap.sporyap.data.network.response.event.categories.Result>) : ViewState()
        class EventOwnerTypesLoaded(val response : List<com.sporyap.sporyap.data.network.response.event.owner_types.Result>) : ViewState()
        object Empty : ViewState()
    }
    private var getSportsJob : Job? = null
    private val _createEventUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val createEventUIState : StateFlow<ViewState> = _createEventUIState

    fun getSports(){
        getSportsJob?.cancel()
        getSportsJob = viewModelScope.launch {
            _createEventUIState.value = ViewState.ShowLoading(true)
            sportUseCases.sportUseCase.invoke().apply {
                _createEventUIState.value = ViewState.ShowLoading(false)
                _createEventUIState.value = ViewState.SportsOnLoaded(this?.result)
            }
        }
    }

    fun getEventTypes(token : String){
        viewModelScope.launch {
            _createEventUIState.value = ViewState.ShowLoading(true)
            eventRepository.getAllEventTypes(token).apply {
                _createEventUIState.value = ViewState.ShowLoading(false)
                if(this.isSuccessful && this.body() != null && this.body()?.succeeded!!){
                    _createEventUIState.value = ViewState.EventTypesLoaded(this.body()!!.result)
                }else{
                    _createEventUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }

    fun getAllGenderTypes(token : String){
        viewModelScope.launch {
            _createEventUIState.value = ViewState.ShowLoading(true)
            eventRepository.getAllGenderTypes(token).apply {
                _createEventUIState.value = ViewState.ShowLoading(false)
                if(this.isSuccessful && this.body() != null && this.body()?.succeeded!!){
                    _createEventUIState.value = ViewState.GenderTypesLoaded(this.body()!!.result)
                }else{
                    _createEventUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }

    fun getAllEventCategories(token : String, sportId : Int){
        viewModelScope.launch {
            _createEventUIState.value = ViewState.ShowLoading(true)
            eventRepository.getAllEventCategories(token , sportId).apply {
                _createEventUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()?.succeeded!!){
                    _createEventUIState.value = ViewState.EventCategoriesLoaded(this.body()?.result!!)
                }else{
                    _createEventUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }

    fun getAllEventOwnerTypes(token : String){
        viewModelScope.launch{
            _createEventUIState.value = ViewState.ShowLoading(true)
            eventRepository.getAllEventOwnerTypes(token).apply {
                _createEventUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()?.succeeded!!){
                    _createEventUIState.value = ViewState.EventOwnerTypesLoaded(this.body()?.result!!)
                }else{
                    _createEventUIState.value = ViewState.ShowErrorMessage(this.message())
                }
            }
        }
    }

    override fun onCleared() {
        getSportsJob?.cancel()
        super.onCleared()
    }
}