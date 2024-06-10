package playzone.tj.presentation.main.live_streams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.databinding.FragmentStreamBinding
import playzone.tj.presentation.main.live_streams.adapters.StreamAdapter
import playzone.tj.presentation.main.live_streams.model.StreamDTO

class StreamFragment : Fragment() {

    private lateinit var binding: FragmentStreamBinding
    private lateinit var rcView: RecyclerView
    private val list =
        listOf<StreamDTO>(
            StreamDTO("2ZPpmhQEvGs", 147),
            StreamDTO("iUwdyHcpUe8", 417),
            StreamDTO("OC1ggQt5Wms", 861),
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        binding.back.setOnClickListener {
            val action = StreamFragmentDirections.actionStreamFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }


    private fun initRcView() {
        rcView = binding.rcViewLiveStream
        rcView.layoutManager = LinearLayoutManager(requireContext())
        rcView.adapter = StreamAdapter(list){
            val action = StreamFragmentDirections.actionStreamFragmentToYouTubeFragment(it)
            findNavController().navigate(action)
        }
    }

}