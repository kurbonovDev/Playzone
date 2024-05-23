package playzone.tj.ui.registration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.databinding.FragmentOtpScreenBinding
import playzone.tj.retrofit.models.registration.Otp
import playzone.tj.retrofit.models.registration.RegisterReceiveRemote
import playzone.tj.ui.registration.ChooseGenreFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.AppTextWatcher
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment


class OtpScreenFragment(private val registerReceiveRemote: RegisterReceiveRemote) : Fragment() {


    private lateinit var binding: FragmentOtpScreenBinding
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        binding = FragmentOtpScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer()


    }

    private fun timer() {
        val timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val second = millisUntilFinished / 1000

                binding.timeWaiting.text = "00:${String.format("%02d", second)}"
            }

            override fun onFinish() {
                binding.timeWaiting.text = "00:00"
            }
        }
        timer.start()
    }

    override fun onResume() {
        super.onResume()
        myKeyBoard()
        verificationWithOtpCode()
        sendAgain()

    }

    private fun sendOtpToEmail() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainApi.registerNewUser(registerReceiveRemote)
            } catch (e: Exception) {
                Log.d("MyTag", "RegisterFragment:${e.message}")
            }
        }
    }

    private fun sendAgain() {
        binding.sendCodeAgain.setOnClickListener {
            if (binding.timeWaiting.text == "00:00") {
                sendOtpToEmail()
                timer()
            } else {
                Toast.makeText(APP_ACTIVITY, "Wait until timer finished", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun verificationWithOtpCode() {
        binding.otpCode.addTextChangedListener(AppTextWatcher { text ->
            val otpCode = text.toString()
            val otp = Otp(
                email = registerReceiveRemote.email,
                password = registerReceiveRemote.password,
                otpCode = otpCode,
                login = registerReceiveRemote.login
            )

            if (otpCode.length == 4) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val token = mainApi.checkOtpCodeForRegister(otp = otp)

                        if (token != null) {
                            withContext(Dispatchers.Main) {
                                sharedPreferences?.edit()?.putBoolean("isRegistered", true)?.apply()
                                sharedPreferences?.edit()?.putString("login",registerReceiveRemote.login)?.apply()
                                sharedPreferences?.edit()?.putString("token",token.token)?.apply()
                                APP_ACTIVITY.supportFragmentManager.popBackStack(
                                    null,
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                )
                                replaceFragment(ChooseGenreFragment(), false)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    APP_ACTIVITY,
                                    "Incorrect OTP code",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MyTag", "Ошибка при отправке запроса: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                APP_ACTIVITY,
                                "Incorrect OTP code",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })

    }


    private fun myKeyBoard() {

        binding.one.setOnClickListener {
            binding.otpCode.append("1")
        }
        binding.two.setOnClickListener {
            binding.otpCode.append("2")
        }
        binding.three.setOnClickListener {
            binding.otpCode.append("3")
        }
        binding.four.setOnClickListener {
            binding.otpCode.append("4")
        }
        binding.five.setOnClickListener {
            binding.otpCode.append("5")
        }
        binding.six.setOnClickListener {
            binding.otpCode.append("6")
        }
        binding.seven.setOnClickListener {
            binding.otpCode.append("7")
        }
        binding.eight.setOnClickListener {
            binding.otpCode.append("8")
        }
        binding.nine.setOnClickListener {
            binding.otpCode.append("9")
        }
        binding.zero.setOnClickListener {
            binding.otpCode.append("0")
        }
        binding.clear.setOnClickListener {
            val currentText = binding.otpCode.text.toString()
            val currentLength = currentText.length
            if (currentLength > 0) {
                binding.otpCode.setText(currentText.substring(0, currentLength - 1))
            }
        }


    }


}