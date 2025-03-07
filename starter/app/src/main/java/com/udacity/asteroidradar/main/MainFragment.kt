package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utils.displaySnackbar

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.asteroids_list)

        setupRecyclerViewAdapter()
        setupObservers()

        setHasOptionsMenu(true)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupRecyclerViewAdapter() {
        adapter = MainAsteroidAdapter(MainAsteroidAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid) })
        binding.asteroidRecycler.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupObservers() {
        viewModel.asteroids.observe(viewLifecycleOwner, { asteroids ->
            if (asteroids != null) {
                adapter.submitList(asteroids)
            }
        })

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, { asteroid ->
            if (asteroid != null) {
                navigateToDetailFragment(asteroid)
                viewModel.doneNavigating()
            }
        })

        viewModel.displaySnackbarEvent.observe(viewLifecycleOwner, { displaySnackbarEvent ->
            if (displaySnackbarEvent) {
                displaySnackbar(
                    getString(R.string.problems_retrieving_online_data_displaying_local_data), requireView())
                viewModel.doneDisplayingSnackbar()
            }
        })
    }

    private fun navigateToDetailFragment(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view_week_asteroids_menu -> viewModel.onViewWeekAsteroidsClicked()
            R.id.view_today_asteroids_menu -> viewModel.onTodayAsteroidsClicked()
            R.id.view_saved_asteroids_menu -> viewModel.onSavedAsteroidsClicked()
        }
        return true
    }
}