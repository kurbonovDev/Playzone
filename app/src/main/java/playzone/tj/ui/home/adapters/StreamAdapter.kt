package playzone.tj.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.databinding.StreamItemBinding

class StreamAdapter:RecyclerView.Adapter<StreamAdapter.StreamViewHolder>() {
    class StreamViewHolder(val binding:StreamItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val binding = StreamItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StreamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {

    }

}