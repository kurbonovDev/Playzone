package playzone.tj.presentation.main.home.event_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import playzone.tj.R
import playzone.tj.databinding.FragmentEventDetailBinding
import playzone.tj.data.remote.retrofit.models.events.EventDTO
import playzone.tj.domain.models.DomainEventDTO
import playzone.tj.presentation.main.home.event_detail.adapter.EventDetailImageAdapter
import playzone.tj.utils.APP_ACTIVITY

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailBinding
    private lateinit var rcViewEventImages: RecyclerView
    private val args: EventDetailFragmentArgs by navArgs()
    private var event: DomainEventDTO? = null
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
        event = args.domainEventDTO
        initUI()
        binding.btnWatchEvent.setOnClickListener {
            val action =
                EventDetailFragmentDirections.actionEventDetailFragmentToYouTubeFragment(event!!.link)
            findNavController().navigate(action)
        }
        binding.btnShare.setOnClickListener {
            shareContent()
        }
        addLike()

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
                with(Dispatchers.Main) {
                    rcViewEventImages = binding.rcViewEventImages
                    rcViewEventImages.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rcViewEventImages.adapter = EventDetailImageAdapter(eventDetailModel.eventData)

                }
            }


        }


    }
    private fun shareContent() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Это пример текста для отправки.")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Поделиться с помощью"))
    }

    private fun addLike(){
        var isLike = false
        binding.btnLike.setOnClickListener {
            if (isLike){
                binding.tvLike.setTextColor(resources.getColor(R.color.white))
                binding.imLike.setColorFilter(ContextCompat.getColor(context?: APP_ACTIVITY,R.color.white))
            }else{
                binding.tvLike.setTextColor(resources.getColor(R.color.red))
                binding.imLike.setColorFilter(ContextCompat.getColor(context?: APP_ACTIVITY,R.color.red))
            }
            isLike=!isLike

        }

    }

}