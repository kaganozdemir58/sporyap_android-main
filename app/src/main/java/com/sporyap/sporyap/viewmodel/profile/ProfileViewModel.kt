package com.sporyap.sporyap.viewmodel.profile

import androidx.lifecycle.ViewModel
import com.sporyap.sporyap.data.rp.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val accountRepository: AccountRepository): ViewModel()