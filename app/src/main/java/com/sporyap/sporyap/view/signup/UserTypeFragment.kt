package com.sporyap.sporyap.view.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.UserTypeFragmentBinding
import com.sporyap.sporyap.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserTypeFragment : Fragment() {

//    private val viewModel: UserTypeViewModel by viewModels()
    private var _binding : UserTypeFragmentBinding? = null
    private val binding get() = _binding!!
    private var userType : Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserTypeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners(){
        binding.buttonAthlete.setOnClickListener {
            userType = 1
            binding.buttonAthlete.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_active_button)
            binding.buttonAthlete.setTextColor(ContextCompat.getColor(requireContext() , R.color.white))

            binding.buttonTrainer.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
            binding.buttonTrainer.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))

            binding.buttonCorporate.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
            binding.buttonCorporate.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))
        }

        binding.buttonTrainer.setOnClickListener {
            userType = 2
            binding.buttonTrainer.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_active_button)
            binding.buttonTrainer.setTextColor(ContextCompat.getColor(requireContext() , R.color.white))

            binding.buttonAthlete.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
            binding.buttonAthlete.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))

            binding.buttonCorporate.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
            binding.buttonCorporate.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))
        }

        binding.buttonCorporate.setOnClickListener {
            userType = 0
            binding.buttonCorporate.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_active_button)
            binding.buttonCorporate.setTextColor(ContextCompat.getColor(requireContext() , R.color.white))

            binding.buttonTrainer.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
            binding.buttonTrainer.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))

            binding.buttonAthlete.background = ContextCompat.getDrawable(requireContext() , R.drawable.bg_user_type)
            binding.buttonAthlete.setTextColor(ContextCompat.getColor(requireContext() , R.color.app_main_color))
        }

        binding.buttonEnter.setOnClickListener {
            if (userType < 0){
                Toast.makeText(requireContext(), getString(R.string.please_select_user_type) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            navigate(userType)
        }
    }

    private fun navigate(userType : Int){
        if (userType == 0){
            Navigation.findNavController(requireView()).navigate(R.id.action_userTypeFragment_to_corporateNameFragment)
            return
        }
        val bundle = Bundle()
        bundle.putInt(Constants.ARG_USER_TYPE, userType)
        Navigation.findNavController(requireView()).navigate(R.id.action_userTypeFragment_to_selectYourSportsFragment, bundle)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}