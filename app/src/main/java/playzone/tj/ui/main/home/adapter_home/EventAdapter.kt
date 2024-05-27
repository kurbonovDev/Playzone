package playzone.tj.ui.main.home.adapter_home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import playzone.tj.R
import playzone.tj.databinding.EventItemBinding
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.games.GameDTO

class EventAdapter(private val list: List<EventDTO>,private val listener: (item: EventDTO) -> Unit): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
     class EventViewHolder(val binding:EventItemBinding):RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = list[position]
        with(holder.binding){
            eventCreatorName.text=currentItem.creatorName
            eventName.text=currentItem.eventName
            eventTitle.text=currentItem.title

            Glide.with(holder.binding.root)
                .load(currentItem.imageCreator)
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(eventCreatorImage)

            Glide.with(holder.binding.root)
                .load(currentItem.imagePreview)
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(eventPreviewImage)

            root.setOnClickListener {
                listener(currentItem)
            }

        }
    }
}