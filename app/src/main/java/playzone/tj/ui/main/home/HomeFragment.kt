package playzone.tj.ui.main.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.launch
import playzone.tj.R
import playzone.tj.ui.main.home.adapter_home.EventAdapter
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.retrofit.models.User
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.user_genres.Genres
import playzone.tj.ui.main.home.adapter_home.GenreAdapter
import playzone.tj.ui.main.home.viewModels.EventUIState
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
        Log.d("MyTag", "ViewModel UserData :${homeViewModel.userUIState.value}")
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit()?.putBoolean("isChosenGenre", true)?.apply()
        super.onViewCreated(view, savedInstanceState)
        openSettings()
        showAllEvents()
        updateUI()
        UiState()
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

    private fun initUser(user: User?) {
        user?.let {
            binding.nameUser.text = it.username
            Glide.with(requireActivity())
                .load(it.userImage)
                .error(R.drawable.user)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageUser)
        }
    }

    private fun setUserError(errorMessage: String?) {
        if (!errorMessage.isNullOrEmpty())
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun isUserLoading(loading: Boolean) {
        binding.userProgressbar.isVisible = loading
    }

    private fun UiState() {
       /* homeViewModel.eventData.observe(viewLifecycleOwner) {
            initEventRcView(it.eventData)
            setEventError(it.errorMessage)
            isEventLoading(it.isLoading)

        }*/

        homeViewModel.userUIState.observe(viewLifecycleOwner) {
            initUser(it.user)
            setUserError(it.errorMessage)
            isUserLoading(it.isLoading)
        }

        homeViewModel.genreUIState.observe(viewLifecycleOwner){
            initRcViewUserGenres(it.userGenresResponse.userGenres)
            setUserGenreError(it.errorMessage)
            isUserGenreLoading(it.isLoading)

        }


        /*viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.eventUIState.collect { state ->
                    initEventRcView(state.eventData)
                    setEventError(state.errorMessage)
                    isEventLoading(state.isLoading)
                }
            }
        }*/

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.eventUIState.collect { state ->
                    when (state) {
                        is EventUIState.Loading -> showLoading()
                        is EventUIState.Success -> showSuccess(state.eventData)
                        is EventUIState.Error -> showError(state.errorMessage)
                        is EventUIState.Empty -> showEmpty()
                    }
                }
            }
        }

    }

    private fun isUserGenreLoading(loading: Boolean) {
        binding.genreProgressbar.isVisible = loading
    }

    private fun setUserGenreError(errorMessage: String?) {
        if (!errorMessage.isNullOrEmpty())
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun initRcViewUserGenres(userGenres: List<String>?) {
        userGenres?.let {
            listGenresName = it
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
    }




    private fun initEventRcView(list: List<EventDTO>) {
        list.let {
            rcViewEvents = binding.rcViewEvents
            rcViewEvents.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            if (it.isNotEmpty()) {
                rcViewEvents.adapter = EventAdapter(it) {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToEventDetailFragment(it)
                    findNavController().navigate(action)
                }
            }
        }
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


    private fun showLoading() {
        binding.apply {
            eventProgressbar.isVisible = true
            rcViewEvents.isVisible = false
            eventsImageError.isVisible = false
            eventsTextError.isVisible = false
            showAllText.isVisible = false
            popularEventText.isVisible = false
        }
    }

    private fun showSuccess(eventData: List<EventDTO>) {
        binding.apply {
            eventProgressbar.isVisible = false
            rcViewEvents.isVisible = true
            initEventRcView(eventData)
            eventsImageError.isVisible = false
            eventsTextError.isVisible = false
            showAllText.isVisible = true
            popularEventText.isVisible = true
        }
    }

    private fun showError(errorMessage: String?) {
        binding.apply {
            eventProgressbar.isVisible = false
            rcViewEvents.isVisible = false
            eventsTextError.text = errorMessage
            eventsTextError.isVisible = true
            eventsImageError.isVisible = true
            showAllText.isVisible = false
            popularEventText.isVisible = false
        }
    }

    private fun showEmpty() {
        binding.apply {
            eventProgressbar.isVisible = false
            rcViewEvents.isVisible = false
            eventsTextError.text = "No such data"
            eventsTextError.isVisible = true
            eventsImageError.isVisible = false
        }
    }

}

