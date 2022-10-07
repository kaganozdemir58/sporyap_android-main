package com.sporyap.sporyap.view.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sporyap.sporyap.R
import com.sporyap.sporyap.viewmodel.signup.CreatePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePasswordFragment : Fragment() {

    private lateinit var viewModel: CreatePasswordViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_password_fragment, container, false)
    }
}