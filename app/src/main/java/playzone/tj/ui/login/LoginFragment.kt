package playzone.tj.ui.login

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
import playzone.tj.databinding.FragmentLoginBinding
import playzone.tj.retrofit.models.login.LoginReceiveRemote
import playzone.tj.ui.featchs.ForgetFragment
import playzone.tj.ui.home.HomeFragment
import playzone.tj.ui.registration.RegisterFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment


class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.dontHaveAcc.setOnClickListener {
            replaceFragment(RegisterFragment())
        }
        binding.btnLoginNow.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                login()
            }
        }

        binding.forgotPassword.setOnClickListener {
            replaceFragment(ForgetFragment())
        }

    }


    private suspend fun login() {
        val login = binding.login.text.toString()
        val password = binding.password.text.toString()

        if (login.isNotEmpty() and password.isNotEmpty()) {
            try {
                val token = mainApi.login(LoginReceiveRemote(login, password))
                if (token != null) {
                    sharedPreferences.edit()?.putBoolean("isRegistered",true)?.apply()
                    sharedPreferences.edit()?.putString("token",token.token)?.apply()
                    sharedPreferences.edit()?.putString("login",login)?.apply()

                    replaceFragment(HomeFragment(), false)
                }
            }catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(APP_ACTIVITY, "Incorrect login or password", Toast.LENGTH_SHORT).show()
                    Log.d("MyTag","${e.message}")
                }

            }
        }else{
            withContext(Dispatchers.Main){

                Toast.makeText(APP_ACTIVITY,"Fill login and password",Toast.LENGTH_SHORT).show()
            }
        }
    }


}