package com.learning.forecastweathermmvmapp.ui.weather.future.list

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.forecastweathermmvmapp.R
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
    private lateinit var navController: NavController
    private val UNIT_SYSTEM_KEY = "UNIT_SYSTEM"

    private var _binding: FragmentFutureListWeatherBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance() = FutureListWeatherFragment()
    }

    private lateinit var prefs: SharedPreferences

    private val listener = object : SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            Log.d("TAG", "return value BATMAN")
            if (key == UNIT_SYSTEM_KEY) {
                val temp = sharedPreferences?.getString(UNIT_SYSTEM_KEY, "mmm")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory)[FutureListWeatherViewModel::class.java]
        _binding = FragmentFutureListWeatherBinding.inflate(layoutInflater, container, false)
        viewModel.currentUnitSystem.observe(viewLifecycleOwner){
            bindUI()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[FutureListWeatherViewModel::class.java]
        bindUI()

        prefs =
            context?.applicationContext?.let { PreferenceManager.getDefaultSharedPreferences(it) }!!
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeatherEntries = viewModel.updateWeatherEntries()
        val weatherLocation = viewModel.updateLocation()

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

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let{
                showWeatherDetails(it.weatherEntry.date, view)
            }
        }
    }

    private fun showWeatherDetails(date: LocalDate, view: View) {
        val dateString = LocalDateConverter.dateToString(date)!!
        val actionDetail = FutureListWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showToast() {
        Toast.makeText(this@FutureListWeatherFragment.context, "Clicked", Toast.LENGTH_SHORT).show()
    }
}