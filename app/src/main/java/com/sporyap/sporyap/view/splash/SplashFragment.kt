package com.sporyap.sporyap.view.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.FragmentSplashBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: MainActivity
    private lateinit var uiStateJob : Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.hide()
        mainActivity.bottomNavigationView.visibility = View.GONE
        uiStateJob = lifecycleScope.launch {
            delay(Constants.SPLASH_DELAY)
        }
        if (Prefs.getKeySharedPreferencesBoolean(
                context = requireContext(),
                key = Constants.PREF_IS_LOGGED
            )
        ) {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_splashFragment_to_eventsFragment)
        } else {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_splashFragment_to_welcomeFragment)
        }
    }

    override fun onDestroyView() {
        if (this::uiStateJob.isInitialized){
            uiStateJob.cancel()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}