package playzone.tj.presentation.main.home.all_events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.R
import playzone.tj.databinding.FragmentPopularEventsBinding
import playzone.tj.data.remote.retrofit.models.events.EventDTO
import playzone.tj.presentation.main.home.all_events.adapter.AllEventsAdapter
import playzone.tj.presentation.main.home.viewModels.EventUIState
import playzone.tj.presentation.main.home.viewModels.HomeViewModel
import playzone.tj.utils.APP_ACTIVITY


class PopularEventsFragment() : Fragment() {


    private lateinit var binding: FragmentPopularEventsBinding
    private lateinit var rcView: RecyclerView
    private lateinit var adapter: AllEventsAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private var eventData: List<EventDTO> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularEventsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateSearchView()
        findEvent()
        binding.back.setOnClickListener {
            val action = PopularEventsFragmentDirections.actionPopularEventsFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        when (viewModel.eventUIState.value) {
            is EventUIState.Loading -> {}
            is EventUIState.Success -> {
                eventData = (viewModel.eventUIState.value as EventUIState.Success).eventData
                initRcView(eventData)
            }

            is EventUIState.Error -> {}
            is EventUIState.Empty -> {}
        }

    }

    private fun initRcView(eventData: List<EventDTO>) {
        rcView = binding.rcViewEvents
        rcView.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        Log.d("MyTag", "PopularFragment:initRcView")
        adapter = AllEventsAdapter(eventData) {
            val action =
                PopularEventsFragmentDirections.actionPopularEventsFragmentToEventDetailFragment(it)
            findNavController().navigate(action)
        }
        rcView.adapter = adapter


    }

    private fun animateSearchView() {
        binding.find.setOnClickListener {
            val slideRightToLeft = AnimationUtils.loadAnimation(
                binding.searchView.context,
                R.anim.slide_searchview_visible
            )
            binding.back.visibility = View.GONE
            binding.popularEventText.visibility = View.GONE
            binding.find.visibility = View.GONE
            binding.back.startAnimation(slideRightToLeft)
            binding.popularEventText.startAnimation(slideRightToLeft)
            binding.find.startAnimation(slideRightToLeft)
            binding.searchView.visibility = View.VISIBLE
            binding.tvCancel.visibility = View.VISIBLE
            binding.searchView.startAnimation(slideRightToLeft)
            binding.tvCancel.startAnimation(slideRightToLeft)

            binding.rcViewEvents.isVisible = false
            Log.d("MyTag", "PopularFragment:filteredList.binding.find.setOnClickListener")

        }

        binding.tvCancel.setOnClickListener {
            val slideLeftToRight = AnimationUtils.loadAnimation(
                binding.searchView.context,
                R.anim.slide_searchview_gone
            )

            binding.searchView.visibility = View.GONE
            binding.tvCancel.visibility = View.GONE
            binding.back.visibility = View.VISIBLE
            binding.find.visibility = View.VISIBLE
            binding.popularEventText.visibility = View.VISIBLE
            binding.searchView.startAnimation(slideLeftToRight)
            binding.tvCancel.startAnimation(slideLeftToRight)
            binding.find.startAnimation(slideLeftToRight)
            binding.popularEventText.startAnimation(slideLeftToRight)
            binding.back.startAnimation(slideLeftToRight)
            binding.nothingFind.visibility = View.GONE
            binding.nothingFindText.visibility = View.GONE
            binding.rcViewEvents.isVisible = true

            rcView.adapter = AllEventsAdapter(eventData) {
                val action =
                    PopularEventsFragmentDirections.actionPopularEventsFragmentToEventDetailFragment(
                        it
                    )
                findNavController().navigate(action)
            }
            Log.d("MyTag", "PopularFragment:filteredList.binding.tvCancel.setOnClickListener")
        }
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<EventDTO>()
            for (i in eventData) {
                if (i.eventName.contains(query, ignoreCase = true)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                binding.rcViewEvents.isVisible = false
                binding.nothingFind.visibility = View.VISIBLE
                binding.nothingFindText.visibility = View.VISIBLE
                binding.nothingFindText.text = "Sorry we couldn’t find any matches for “$query”"
                adapter.setFilterList(filteredList)
                Log.d("MyTag", "PopularFragment:filteredList.isEmpty()")
            } else {
                binding.rcViewEvents.isVisible = true
                binding.rcViewEvents.visibility = View.VISIBLE
                binding.nothingFind.visibility = View.GONE
                binding.nothingFindText.visibility = View.GONE
                adapter.setFilterList(filteredList)
                Log.d("MyTag", "PopularFragment:filteredList.not_Empty()")
                Log.d("MyTag", "PopularFragment:${filteredList}")
                Log.d("MyTag", "PopularFragment:${binding.rcViewEvents.isVisible}")
            }
        }
    }

    private fun findEvent() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MyTag", "PopularFragment:onQueryTextSubmit")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MyTag", "PopularFragment:onQueryTextChange")
                if (newText?.length == 0) binding.rcViewEvents.isVisible = false
                filterList(newText)
                return true
            }
        })
    }


}