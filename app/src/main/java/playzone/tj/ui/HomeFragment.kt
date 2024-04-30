package playzone.tj.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import playzone.tj.R
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.utils.APP_ACTIVITY


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage",Context.MODE_PRIVATE)
        binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.button.setOnClickListener {
            sharedPreferences.edit().clear().apply()
        }
    }


}