package com.sporyap.sporyap.viewmodel.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.data.network.response.sport.GetSports
import com.sporyap.sporyap.use_case.sport.SportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectYourSportsViewModel @Inject constructor(private val sportUseCases: SportUseCases) : ViewModel() {

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnLoaded(val response : GetSports?) : ViewState()
        object Empty : ViewState()
    }
    private var getSportsJob : Job? = null
    private val _userSportUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val userSportUIState : StateFlow<ViewState> = _userSportUIState

    fun getSports(){
        getSportsJob?.cancel()
        getSportsJob = viewModelScope.launch {
            _userSportUIState.value = ViewState.ShowLoading(true)
            sportUseCases.sportUseCase.invoke().apply {
                _userSportUIState.value = ViewState.ShowLoading(false)
                _userSportUIState.value = ViewState.OnLoaded(this)
            }
        }
    }

    override fun onCleared() {
        getSportsJob?.cancel()
        super.onCleared()
    }
}