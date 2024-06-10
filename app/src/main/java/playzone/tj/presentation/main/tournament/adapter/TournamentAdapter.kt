package playzone.tj.presentation.main.tournament.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playzone.tj.databinding.TournamentItemBinding
import playzone.tj.presentation.main.tournament.model.TournamentDTO

class TournamentAdapter(private val listTournament:List<TournamentDTO>):RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {
    class TournamentViewHolder(val binding:TournamentItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val binding = TournamentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TournamentViewHolder(binding)
    }

    override fun getItemCount(): Int = listTournament.size

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        with(holder.binding){
            imageTeamFirst.setImageResource(listTournament[position].teamFirstImage)
            imageTeamSecond.setImageResource(listTournament[position].teamSecondImage)

            nameTeamFirst.text= listTournament[position].teamFirstName
            nameTeamSecond.text = listTournament[position].teamSecondName

            tvScoreFirstTeam.text = listTournament[position].teamFirstScore
            tvScoreSecondTeam.text = listTournament[position].teamSecondScore

            tvMoneyFirstTeam.text= listTournament[position].teamFirstMoney
            tvMoneySecondTeam.text=listTournament[position].teamSecondMoney
        }
    }
}