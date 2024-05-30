package playzone.tj.ui.main.games

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import playzone.tj.R
import playzone.tj.databinding.FragmentGamesBinding
import playzone.tj.retrofit.models.games.GameDTO
import playzone.tj.ui.main.games.games_adapter.GamesAdapter
import playzone.tj.ui.main.games.viewModels.GameViewModel
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.STORAGE_KEY
import playzone.tj.utils.TOKEN_KEY
import playzone.tj.utils.filterGames

class GamesFragment : Fragment() {

    private lateinit var binding: FragmentGamesBinding
    private lateinit var rcViewGame: RecyclerView
    private val gameViewModel: GameViewModel by activityViewModels()
    private val gameFindViewModel: GameViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: GamesAdapter
    private var gameList = listOf<GameDTO>()
    private var token: String? = ""
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MyTag", "GamesFragment:onCreateView")
        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyTag", "GamesFragment:onViewCreated")
        sharedPreferences = APP_ACTIVITY.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(TOKEN_KEY, "")
        binding.searchView.setQuery("", false)
        getGames()
        findGame()
        binding.backHome.setOnClickListener {
            val action = GamesFragmentDirections.actionGamesFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        searchGame()
    }


    private fun getGames() {
        Log.d("MyTag", "GamesFragment:getGame")
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                gameViewModel.fetchGames(token ?: "")
                gameList = gameViewModel.gameData
                with(Dispatchers.Main) {
                    initRcView()
                }
            } catch (e: Exception) {
                with(Dispatchers.Main) {
                    Toast.makeText(APP_ACTIVITY, e.message, Toast.LENGTH_SHORT)

                }
            }
        }
    }

    private fun searchGame() {
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.mainBlock.visibility = View.GONE
                binding.rcViewGame.isVisible = false
                binding.root.setOnClickListener {
                    binding.mainBlock.visibility = View.VISIBLE
                    binding.rcViewGame.isVisible = true
                    binding.searchView.clearFocus()
                    val (topThree, remainGames) = filterGames(gameList)
                    initTopGames(topThree)
                    adapter.setFilterList(remainGames, false)
                    Log.d("MyTag", "GamesFragment:search_game")
                    binding.nothingFind.visibility = View.GONE
                    binding.nothingFindText.visibility = View.GONE
                }

            }
        }

    }

    private fun initRcView() {
        val (topThree, remainGames) = filterGames(gameList)
        initTopGames(topThree)
        adapter = GamesAdapter(remainGames, false) {
            val action =
                GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(it)
            findNavController().navigate(action)
        }
        rcViewGame = binding.rcViewGame
        rcViewGame.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        rcViewGame.adapter = adapter
        Log.d("MyTag", "GamesFragment:initRcView")
    }

    private fun initTopGames(list: List<GameDTO>) {
        Log.d("MyTag", "GamesFragment:initTopGames")

        if (list.size == 3) {
            binding.tvNameTop1.text = list[0].gameName
            Glide.with(requireActivity())
                .load(list[0].image)
                .error(R.drawable.ic_sword)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imTop1)
            binding.imTop1.setOnClickListener {
                val game = list[0]
                val action = GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(game)
                findNavController().navigate(action)
            }

            binding.tvNameTop2.text = list[1].gameName
            Glide.with(requireActivity())
                .load(list[1].image)
                .error(R.drawable.ic_sword)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imTop2)
            binding.imTop2.setOnClickListener {
                val game = list[1]
                val action = GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(game)
                findNavController().navigate(action)
            }

            Glide.with(requireActivity())
                .load(list[2].image)
                .error(R.drawable.ic_sword)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imTop3)

            binding.imTop3.setOnClickListener {
                val game = list[2]
                val action = GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(game)
                findNavController().navigate(action)
            }
        }

    }

    /* private fun filterList(query: String?) {

          if (query != null) {
              val filteredList = ArrayList<GameDTO>()
              for (i in gameViewModel.findData) {
                  if (i.gameName.contains(query, ignoreCase = true)) {
                      filteredList.add(i)
                  }
              }
              if (filteredList.isEmpty()) {
                  binding.rcViewGame.isVisible = false
                  binding.nothingFind.visibility = View.VISIBLE
                  binding.nothingFindText.visibility = View.VISIBLE
                  binding.nothingFindText.text = "Sorry we couldn’t find any game for “$query”"
                  Log.d("MyTag","GamesFragment:filteredList.empty")
              } else {
                  binding.rcViewGame.isVisible = true
                  binding.nothingFind.visibility = View.GONE
                  binding.nothingFindText.visibility = View.GONE
                  val (topThree, remainGames) = filterGames(filteredList)
                  adapter.setFilterList(filteredList,true)
                  Log.d("MyTag","GamesFragment:filteredList.not_empty")
              }
          }
      }*/


    private fun filterList(list: List<GameDTO>, query: String?) {
        if (list.isEmpty()) {
            binding.rcViewGame.isVisible = false
            binding.nothingFind.visibility = View.VISIBLE
            binding.nothingFindText.visibility = View.VISIBLE
            binding.nothingFindText.text = "Sorry we couldn’t find any game:$query"
            Log.d("MyTag", "GamesFragment:filteredList.empty")
        } else {
            binding.rcViewGame.isVisible = true
            binding.nothingFind.visibility = View.GONE
            binding.nothingFindText.visibility = View.GONE
            adapter.setFilterList(list, true)
            Log.d("MyTag", "GamesFragment:filteredList.not_empty")
        }

    }

    private fun findGame() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MyTag", "GamesFragment:onQueryTextSubmit")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MyTag", "GamesFragment:onQueryTextChange")
                binding.mainBlock.visibility = View.GONE
                debounceSearch(newText)
                return true
            }
        })


    }


    private fun debounceSearch(query: String?) {
      if(query==""){
          searchJob?.cancel()
          filterList(emptyList(),"")
      }else if (query != null) {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500) // Задержка для debounce (500 мс)
                gameFindViewModel.findGames(token!!, query)
                filterList(gameFindViewModel.findData, query)
            }
        } else {
            filterList(emptyList(), "")
        }
    }
}