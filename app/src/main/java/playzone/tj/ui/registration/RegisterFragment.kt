package playzone.tj.ui.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.databinding.FragmentRegisterBinding
import playzone.tj.retrofit.MainAPI
import playzone.tj.retrofit.models.registration.RegisterReceiveRemote
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.replaceFragment
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var userInfoForRegister: RegisterReceiveRemote
    @Inject
    lateinit var mainAPI: MainAPI
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
           createAcc()
        }
    }

    private fun createAcc(){
        val login = binding.login.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (login.isNotEmpty() and email.isNotEmpty() and password.isNotEmpty()) {

            userInfoForRegister = RegisterReceiveRemote(
                login = login,
                email = email,
                password = password
            )
            binding.progressBar2.visibility = View.VISIBLE
            binding.btnCreateAccount.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    mainAPI.registerNewUser(userInfoForRegister)
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
                    withContext(Dispatchers.Main){
                        binding.progressBar2.visibility = View.GONE
                        binding.btnCreateAccount.isEnabled = true
                    }
                }

            }
        }else{
            Toast.makeText(APP_ACTIVITY,"Fill all the fields",Toast.LENGTH_SHORT).show()
        }
    }



}