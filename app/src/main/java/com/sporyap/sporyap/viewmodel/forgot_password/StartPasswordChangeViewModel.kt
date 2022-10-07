package com.sporyap.sporyap.viewmodel.forgot_password

import androidx.lifecycle.ViewModel
import com.sporyap.sporyap.data.rp.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartPasswordChangeViewModel @Inject constructor(private val accountRepository: AccountRepository): ViewModel()