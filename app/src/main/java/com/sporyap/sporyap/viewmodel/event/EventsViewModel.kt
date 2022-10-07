package com.sporyap.sporyap.viewmodel.event

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sporyap.sporyap.data.entity.MediaObject
import com.sporyap.sporyap.data.rp.MainMediaObjectDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Console
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(): ViewModel(){

    sealed class ViewState {
        class ShowLoading(val isShow: Boolean) : ViewState()
        class ShowErrorMessage(val message: String) : ViewState()
        object Empty : ViewState()
    }

    private val _eventsUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val eventsUIState : StateFlow<ViewState> = _eventsUIState

    val kRepo = MainMediaObjectDaoRepository()

    var mediaObjectList=  MutableLiveData<List<MediaObject>>()

    init {
        getAllViewModel()
        mediaObjectList = kRepo.getMutableList()
    }

    fun getAllViewModel(){
        kRepo.getAllObjects()
        Log.e("deneme","1 çalıştı")
    }

}