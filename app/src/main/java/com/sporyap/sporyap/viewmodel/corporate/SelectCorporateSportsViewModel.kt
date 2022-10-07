package com.sporyap.sporyap.viewmodel.corporate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.response.sport.GetSports
import com.sporyap.sporyap.data.rp.SportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCorporateSportsViewModel @Inject constructor(private val sportRepository: SportRepository) : ViewModel() {

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnLoaded(val response : GetSports) : ViewState()
        object Empty : ViewState()
    }

    private lateinit var job : Job

    private val _corporateSportUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val corporateSportUIState : StateFlow<ViewState> = _corporateSportUIState

    fun getSports(context : Context){
        job = viewModelScope.launch {
            _corporateSportUIState.value = ViewState.ShowLoading(true)
            sportRepository.getSports().apply {
                _corporateSportUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _corporateSportUIState.value = ViewState.OnLoaded(this.body()!!)
                }else{
                    _corporateSportUIState.value =
                        ViewState.ShowErrorMessage(context.getString(R.string.please_check_entered_information))
                }
            }
        }
    }

    override fun onCleared() {
        if(this::job.isInitialized){
            job.cancel()
        }
        super.onCleared()
    }
}