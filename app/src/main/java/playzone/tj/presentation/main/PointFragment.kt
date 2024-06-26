package playzone.tj.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import playzone.tj.R
import playzone.tj.databinding.FragmentPointBinding

class PointFragment : Fragment() {

    private lateinit var binding: FragmentPointBinding
    private lateinit var navController: NavController
    private val topIdSet = setOf(
        R.id.homeFragment,
        R.id.gamesFragment,
        R.id.streamFragment,
        R.id.tournamentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPointBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomBar()
    }


    private fun initBottomBar() {
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.menuNbar.isVisible = topIdSet.contains(destination.id)
        }
        NavigationUI.setupWithNavController(binding.menuNbar, navController)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            when (navController.currentDestination?.id) {
                R.id.userInfoFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                }

                R.id.popularEventsFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                }

                R.id.gameDetailFragment -> {
                    navController.popBackStack(R.id.gamesFragment, false)
                }

                R.id.eventDetailFragment -> {
                    navController.popBackStack()
                }

                R.id.youTubeFragment -> {
                  navController.popBackStack()
                }
                else -> {
                    requireActivity().finish()
                }
            }

        }

    }
}