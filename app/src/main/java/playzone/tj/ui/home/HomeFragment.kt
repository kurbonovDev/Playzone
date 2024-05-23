package playzone.tj.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.R
import playzone.tj.ui.home.adapters.EventAdapter
import playzone.tj.ui.home.adapters.GenreAdapter
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.user_genres.Genres
import playzone.tj.ui.home.viewModels.HomeViewModel
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.replaceFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rcViewEvents: RecyclerView
    private lateinit var rcViewCategories: RecyclerView
    private var listEvents = listOf<EventDTO>()
    private var listGenresName = listOf<String>()
    private var listGenres = mutableListOf<Genres>()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var login: String? = null
    private var token: String? = null

    private val genreImageMap = mapOf(
        "Puzzle" to R.drawable.puzzle_image,
        "Sport" to R.drawable.sport_image,
        "Strategy" to R.drawable.strategy_image,
        "Logic" to R.drawable.logic_image
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        sharedPreferences.edit()?.putBoolean("isChosenGenre", true)?.apply()
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        token = sharedPreferences.getString("token", "")
        login = sharedPreferences.getString("login", "")
        initEventRcView()
        initToolbar()
        initGenreRcView()
        openSettings()
        showAllEvents()
        updateUI()
        return binding.root
    }

    private fun showAllEvents() {
        binding.showAllText.setOnClickListener {
            replaceFragment(PopularEventsFragment())
        }
    }

    private fun openSettings() {
        binding.icSettings.setOnClickListener {
            replaceFragment(UserInfoFragment())
        }
    }

    private fun initToolbar() {
        viewLifecycleOwner.lifecycleScope.launch {
            val login = sharedPreferences.getString("login", "")
            homeViewModel.fetchUser(login!!)
            withContext(Dispatchers.Main) {
                binding.nameUser.text = homeViewModel.userData?.username
                Glide.with(APP_ACTIVITY)
                    .load(homeViewModel.userData?.userImage)
                    .error(R.drawable.user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageUser)
            }
        }
    }

    private fun initEventRcView() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (homeViewModel.eventData.isEmpty()) {
                homeViewModel.fetchEvents(token ?: "")
            }
            try {
                listEvents = homeViewModel.eventData
                withContext(Dispatchers.Main) {
                    rcViewEvents = binding.rcViewEvents
                    rcViewEvents.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    rcViewEvents.adapter = EventAdapter(listEvents)
                }

            } catch (e: Exception) {
                Log.d("MyHomeTag", "${e.message}")
            }
        }
    }

    private fun initGenreRcView() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                homeViewModel.fetchUserGenres(login ?: "")
                listGenresName = homeViewModel.genreData.userGenres
                listGenres = listGenresName.mapNotNull { genreName ->
                    genreImageMap[genreName]?.let { imageResId ->
                        Genres(imageResId, genreName)
                    }
                }.toMutableList()
                withContext(Dispatchers.Main) {
                    rcViewCategories = binding.reViewCategory
                    rcViewCategories.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    rcViewCategories.adapter = GenreAdapter(listGenres)
                }


            } catch (e: Exception) {
                Log.d("MyHometag", "${e.message}")
            }
        }
    }

    private fun updateUI() {
        binding.swiper.setOnRefreshListener {
            homeViewModel.clearData()
            viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.fetchUser(login ?: "")
                homeViewModel.fetchEvents(token ?: "")
                homeViewModel.fetchUserGenres(login ?: "")
                withContext(Dispatchers.Main) {
                    binding.swiper.isRefreshing = false
                    initToolbar()
                    initEventRcView()
                    initGenreRcView()
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        listGenres.clear()
    }
}
