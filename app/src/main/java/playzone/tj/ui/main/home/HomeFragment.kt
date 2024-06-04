package playzone.tj.ui.main.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.R
import playzone.tj.ui.main.home.adapter_home.EventAdapter
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.retrofit.models.user_genres.Genres
import playzone.tj.ui.main.home.adapter_home.GenreAdapter
import playzone.tj.ui.main.home.viewModels.HomeViewModel
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.STORAGE_KEY

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rcViewEvents: RecyclerView
    private lateinit var rcViewCategories: RecyclerView
    private var listGenresName = listOf<String>()
    private var listGenres = mutableListOf<Genres>()

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.MainViewModelFactory(requireContext())
    }


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
        Log.d("MyTag", "ViewModel UserData :${homeViewModel.userData.value}")
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit()?.putBoolean("isChosenGenre", true)?.apply()
        super.onViewCreated(view, savedInstanceState)
        initEventRcView()
        initToolbar()
        initGenreRcView()
        openSettings()
        showAllEvents()
        updateUI()
    }

    private fun showAllEvents() {
        binding.showAllText.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToPopularEventsFragment()
            findNavController().navigate(action)
        }
    }

    private fun openSettings() {
        binding.icSettings.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToUserInfoFragment()
            findNavController().navigate(action)
        }
    }

    private fun initToolbar() {
        homeViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.nameUser.text = it.username
                Glide.with(requireActivity())
                    .load(it.userImage)
                    .error(R.drawable.user)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageUser)
            }
        })
    }

    private fun test() {
        homeViewModel.eventData.observe(viewLifecycleOwner){

        }
    }

    private fun initEventRcView() {
        homeViewModel.eventData.observe(viewLifecycleOwner, Observer { events ->
            Log.d("MyTag", "ViewModel initEventRcView  ${events}")
            events?.let {

                rcViewEvents = binding.rcViewEvents
                rcViewEvents.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                if (it.eventData.isNotEmpty()) {
                    rcViewEvents.adapter = EventAdapter(it.eventData) {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToEventDetailFragment(it)
                        findNavController().navigate(action)
                    }

                }
            }
        })
    }

    private fun initGenreRcView() {
        homeViewModel.genreData.observe(viewLifecycleOwner, Observer { userGenres ->
            userGenres?.let {
                listGenresName = it.userGenres
                listGenres = listGenresName.mapNotNull { genreName ->
                    genreImageMap[genreName]?.let { imageResId ->
                        Genres(imageResId, genreName)
                    }
                }.toMutableList()
                rcViewCategories = binding.reViewCategory
                rcViewCategories.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                rcViewCategories.adapter = GenreAdapter(listGenres)
            }
        })
    }


    private fun updateUI() {
        binding.swiper.setOnRefreshListener {
            homeViewModel.clearData()
            homeViewModel.fetchUser()
            homeViewModel.fetchEvents()
            homeViewModel.fetchUserGenres()
            binding.swiper.isRefreshing = false

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listGenres.clear()
    }
}

