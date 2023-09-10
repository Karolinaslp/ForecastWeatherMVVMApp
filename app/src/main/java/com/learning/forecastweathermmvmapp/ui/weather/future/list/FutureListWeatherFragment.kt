package com.learning.forecastweathermmvmapp.ui.weather.future.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.learning.forecastweathermmvmapp.R
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.databinding.FragmentFutureListWeatherBinding
import com.learning.forecastweathermmvmapp.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FutureListWeatherFragment : ScopedFragment() {
    private val viewmodelFactory: FutureListWeatherViewModelFactory by inject()
    private lateinit var viewModel: FutureListWeatherViewModel


    private var _binding: FragmentFutureListWeatherBinding? = null
    private val binding
        get() = _binding!!

//    companion object {
//        fun newInstance() = FutureListWeatherFragment()
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFutureListWeatherBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewmodelFactory)
            .get(FutureListWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureListWeatherFragment, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.name)
        })

        futureWeatherEntries.observe(this@FutureListWeatherFragment, Observer {weatherEntries ->
            if (weatherEntries == null) return@Observer

            binding.groupLoading.visibility = View.GONE

            updateDateToNextWeek()
            initRecyclerView(weatherEntries.toFutureWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }
    private fun initRecyclerView(item: List<FutureWeatherItem>) {
val groupAdapter = GroupAdapter<ViewHolder>().apply {
    addAll(items)
}
    }
}