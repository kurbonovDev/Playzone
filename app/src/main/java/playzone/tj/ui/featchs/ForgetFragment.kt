package playzone.tj.ui.featchs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.databinding.FragmentForgetBinding
import playzone.tj.retrofit.models.forget_password.ForgetPasswordRemote
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment


class ForgetFragment : Fragment() {


    private lateinit var binding: FragmentForgetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgetBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility=View.GONE
        binding.btnSendCodeToReset.isEnabled=true
        binding.btnSendCodeToReset.setOnClickListener {
            val email= binding.email.text.toString()
            if (email.isNotEmpty()){
                binding.progressBar.visibility=View.VISIBLE
                binding.btnSendCodeToReset.isEnabled=false


                viewLifecycleOwner.lifecycleScope.launch{
                    try {
                        mainApi.sendCheckCodeToEmail(
                            ForgetPasswordRemote(
                                email = email,
                                checkCode = "",
                                newPassword = ""
                            )
                        )

                        replaceFragment(ConfirmPasswordFragment(email))

                    } catch (e: Exception) {
                        Log.e("MyTag", "ConFirmPasswordFragment: ${e.message}")
                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility=View.GONE
                            binding.btnSendCodeToReset.isEnabled=true
                            Toast.makeText(
                                APP_ACTIVITY,
                                "User not found with this email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }else{
                Toast.makeText(APP_ACTIVITY,"Fill field email",Toast.LENGTH_SHORT).show()
            }
        }

    }

}