package playzone.tj.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.databinding.FragmentRegisterBinding
import playzone.tj.retrofit.MainAPI
import playzone.tj.retrofit.models.registration.Otp
import playzone.tj.retrofit.models.registration.RegisterReceiveRemote
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment
import playzone.tj.utils.retrofit
import retrofit2.HttpException


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var userInfoForRegister: RegisterReceiveRemote
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar2.visibility = View.GONE
        binding.btnCreateAccount.isEnabled = true

        binding.btnCreateAccount.setOnClickListener {
            val login = binding.login.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (login.isNotEmpty() and email.isNotEmpty() and password.isNotEmpty()) {

              if (login.length<=8){

              }

                userInfoForRegister = RegisterReceiveRemote(
                    login = login,
                    email = email,
                    password = password
                )
                binding.progressBar2.visibility = View.VISIBLE
                binding.btnCreateAccount.isEnabled = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        mainApi.registerNewUser(userInfoForRegister)
                        replaceFragment(OtpScreenFragment(userInfoForRegister))
                    } catch (e: HttpException) {
                        if (e.code() == 409) {
                            withContext(Dispatchers.Main){
                                Toast.makeText(
                                    APP_ACTIVITY,
                                    "User with this email is created try some new ",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.progressBar2.visibility = View.GONE
                                binding.btnCreateAccount.isEnabled = true
                            }

                        }
                    }catch (e:Exception){
                        Log.d("MyTag", "RegisterFragment: ${e.message}")
                    }

                }
            }else{
                Toast.makeText(APP_ACTIVITY,"Fill all the fields",Toast.LENGTH_SHORT).show()
            }
        }
    }



}