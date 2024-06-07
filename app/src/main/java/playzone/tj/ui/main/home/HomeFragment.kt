package playzone.tj.ui.main.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import playzone.tj.R
import playzone.tj.databinding.FragmentHomeBinding
import playzone.tj.retrofit.models.User
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.user_genres.Genres
import playzone.tj.ui.main.home.adapter_home.EventAdapter
import playzone.tj.ui.main.home.adapter_home.GenreAdapter
import playzone.tj.ui.main.home.viewModels.EventUIState
import playzone.tj.ui.main.home.viewModels.HomeViewModel
import playzone.tj.ui.main.home.viewModels.UserGenreUIState
import playzone.tj.ui.main.home.viewModels.UserUIState
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.LOGIN_KEY
import playzone.tj.utils.STORAGE_KEY
import playzone.tj.utils.TOKEN_KEY
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rcViewEvents: RecyclerView
    private lateinit var rcViewCategory: RecyclerView
    private var listGenresName = listOf<String>()
    private var listGenres = mutableListOf<Genres>()
    private var token: String? = null
    private var login: String? = null
    private val genreImageMap = mapOf(
        "Puzzle" to R.drawable.puzzle_image,
        "Sport" to R.drawable.sport_image,
        "Strategy" to R.drawable.strategy_image,
        "Logic" to R.drawable.logic_image
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit()?.putBoolean("isChosenGenre", true)?.apply()
        token = sharedPreferences.getString(TOKEN_KEY, "")
        login = sharedPreferences.getString(LOGIN_KEY, "")
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        homeViewModel.fetchUser(login = login)
        homeViewModel.fetchEvents(token = token)
        homeViewModel.fetchUserGenres(login = login)
        openSettings()
        showAllEvents()
        updateUI()
        uiState()
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

    private fun uiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.eventUIState.collectLatest { state ->
                    binding.apply {
                        mainProgressbar.isVisible = state is EventUIState.Loading
                        rcViewEvents.isVisible = state is EventUIState.Success
                        eventsImageError.isVisible =
                            (state is EventUIState.Error || state is EventUIState.Empty)
                        eventsTextError.isVisible =
                            (state is EventUIState.Error || state is EventUIState.Empty)
                        showAllText.isVisible = state is EventUIState.Success
                        popularEventText.isVisible = state is EventUIState.Success
                    }

                    when (state) {
                        is EventUIState.Loading -> Unit
                        is EventUIState.Success -> initEventRcView(state.eventData)
                        is EventUIState.Error -> binding.eventsTextError.text = state.errorMessage
                        is EventUIState.Empty -> binding.eventsTextError.text = "No such data"
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.genreUIState.collect { state ->
                    binding.apply {
                        mainProgressbar.isVisible = state is UserGenreUIState.Loading
                        rcViewCategory.isVisible = state is UserGenreUIState.Success
                        genreTextError.isVisible =
                            (state is UserGenreUIState.Error || state is UserGenreUIState.Empty)
                        genreImageError.isVisible =
                            (state is UserGenreUIState.Error || state is UserGenreUIState.Empty)

                    }
                    when (state) {
                        is UserGenreUIState.Loading -> Unit
                        is UserGenreUIState.Success -> initRcViewUserGenres(state.userGenreData.userGenres)
                        is UserGenreUIState.Error -> binding.genreTextError.text =
                            state.errorMessage

                        is UserGenreUIState.Empty -> binding.genreTextError.text = "No such data"
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.userUIState.collect { state ->
                    binding.mainProgressbar.isVisible = state is UserUIState.Loading
                    when (state) {
                        is UserUIState.Loading -> Unit
                        is UserUIState.Success -> initUser(state.userData)
                        is UserUIState.Error -> Toast.makeText(
                            requireContext(), state.errorMessage, Toast.LENGTH_SHORT
                        ).show()

                        is UserUIState.Empty -> Unit
                    }
                }
            }
        }

    }

    private fun initEventRcView(list: List<EventDTO>) {
        list.let {
            rcViewEvents = binding.rcViewEvents
            rcViewEvents.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rcViewEvents.adapter = EventAdapter(it) {
                val action = HomeFragmentDirections.actionHomeFragmentToEventDetailFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun initRcViewUserGenres(userGenres: List<String>?) {
        userGenres?.let {
            listGenresName = it
            listGenres = listGenresName.mapNotNull { genreName ->
                genreImageMap[genreName]?.let { imageResId ->
                    Genres(imageResId, genreName)
                }
            }.toMutableList()
            rcViewCategory = binding.rcViewCategory
            rcViewCategory.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rcViewCategory.adapter = GenreAdapter(listGenres)
        }
    }

    private fun initUser(user: User?) {
        user?.let {
            binding.nameUser.text = it.username
            Glide.with(requireActivity()).load(it.userImage).error(R.drawable.user)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageUser)
        }
    }

    private fun updateUI() {
        binding.swiper.setOnRefreshListener {
            homeViewModel.clearData()
            homeViewModel.fetchUser(login)
            homeViewModel.fetchEvents(token)
            homeViewModel.fetchUserGenres(login)
            binding.swiper.isRefreshing = false

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listGenres.clear()
    }

}

