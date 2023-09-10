package com.learning.forecastweathermmvmapp.ui.weather.future.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.forecastweathermmvmapp.R
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.MetricSimpleFutureWeatherEntry
import com.xwray.groupie.Item

import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.databinding.ItemFutureWeatherBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.BindableItem
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.Collections.addAll


class FutureWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : RecyclerView.Adapter<FutureWeatherItem.ViewHolder>() {

    inner class ViewHolder(binding: ItemFutureWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val condition = binding.textViewCondition
        val date = binding.textViewDate
        val temperature = binding.textViewTemperature
        val icon = binding.textViewConditionImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val futureWeatherRow = ItemFutureWeatherBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(futureWeatherRow)
    }

    override fun getItemCount(): Int {
        TODO()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.condition.text = weatherEntry.conditionText
        holder.apply {
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }
    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toFutureWeatherItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }
    private fun initRecyclerView(item: List<FutureWeatherItem>) {
        val groupAdapter = this.apply {
            addAll(items)
        }
    }

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        date.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry) "°C"
        else "°F"
        temperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage() {
        Glide.with(this.itemView)
            .load("http:" + weatherEntry.conditionIconUrl)
            .into(icon)
    }
}

