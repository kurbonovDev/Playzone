package playzone.tj.ui.featchs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.databinding.FragmentConfirmPasswordBinding
import playzone.tj.retrofit.MainAPI
import playzone.tj.retrofit.models.forget_password.ForgetPasswordRemote
import playzone.tj.ui.login.LoginFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.replaceFragment
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmPasswordFragment(private val email: String) : Fragment() {

    @Inject
    lateinit var mainAPI: MainAPI
    private lateinit var binding: FragmentConfirmPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentConfirmPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()

        binding.btnConfirmNewPassword.setOnClickListener {
            confirmNewPassword()
        }

    }

    private fun confirmNewPassword() {
        val checkCode = binding.checkCode.text.toString()
        val newPassword = binding.newPassword.text.toString()
        val confirmNewPassword = binding.confirmNewPassword.text.toString()


        if (checkCode.isNotEmpty() and newPassword.isNotEmpty()) {

            if (newPassword == confirmNewPassword) {
                val forgetPasswordResponseRemote = ForgetPasswordRemote(
                    email = email,
                    checkCode,
                    newPassword = newPassword
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    try {

                        mainAPI.confirmNewPassword(forgetPasswordResponseRemote)
                        APP_ACTIVITY.supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        replaceFragment(LoginFragment(), false)
                        withContext(Dispatchers.Main){
                            Toast.makeText(APP_ACTIVITY,"Password updated successfully",Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                APP_ACTIVITY,
                                "Incorrect check code",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }else{
                Toast.makeText(APP_ACTIVITY,"Password are not same",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(APP_ACTIVITY,"Fill all the fields",Toast.LENGTH_SHORT).show()
        }
    }


}