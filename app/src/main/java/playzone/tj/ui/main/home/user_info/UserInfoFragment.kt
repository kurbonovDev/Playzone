package playzone.tj.ui.main.home.user_info

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.R
import playzone.tj.databinding.FragmentFillUserInfoBinding
import playzone.tj.retrofit.models.User
import playzone.tj.ui.main.home.HomeFragment
import playzone.tj.ui.main.home.HomeFragmentDirections
import playzone.tj.ui.main.home.viewModels.HomeViewModel
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.ExitDialog
import playzone.tj.utils.STORAGE_KEY
import playzone.tj.utils.mainApi
import playzone.tj.utils.replaceFragment


class UserInfoFragment() : Fragment() {

    private lateinit var binding: FragmentFillUserInfoBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences
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
        updateUser()


        binding.btnExit.setOnClickListener {
            ExitDialog.showDialog(requireContext(),object :ExitDialog.Listener{
                override fun onClick() {
                    sharedPreferences.edit().clear().apply()
                    val action  = UserInfoFragmentDirections.actionUserInfoFragmentToMainFragment()
                    findNavController().navigate(action)
                }
            })
        }
    }


    private fun initFields() {
        binding.userFullName.setText(homeViewModel.userData.username)
        binding.userPassword.setText(homeViewModel.userData.password)
        Glide.with(APP_ACTIVITY)
            .load(homeViewModel.userData.userImage)
            .error(R.drawable.user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.userImage)

        binding.tvUserFullName.text = homeViewModel.userData.username
        binding.tvUserEmail.text = homeViewModel.userData.email
        binding.tvUserLogin.text = homeViewModel.userData.login

    }

    private fun updateUser() {

        binding.updateBtn.setOnClickListener {
            binding.isUpdating.visibility = View.VISIBLE
            binding.updateBtn.isEnabled = false
            val user =User(
                homeViewModel.userData.login,
                binding.userPassword.text.toString(),
                homeViewModel.userData.email,
                binding.userFullName.text.toString(),
                homeViewModel.userData.userImage
            )
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    mainApi.updateUser(
                        user
                    )

                   val action = UserInfoFragmentDirections.actionUserInfoFragmentToHomeFragment()
                    findNavController().navigate(action)

                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        binding.isUpdating.visibility = View.GONE
                        binding.updateBtn.isEnabled = true
                        Toast.makeText(APP_ACTIVITY,e.message,Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

    }


}