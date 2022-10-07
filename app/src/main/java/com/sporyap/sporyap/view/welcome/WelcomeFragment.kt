package com.sporyap.sporyap.view.welcome

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.sporyap.sporyap.R
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.sporyap.sporyap.MainActivity
import com.sporyap.sporyap.databinding.WelcomeFragmentBinding
import com.sporyap.sporyap.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import java.lang.NullPointerException

@AndroidEntryPoint
class WelcomeFragment : Fragment(){

//    private val viewModel: WelcomeViewModel by viewModels()
    private var _binding : WelcomeFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var callBackManager : CallbackManager

    private var faceBookFirstName : String? = null
    private var faceBookLastName : String? = null
    private var faceBookEmail : String? = null
    private var faceBookUserId : String? = null
    private var faceBookGender : String? = null
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private var googleUserName : String? = null
    private var googleFamilyName : String? = null
    private var googleEmail : String? = null
    private var isExpireFaceBookToken : Boolean = false
    private lateinit var mainActivity : MainActivity

    private val googleSignInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.data != null){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val googleAccount = task.result
                googleUserName = googleAccount.displayName
                googleFamilyName = googleAccount.familyName
                googleEmail = googleAccount.email
                val bundle = Bundle()
                bundle.putString(Constants.PREF_NAME, googleUserName)
                bundle.putString(Constants.PREF_LAST_NAME, googleFamilyName)
                bundle.putString(Constants.PREF_EMAIL, googleEmail)
                Navigation.findNavController(requireView()).navigate(R.id.action_welcomeFragment_to_personalInformationFragment, bundle)
            }catch (ex : ApiException){
                Log.d("Api Exception" , ex.message!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildFaceBookLoginServices()
    }

    private fun buildFaceBookLoginServices(){
        callBackManager = CallbackManager.Factory.create()
        val accessToken = AccessToken.getCurrentAccessToken()
        isExpireFaceBookToken =  accessToken?.isExpired?:true
    }

    private fun buildGoogleLoginServices(){
         googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
         googleSignInClient = GoogleSignIn.getClient(requireContext() , googleSignInOptions)
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (googleSignInAccount != null){
            googleUserName = googleSignInAccount.displayName
            googleFamilyName = googleSignInAccount.familyName
            googleEmail = googleSignInAccount.email

            val bundle = Bundle()
            bundle.putString(Constants.PREF_NAME, googleUserName)
            bundle.putString(Constants.PREF_LAST_NAME, googleFamilyName)
            bundle.putString(Constants.PREF_EMAIL, googleEmail)

            Navigation.findNavController(requireView()).navigate(R.id.action_welcomeFragment_to_personalInformationFragment, bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        buildGoogleLoginServices()
        initListeners()
        loadVideo()
        setAlphaValues()
    }

    private fun setAlphaValues() {
        lifecycleScope.launch {
            delay(12000L)
            binding?.footerLayout?.visibility = View.VISIBLE
            binding?.textViewKVKK?.visibility = View.VISIBLE
            binding?.buttonSignUp?.background?.alpha = 165
            binding?.buttonFacebook?.background?.alpha = 165
            //binding?.googleSignInButton?.background?.alpha = 165
            binding?.buttonGoogle?.background?.alpha = 165
        }
    }

    private fun initListeners(){
       binding?.videoView?.setOnCompletionListener {
           binding?.videoView?.start()
       }
        
        binding?.buttonSignUp?.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_welcomeFragment_to_personalInformationFragment)
        }
        
        binding?.buttonLogin?.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        binding?.textViewKVKK?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_welcomeFragment_to_webViewFragment)
        }

        binding?.buttonFacebookLogin?.registerCallback(callBackManager , object : FacebookCallback<LoginResult>{
            override fun onCancel() {
                Log.d("onCancel", "worked")
            }

            override fun onError(error: FacebookException) {
                Log.d("onError" , error.localizedMessage!!)
            }

            override fun onSuccess(result: LoginResult) {
                Log.d("result" , result.accessToken.token)

                val request = GraphRequest.newMeRequest(result.accessToken
                ) { obj, _ ->
                    if (obj != null) {
                        try {
                            faceBookFirstName = obj.getString("first_name")
                            faceBookLastName = obj.getString("last_name")
                            faceBookEmail = obj.getString("email")
                            faceBookUserId = obj.getString("id")
                            faceBookGender = obj.getString("gender")

                            val bundle = Bundle()
                            bundle.putString(Constants.PREF_NAME, faceBookFirstName)
                            bundle.putString(Constants.PREF_LAST_NAME, faceBookLastName)
                            bundle.putString(Constants.PREF_EMAIL, faceBookEmail)
                            //bundle.putBoolean("gender", faceBookGender)

                            Navigation.findNavController(requireView()).navigate(R.id.action_welcomeFragment_to_personalInformationFragment, bundle)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "first_name,,email,id,birthday,gender")
                request.parameters = parameters
                request.executeAsync()
                mainActivity.progressBar.visibility = View.GONE
            }
        })

        binding?.googleSignInButton?.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInResultLauncher.launch(signInIntent)
        }

        binding?.buttonFacebook?.setOnClickListener {
            mainActivity.progressBar.visibility = View.VISIBLE
            LoginManager.getInstance().logInWithReadPermissions(this, callBackManager , listOf("first_name", "last_name", "email" ,"id", "birthday", "gender"))
            binding?.buttonFacebookLogin?.performClick()
        }

        binding?.buttonGoogle?.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInResultLauncher.launch(signInIntent)
        }
    }

    private fun loadVideo() {
        binding?.videoView?.setVideoURI((Uri.parse(Constants.INTRO_VIDEO_URL)))
        binding?.videoView?.start()
    }
}

