package com.sporyap.sporyap.viewmodel.corporate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sporyap.sporyap.R
import com.sporyap.sporyap.data.network.response.corporate.CorporateTypesResponse
import com.sporyap.sporyap.data.rp.CorporateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CorporateTypeViewModel @Inject constructor(private val corporateRepository: CorporateRepository): ViewModel() {

    sealed class ViewState{
        class ShowLoading(val isShow : Boolean) : ViewState()
        class ShowErrorMessage(val message : String) : ViewState()
        class OnLoadedCorporateTypes(val response : CorporateTypesResponse) : ViewState()
        object Empty : ViewState()
    }

    private val _corporateTypeUIState = MutableStateFlow<ViewState>(ViewState.Empty)
    val corporateTypeUIState : StateFlow<ViewState> = _corporateTypeUIState

    fun getCorporateTypes(context: Context){
        viewModelScope.launch {
            _corporateTypeUIState.value = ViewState.ShowLoading(true)
            corporateRepository.getCorporateTypes().apply {
                _corporateTypeUIState.value = ViewState.ShowLoading(false)
                if (this.isSuccessful && this.body()!!.succeeded){
                    _corporateTypeUIState.value = ViewState.OnLoadedCorporateTypes(this.body()!!)
                }else{
                    _corporateTypeUIState.value = ViewState.ShowErrorMessage(context.getString(R.string.corporate_types_could_not_get))
                }
            }
        }
    }
}