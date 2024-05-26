package playzone.tj.ui.main.games

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
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
    private lateinit var sharedPreferences: SharedPreferences

    private var gameList = listOf<GameDTO>()
    private var token: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGamesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = APP_ACTIVITY.getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE)
        token=sharedPreferences.getString(TOKEN_KEY,"")
        getGames()
        binding.backHome.setOnClickListener {
            val action = GamesFragmentDirections.actionGamesFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun getGames() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                gameViewModel.fetchGames(token?:"")
                gameList = gameViewModel.gameData

                with(Dispatchers.Main){

                    val (topThree,remainGames) =filterGames(gameList)
                    initTopGames(topThree)
                    rcViewGame = binding.rcViewGame
                    rcViewGame.layoutManager = LinearLayoutManager(APP_ACTIVITY)
                    rcViewGame.adapter = GamesAdapter(remainGames)
                }

            }catch (e:Exception){
               with(Dispatchers.Main){
                   Toast.makeText(APP_ACTIVITY,e.message,Toast.LENGTH_SHORT)

               }
            }
        }
    }

    private fun initTopGames(list: List<GameDTO>){
        if (list.size==3)
        binding.tvNameTop1.text = list[0].gameName
        Glide.with(requireActivity())
            .load(list[0].image)
            .error(R.drawable.ic_sword)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imTop1)

        binding.tvNameTop2.text = list[1].gameName
        Glide.with(requireActivity())
            .load(list[1].image)
            .error(R.drawable.ic_sword)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imTop2)

        Glide.with(requireActivity())
            .load(list[2].image)
            .error(R.drawable.ic_sword)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imTop3)
    }

}