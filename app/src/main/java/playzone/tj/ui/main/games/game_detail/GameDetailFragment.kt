package playzone.tj.ui.main.games.game_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import playzone.tj.R
import playzone.tj.databinding.FragmentGameDetailBinding
import playzone.tj.retrofit.models.games.GameDTO
import playzone.tj.retrofit.models.games.GetGameGenre
import playzone.tj.ui.main.games.game_detail.adapters.GenreGameAdapter
import playzone.tj.utils.mainApi


class GameDetailFragment() : Fragment() {

    private lateinit var binding: FragmentGameDetailBinding
    private lateinit var rcViewGenres: RecyclerView
    private val args: GameDetailFragmentArgs by navArgs()
    private var game: GameDTO? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        game = args.gameDTO
        initRcViewAndUi()

    }

    private fun initRcViewAndUi() {
        if (game != null) {
            Glide.with(requireContext())
                .load(game?.image)
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.eventImage)

            Glide.with(requireContext())
                .load(game?.logo)
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.gameLogo)

            binding.gameName.text = game?.gameName
            binding.gameDesc.text = game?.description
            binding.tvRateGame.text = game?.rateGame.toString()
            binding.tvDownloadCount.text = game?.downloadCount.toString()

            Glide.with(requireContext())
                .load(game?.image)
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.someGameImage)



            viewLifecycleOwner.lifecycleScope.launch {
                val listGenres = mainApi.fetchGameGenre(GetGameGenre(game!!.gameID))
                with(Dispatchers.Main) {
                    rcViewGenres = binding.rcViewGenres
                    rcViewGenres.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rcViewGenres.adapter = GenreGameAdapter(listGenres)
                }
            }
        }

    }


}