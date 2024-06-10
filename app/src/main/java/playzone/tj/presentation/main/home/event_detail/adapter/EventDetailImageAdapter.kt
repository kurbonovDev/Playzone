package playzone.tj.presentation.main.home.event_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import playzone.tj.R
import playzone.tj.databinding.EventDetailImageBinding

class EventDetailImageAdapter(private val list: List<String>) :
    RecyclerView.Adapter<EventDetailImageAdapter.EventDetailImageViewHolder>() {
    class EventDetailImageViewHolder(var binding: EventDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailImageViewHolder {
        val binding =
            EventDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventDetailImageViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EventDetailImageViewHolder, position: Int) {
        with(holder.binding){
            Glide.with(root)
                .load(list[position])
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(eventDetailImage)
        }
    }
}