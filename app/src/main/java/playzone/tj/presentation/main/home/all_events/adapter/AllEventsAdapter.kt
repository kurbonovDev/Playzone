package playzone.tj.presentation.main.home.all_events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import playzone.tj.R
import playzone.tj.databinding.AllEventItemBinding
import playzone.tj.data.remote.retrofit.models.events.EventDTO

class AllEventsAdapter(private var eventList: List<EventDTO>, private val listener: (item: EventDTO) -> Unit) :
    RecyclerView.Adapter<AllEventsAdapter.AllEventViewHolder>() {
    class AllEventViewHolder(var binding: AllEventItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllEventViewHolder {
        val binding =
            AllEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllEventViewHolder(binding)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: AllEventViewHolder, position: Int) {
        with(holder.binding){
            Glide.with(root)
                .load(eventList[position].imagePreview)
                .error(R.drawable.game_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(eventImage)

            eventName.text=eventList[position].eventName

            root.setOnClickListener {
                listener(eventList[position])
            }
        }
    }

    fun setFilterList(list: List<EventDTO>) {
        this.eventList = list
        notifyDataSetChanged()

    }
}