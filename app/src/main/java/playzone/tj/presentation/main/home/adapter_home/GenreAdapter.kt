package playzone.tj.presentation.main.home.adapter_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.databinding.GenreItemBinding
import playzone.tj.data.remote.retrofit.models.user_genres.Genres

class GenreAdapter(private var list: MutableList<Genres>):RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    class GenreViewHolder(var binding:GenreItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = GenreItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GenreViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return list.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        with(holder.binding){
            categoryImage.setImageResource(list[position].image)
            categoryName.text=list[position].genreName
        }
    }
}