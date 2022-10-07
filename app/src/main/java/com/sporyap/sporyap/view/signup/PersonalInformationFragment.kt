package com.sporyap.sporyap.view.signup

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.telephony.PhoneNumberUtils
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.location.*
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.R
import com.sporyap.sporyap.databinding.PersonalInformationFragmentBinding
import com.sporyap.sporyap.utils.Constants
import com.sporyap.sporyap.utils.DialogHelper
import com.sporyap.sporyap.utils.MaterialDialogHelper
import com.sporyap.sporyap.viewmodel.signup.PersonalInformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class PersonalInformationFragment : Fragment(){

    private val viewModel: PersonalInformationViewModel by viewModels()
    private var _binding : PersonalInformationFragmentBinding? = null
    private val binding get() = _binding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private lateinit var city: String
    private lateinit var country: String
    private lateinit var subLocality: String
    private lateinit var gender : String
    private var genders = arrayListOf<String>()
    private lateinit var phoneCodesAdapter: ArrayAdapter<String>
    private var permissionList = arrayListOf<String>()

    private lateinit var uiStateJob : Job
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonalInformationFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        //viewModel.getPhoneCodes()
        addGenders()
        requestLocationPermissions()
        initListeners()
        initCollect()
        setArguments()
    }

    private fun initCollect(){
        uiStateJob = lifecycleScope.launch {
            viewModel.personalInformationUIState.collect {
                when(it){
                    is PersonalInformationViewModel.ViewState.ShowLoading->{
                        mainActivity.progressBar.isVisible = it.isShow
                    }
                    is PersonalInformationViewModel.ViewState.ShowErrorMessage->{
                        DialogHelper.showErrorDialog(requireContext() , it.message)
                    }
                    is PersonalInformationViewModel.ViewState.OnLocationSaved->{
                        viewModel.setUserInformation(
                            requireContext(),
                            binding?.editTextName?.text.toString(),
                            binding?.editTextLastName?.text.toString(),
                            binding?.editTextEmail?.text.toString(),
                            binding?.editTextPhone?.text.toString(),
                            binding?.editTextPassword?.text.toString(),
                            gender,
                            lat.toString(),
                            long.toString(),
                            it.response.result.districtId.toString(),
                            it.response.result.cityId.toString(),
                            it.response.result.countryId.toString()
                        )
                        Navigation.findNavController(requireView()).navigate(R.id.action_personalInformationFragment_to_userTypeFragment)

                    }
                    is PersonalInformationViewModel.ViewState.PhoneCodesOnLoaded->{
                        if (it.response.isEmpty()){
                            return@collect
                        }
                        phoneCodesAdapter = ArrayAdapter(requireActivity().baseContext , android.R.layout.simple_spinner_dropdown_item , it.response)
                        binding?.spinnerPhoneCodes?.adapter = phoneCodesAdapter
                        phoneCodesAdapter.notifyDataSetChanged()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun addGenders(){
        genders.add(requireContext().getString(R.string.choose_gender))
        genders.add(requireContext().getString(R.string.female))
        genders.add(requireContext().getString(R.string.male))
    }

    private fun setArguments(){

        val name = requireArguments().getString(Constants.PREF_NAME)
        if (name !=null){
            binding?.editTextName?.setText(name)
        }

        val lastName = requireArguments().getString(Constants.PREF_LAST_NAME)
        if (lastName != null){
            binding?.editTextLastName?.setText(lastName)
        }

        val email = requireArguments().getString(Constants.PREF_EMAIL)
        if (email != null){
            binding?.editTextEmail?.setText(email)
        }
    }

    private fun initListeners(){

        PhoneNumberUtils.formatNumber(binding?.editTextPhone?.text?.toString(), Locale.getDefault().isO3Country)

        binding?.radioGroupGender?.setOnCheckedChangeListener { _, checkedId ->
            gender = if (R.id.radio_button_female == checkedId && binding?.radioButtonFemale?.isChecked!!) {
                requireContext().getString(R.string.female)
            } else {
                requireContext().getString(R.string.male)
            }
        }

        binding?.buttonEnter?.setOnClickListener {

            if(binding?.editTextName?.text!!.isEmpty()){
                Toast.makeText(requireContext() , getString(R.string.please_enter_first_name) ,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding?.editTextLastName?.text!!.isEmpty()){
                Toast.makeText(requireContext() , getString(R.string.please_enter_last_name) ,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding?.editTextPassword?.text!!.isEmpty() || binding?.editTextPasswordRetry?.text!!.isEmpty() || !viewModel.validatePassword(binding?.editTextPassword?.text!!.toString() , binding?.editTextPasswordRetry?.text!!.toString())){
                Toast.makeText(requireContext() , getString(R.string.your_password_not_equals) ,Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!viewModel.isPasswordValid(binding?.editTextPassword?.text!!.toString())){
                Toast.makeText(requireContext() , requireContext().getString(R.string.password_was_not_correct_format), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(binding?.editTextPhone?.text!!.isEmpty() || !viewModel.isValidMobile(binding?.editTextPhone?.text.toString())){
                Toast.makeText(requireContext() , getString(R.string.please_enter_phone_number), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(binding?.editTextEmail?.text!!.isEmpty() || !viewModel.isValidMail(binding?.editTextEmail?.text.toString())){
                Toast.makeText(requireContext() , getString(R.string.please_enter_email), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!this::gender.isInitialized || gender == requireContext().getString(R.string.choose_gender)){
                Toast.makeText(requireContext() , requireContext().getString(R.string.choose_gender) , Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!this::country.isInitialized || !this::city.isInitialized || !this::subLocality.isInitialized){
                Toast.makeText(requireContext() , getString(R.string.location_could_not_be_found), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.saveLocations(requireContext() , country, city, subLocality)
        }

        binding?.imageViewIcEyeOpenPassword?.setOnClickListener {
            binding?.editTextPassword?.inputType = 129
            binding?.imageViewIcEyeOpenPassword?.visibility = View.GONE
            binding?.imageViewIcEyeClosePassword?.visibility = View.VISIBLE
            binding?.editTextPasswordRetry?.inputType = 129
            binding?.imageViewIcEyeOpenPasswordRetry?.visibility = View.GONE
            binding?.imageViewIcEyeClosePasswordRetry?.visibility = View.VISIBLE
        }

        binding?.imageViewIcEyeClosePassword?.setOnClickListener {
            binding?.imageViewIcEyeClosePasswordRetry?.visibility = View.GONE
            binding?.imageViewIcEyeOpenPasswordRetry?.visibility = View.VISIBLE
            binding?.editTextPassword?.inputType = InputType.TYPE_CLASS_TEXT
            binding?.editTextPasswordRetry?.inputType = InputType.TYPE_CLASS_TEXT
            binding?.imageViewIcEyeClosePassword?.visibility = View.GONE
            binding?.imageViewIcEyeOpenPassword?.visibility = View.VISIBLE
        }

        binding?.imageViewIcEyeOpenPasswordRetry?.setOnClickListener {
            binding?.editTextPasswordRetry?.inputType = 129
            binding?.editTextPassword?.inputType = 129
            binding?.imageViewIcEyeOpenPasswordRetry?.visibility = View.GONE
            binding?.imageViewIcEyeClosePasswordRetry?.visibility = View.VISIBLE
            binding?.imageViewIcEyeOpenPassword?.visibility = View.GONE
            binding?.imageViewIcEyeClosePassword?.visibility = View.VISIBLE
        }

        binding?.imageViewIcEyeClosePasswordRetry?.setOnClickListener {
            binding?.imageViewIcEyeClosePasswordRetry?.visibility = View.GONE
            binding?.imageViewIcEyeOpenPasswordRetry?.visibility = View.VISIBLE
            binding?.editTextPassword?.inputType = InputType.TYPE_CLASS_TEXT
            binding?.editTextPasswordRetry?.inputType = InputType.TYPE_CLASS_TEXT
            binding?.imageViewIcEyeClosePassword?.visibility = View.GONE
            binding?.imageViewIcEyeOpenPassword?.visibility = View.VISIBLE
        }
    }

    private fun getLastLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && isLocationEnabled()) {

            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location == null) {
                    getNewLocation()
                } else {
                    lat = location.latitude
                    long = location.longitude
                    getAddress(location.latitude, location.longitude)
                }
            }
        }
    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            lat = p0.lastLocation.latitude
            long = p0.lastLocation.longitude
            getAddress(p0.lastLocation.latitude, p0.lastLocation.longitude)
        }
    }

    private fun getAddress(lat: Double, long: Double) {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)

        if (address.isNotEmpty()) {
            country = address.first().countryName
            city = address.first().adminArea
            subLocality = address.first().subAdminArea
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallBack,
            Looper.myLooper()!!
        )
    }

    private val locationPermissionsLaunch = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        if (permissions.any { x-> !x.value }){
            MaterialDialogHelper().showCustomDialog(requireContext(),getString(R.string.you_should_give_location_permission), 1)
            return@registerForActivityResult
        }
        getLastLocation()
    }

    private fun hasLocationPermissions() : Boolean = permissionList.all {
        ContextCompat.checkSelfPermission(requireContext() , it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            permissionList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }else{
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (!hasLocationPermissions()){
            locationPermissionsLaunch.launch(permissionList.toTypedArray())
        }else{
            getLastLocation()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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