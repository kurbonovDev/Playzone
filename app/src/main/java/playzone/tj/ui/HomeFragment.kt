package playzone.tj.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.mainApi


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var list = listOf<EventDTO>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage",Context.MODE_PRIVATE)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                list= mainApi.fetchEvent("bf8487ae-7d47-11ec-90d6-0242ac120003",EventRequest("")).result
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()


    }


}