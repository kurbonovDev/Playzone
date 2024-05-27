package playzone.tj.ui.main.games.game_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.databinding.GameGenreItemBinding

class GenreGameAdapter(private var list: List<String>):RecyclerView.Adapter<GenreGameAdapter.GenreGameViewHolder>() {

    class GenreGameViewHolder(var binding:GameGenreItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreGameViewHolder {
        val binding = GameGenreItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GenreGameViewHolder(binding)
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: GenreGameViewHolder, position: Int) {
        holder.binding.tvGenre.text = list[position]
    }
}