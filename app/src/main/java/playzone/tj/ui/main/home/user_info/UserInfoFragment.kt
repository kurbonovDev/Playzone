package playzone.tj.ui.main.home.user_info

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.R
import playzone.tj.databinding.FragmentFillUserInfoBinding
import playzone.tj.retrofit.MainAPI
import playzone.tj.retrofit.models.User
import playzone.tj.ui.main.home.viewModels.HomeViewModel
import playzone.tj.ui.main.home.viewModels.UserUIState
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.ExitDialog
import playzone.tj.utils.STORAGE_KEY
import javax.inject.Inject

@AndroidEntryPoint
class UserInfoFragment() : Fragment() {

    private lateinit var binding: FragmentFillUserInfoBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var mainAPI: MainAPI
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFillUserInfoBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = APP_ACTIVITY.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        initFields()
        binding.btnExit.setOnClickListener {
            ExitDialog.showDialog(requireContext(), object : ExitDialog.Listener {
                override fun onClick() {
                    homeViewModel.clearData()
                    sharedPreferences.edit().clear().apply()
                    val action = UserInfoFragmentDirections.actionUserInfoFragmentToMainFragment()
                    findNavController().navigate(action)
                }
            })
        }
    }


    private fun initFields() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.userUIState.collectLatest { state ->
                    when (state) {
                        is UserUIState.Loading -> Unit
                        is UserUIState.Success -> {
                            initUser(state.userData)
                            updateUser(state.userData)
                        }

                        is UserUIState.Error -> Toast.makeText(
                            requireContext(),
                            state.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()

                        is UserUIState.Empty -> Unit
                    }
                }
            }
        }
    }

    private fun updateUser(user: User) {
        binding.updateBtn.setOnClickListener {
            binding.isUpdating.visibility = View.VISIBLE
            binding.updateBtn.isEnabled = false
            val newUser = User(
                login = user.login,
                password = binding.userPassword.text.toString(),
                email = user.email,
                username = binding.userFullName.text.toString(),
                userImage = user.userImage
            )
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    mainAPI.updateUser(
                        newUser
                    )
                    homeViewModel.userUIState.value
                    val action = UserInfoFragmentDirections.actionUserInfoFragmentToHomeFragment()
                    findNavController().navigate(action)

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.isUpdating.visibility = View.GONE
                        binding.updateBtn.isEnabled = true
                        Toast.makeText(APP_ACTIVITY, e.message, Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

    }

    private fun initUser(userData: User) {
        binding.userFullName.setText(userData.username)
        binding.userPassword.setText(userData.password)
        Glide.with(APP_ACTIVITY)
            .load(userData.userImage)
            .error(R.drawable.user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.userImage)

        binding.tvUserFullName.text = userData.username
        binding.tvUserEmail.text = userData.email
        binding.tvUserLogin.text = userData.login
    }


}