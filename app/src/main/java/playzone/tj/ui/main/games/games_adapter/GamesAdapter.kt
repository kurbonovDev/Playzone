package playzone.tj.ui.main.games.games_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import playzone.tj.R
import playzone.tj.databinding.GameItemBinding
import playzone.tj.retrofit.models.games.GameDTO

class GamesAdapter(private val gameList: List<GameDTO>,private val listener: (item: GameDTO) -> Unit)
    :RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {
    class GameViewHolder(val binding:GameItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GameViewHolder(binding)
    }

    override fun getItemCount(): Int =gameList.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        with(holder.binding){
            Glide.with(root)
                .load(gameList[position].logo)
                .error(R.drawable.ic_sword)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imGame)

            tvGameName.text = gameList[position].gameName
            tvRateGame.text = gameList[position].rateGame.toString()
            tvDownloadCount.text = gameList[position].downloadCount.toString()
            tvGameVersion.text = "Ver:${gameList[position].versionGame}"
            tvGameSize.text =gameList[position].sizeGame
            tvGameNumber.text = (position+4).toString()

            root.setOnClickListener {
                listener(gameList[position])
            }
        }

    }
}