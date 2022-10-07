package com.sporyap.sporyap.view.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.adapter.event.EventVideoRVAdapter
import com.sporyap.sporyap.databinding.EventsFragmentBinding
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.viewmodel.event.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()
    private var _binding : EventsFragmentBinding?=null
    private val binding get() = _binding
    private lateinit var mainActivity: MainActivity
    private lateinit var uiStateJob : Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.hide()
        mainActivity.bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventsFragmentBinding.inflate(inflater, container, false)

        _binding!!.rv.layoutManager = LinearLayoutManager(this.context)

        viewModel.mediaObjectList.observe(viewLifecycleOwner){
            val adapter = EventVideoRVAdapter(requireContext(),it)
            _binding!!.rv.adapter = adapter


        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        initCollect()
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
            viewModel.eventsUIState.collect {
                when(it){
                    is EventsViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is EventsViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(requireContext() , it.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroy() {
        if (this::uiStateJob.isInitialized){
            uiStateJob.cancel()
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}