package playzone.tj.ui.main.home.event_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import playzone.tj.R
import playzone.tj.databinding.FragmentEventDetailBinding
import playzone.tj.retrofit.models.events.EventDTO
import playzone.tj.retrofit.models.events.EventImagesRequest
import playzone.tj.retrofit.models.games.GameDTO
import playzone.tj.ui.main.games.game_detail.GameDetailFragmentArgs
import playzone.tj.ui.main.home.event_detail.adapter.EventDetailImageAdapter
import playzone.tj.ui.main.home.viewModels.HomeViewModel
import playzone.tj.utils.mainApi

class EventDetailFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailBinding
    private lateinit var rcViewEventImages: RecyclerView
    private val args: EventDetailFragmentArgs by navArgs()
    private var event: EventDTO? = null
    private val eventDetailModel: EventDetailViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEventDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event = args.eventDTO
        initUI()
        binding.btnWatchEvent.setOnClickListener {
            val action =
                EventDetailFragmentDirections.actionEventDetailFragmentToYouTubeFragment(event!!.link)
            findNavController().navigate(action)
        }
    }


    private fun initUI() {

        if (event != null) {
            Glide.with(requireContext())
                .load(event?.imageMain)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.eventImage)
            binding.tvWatcherCount.text = event?.watcherCount.toString()
            binding.eventDetailName.text = event?.eventName
            binding.tvEventFormat.text = event?.format + " | 72/100 Prize | Pool"
            binding.tvEventDesc.text = event?.description

            viewLifecycleOwner.lifecycleScope.launch {
                if (eventDetailModel.eventData.isEmpty())
                eventDetailModel.fetchEventImages(event!!.eventId)
                with(Dispatchers.Main){
                    rcViewEventImages = binding.rcViewEventImages
                    rcViewEventImages.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    rcViewEventImages.adapter = EventDetailImageAdapter(eventDetailModel.eventData)

                }
            }


        }


    }


}