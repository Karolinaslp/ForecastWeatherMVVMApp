package com.learning.forecastweathermmvmapp.ui.weather.future.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.forecastweathermmvmapp.data.db.LocalDateConverter
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.databinding.FragmentFutureListWeatherBinding
import com.learning.forecastweathermmvmapp.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_future_list_weather.recyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.threeten.bp.LocalDate

class FutureListWeatherFragment : ScopedFragment() {

    private val viewModelFactory: FutureListWeatherViewModelFactory by inject()
    private lateinit var viewModel: FutureListWeatherViewModel


    private var _binding: FragmentFutureListWeatherBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance() = FutureListWeatherFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFutureListWeatherBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[FutureListWeatherViewModel::class.java]
        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.name)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer { weatherEntries ->
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
            FutureWeatherItem(it, { showToast() })
        }
    }

    @SuppressLint("ShowToast")
    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }
    }

    private fun showWeatherDetails(date: LocalDate, view: View) {
        val dateString = LocalDateConverter.dateToString(date)!!

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showToast(){
        Toast.makeText(this@FutureListWeatherFragment.context, "Clicked", Toast.LENGTH_SHORT).show()
    }
}