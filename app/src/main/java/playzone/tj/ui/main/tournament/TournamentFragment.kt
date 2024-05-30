package playzone.tj.ui.main.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.R
import playzone.tj.databinding.FragmentTournamentBinding
import playzone.tj.ui.main.tournament.adapter.TournamentAdapter
import playzone.tj.ui.main.tournament.model.TournamentDTO


class TournamentFragment : Fragment() {

    private lateinit var rcViewTournament:RecyclerView
    private lateinit var binding:FragmentTournamentBinding

    private val listTournament = listOf<TournamentDTO>(
        TournamentDTO(R.drawable.team1,R.drawable.team2,"Evil Geniuses","Team Secret","12 / 6 / 10",
            "10 / 8 / 8","49K","39K","4.2K"),
        TournamentDTO(R.drawable.team2,R.drawable.team4,"Fnatic","Virtus.pro","12 / 6 / 10",
            "10 / 8 / 8","37K","38K","3.2K"),
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTournamentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        binding.back.setOnClickListener {
            val action = TournamentFragmentDirections.actionTournamentFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }


    private fun initRcView(){
        rcViewTournament = binding.rcViewTournament
        rcViewTournament.layoutManager = LinearLayoutManager(requireContext())
        rcViewTournament.adapter  = TournamentAdapter(listTournament)
    }
}