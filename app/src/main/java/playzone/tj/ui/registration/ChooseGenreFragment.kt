package playzone.tj.ui.registration

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import playzone.tj.databinding.FragmentChooseGenreBinding
import playzone.tj.retrofit.MainAPI
import playzone.tj.retrofit.models.user_genres.UsersGenresRequest
import playzone.tj.ui.main.PointFragment
import playzone.tj.utils.APP_ACTIVITY
import playzone.tj.utils.replaceFragment
import javax.inject.Inject

@AndroidEntryPoint
class ChooseGenreFragment() : Fragment() {

    private lateinit var binding: FragmentChooseGenreBinding
    private var listGenres = mutableListOf<String>()
    private var sharedPreferences: SharedPreferences? = null
    @Inject
    lateinit var mainAPI: MainAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = APP_ACTIVITY.getSharedPreferences("my_storage", Context.MODE_PRIVATE)
        binding = FragmentChooseGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        imageListener()
        chooseGenre()

    }


    private fun imageListener() {
        binding.imageLogic.setOnClickListener {
            val logic = "Logic"
            if (listGenres.contains(logic)) {
                listGenres.remove(logic)
                binding.logicText.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                listGenres.add(logic)
                binding.logicText.setTextColor(Color.parseColor("#F4D144"))
            }
        }

        binding.imagePuzzle.setOnClickListener {
            val puzzle = "Puzzle"
            if (listGenres.contains(puzzle)) {
                listGenres.remove(puzzle)
                binding.puzzleText.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                listGenres.add(puzzle)
                binding.puzzleText.setTextColor(Color.parseColor("#F4D144"))
            }
        }
        binding.imageSport.setOnClickListener {
            val sport = "Sport"
            if (listGenres.contains(sport)) {
                listGenres.remove(sport)
                binding.sportText.setTextColor(Color.parseColor("#FFFFFF"))

            } else {
                listGenres.add(sport)
                binding.sportText.setTextColor(Color.parseColor("#F4D144"))

            }
        }
        binding.imageStrategy.setOnClickListener {
            val strategy = "Strategy"
            if (listGenres.contains(strategy)) {
                listGenres.remove(strategy)
                binding.strategyText.setTextColor(Color.parseColor("#FFFFFF"))

            } else {
                listGenres.add(strategy)
                binding.strategyText.setTextColor(Color.parseColor("#F4D144"))

            }
        }
    }

    private fun chooseGenre() {

        binding.btnChooseGenre.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {
                val login = sharedPreferences?.getString("login", "")
                if (login!!.isNotEmpty()) {
                    try {
                        withContext(Dispatchers.Main){
                            binding.btnChooseGenre.isEnabled = false
                        }
                        mainAPI.addGenresToUser(
                            UsersGenresRequest(
                                userLogin = login,
                                genreId = listGenres
                            )
                        )
                        sharedPreferences?.edit()?.putBoolean("isChosenGenre", true)?.apply()
                        replaceFragment(PointFragment(), false)
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.btnChooseGenre.isEnabled = true
                            Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }


}