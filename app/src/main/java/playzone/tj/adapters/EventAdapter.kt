package playzone.tj.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import playzone.tj.R
import playzone.tj.databinding.EventItemBinding
import playzone.tj.retrofit.models.events.EventDTO

class EventAdapter(private val list: List<EventDTO>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
     class EventViewHolder(val binding:EventItemBinding):RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = list[position]
        with(holder.binding){
            eventCreatorName.text=currentItem.creatorName
            eventName.text=currentItem.eventName
            eventTitle.text=currentItem.title

            Glide.with(holder.binding.root)
                .load(currentItem.imageCreator)
                .error(R.drawable.game_photo)
                .into(eventCreatorImage)

            Glide.with(holder.binding.root)
                .load(currentItem.imagePreview)
                .error(R.drawable.game_photo)
                .into(eventPreviewImage)


        }
    }
}