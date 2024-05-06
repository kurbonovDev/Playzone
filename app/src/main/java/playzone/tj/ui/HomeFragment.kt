package playzone.tj.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.R
import playzone.tj.adapters.EventAdapter
import playzone.tj.adapters.GenreAdapter
import playzone.tj.adapters.StreamAdapter
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventRequest
import playzone.tj.retrofit.models.events.EventResponse
import playzone.tj.retrofit.models.user_genres.Genres
import playzone.tj.retrofit.models.user_genres.UserGenresReceive
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.mainApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rcView: RecyclerView
    private lateinit var rcView2: RecyclerView
    private lateinit var rcView3: RecyclerView
    private var list = listOf<EventDTO>()
    private var listGenresName = listOf<String>()
    private var listGenres = mutableListOf<Genres>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                list = mainApi.fetchEvent(
                    headerValue = "bf8487ae-7d47-11ec-90d6-0242ac120003",
                    EventRequest("")
                )
                rcView = binding.rcViewEvents
                rcView.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                rcView.adapter = EventAdapter(list)
                val login = sharedPreferences.getString("login", "")

                listGenresName =
                    mainApi.fetchUserGenres(UserGenresReceive(login = login!!)).userGenres

                listGenresName.forEach {
                    when (it) {
                        "Puzzle" -> {
                            listGenres.add(
                                Genres(R.drawable.puzzle_image, it)
                            )
                        }

                        "Sport" -> {
                            listGenres.add(
                                Genres(R.drawable.sport_image, it)
                            )
                        }

                        "Strategy" -> {
                            listGenres.add(
                                Genres(R.drawable.strategy_image, it)
                            )
                        }

                        "Logic" -> {
                            listGenres.add(
                                Genres(R.drawable.logic_image, it)
                            )
                        }

                    }
                }
                rcView2 = binding.reViewCategory
                rcView2.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

                rcView2.adapter =GenreAdapter(listGenres)


            } catch (e: Exception) {
                Log.d("MyTag", "${e.message}")
            }
        }


    }


}