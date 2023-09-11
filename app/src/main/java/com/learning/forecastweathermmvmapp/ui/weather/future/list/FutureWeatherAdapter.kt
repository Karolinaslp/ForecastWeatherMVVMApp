//package com.learning.forecastweathermmvmapp.ui.weather.future.list
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.list.MetricSimpleFutureWeatherEntry
//import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
//import com.learning.forecastweathermmvmapp.databinding.ItemFutureWeatherBinding
//import org.threeten.bp.LocalDate
//import org.threeten.bp.format.DateTimeFormatter
//import org.threeten.bp.format.FormatStyle
//
//class FutureWeatherAdapter(
//    private val weatherEntries: List<UnitSpecificSimpleFutureWeatherEntry>,
//    private val onItemClick: (UnitSpecificSimpleFutureWeatherEntry) -> Unit
//) : RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ItemFutureWeatherBinding.inflate(inflater, parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val weatherEntry = weatherEntries[position]
//        holder.bind(weatherEntry)
//    }
//
//    override fun getItemCount(): Int = weatherEntries.size
//
//    inner class ViewHolder(private val binding: ItemFutureWeatherBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        private val condition = binding.textViewCondition
//        private val date = binding.textViewDate
//        private val temperature = binding.textViewTemperature
//        private val icon = binding.textViewConditionImage
//
//        fun bind(weatherEntry: UnitSpecificSimpleFutureWeatherEntry) {
//            condition.text = weatherEntry.conditionText
//            updateDate(weatherEntry.date)
//            updateTemperature(weatherEntry)
//            updateConditionImage(weatherEntry.conditionIconUrl)
//
//            itemView.setOnClickListener { onItemClick(weatherEntry) }
//
//        }
//
//        private fun updateDate(date: LocalDate) {
//            val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
//            this.date.text = date.format(dtFormatter)
//        }
//
//        @SuppressLint("SetTextI18n")
//        private fun updateTemperature(weatherEntry: UnitSpecificSimpleFutureWeatherEntry) {
//            val unitAbbreviation =
//                if (weatherEntry is MetricSimpleFutureWeatherEntry) "°C" else "°F"
//            temperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
//        }
//
//        private fun updateConditionImage(iconUrl: String) {
//            Glide.with(itemView)
//                .load("http:$iconUrl")
//                .into(icon)
//        }
//    }
//
//}