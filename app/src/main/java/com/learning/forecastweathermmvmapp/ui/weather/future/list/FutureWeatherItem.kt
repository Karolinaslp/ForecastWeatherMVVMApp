package com.learning.forecastweathermmvmapp.ui.weather.future.list


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.forecastweathermmvmapp.R
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.MetricSimpleFutureWeatherEntry
import com.learning.forecastweathermmvmapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import com.learning.forecastweathermmvmapp.databinding.ItemFutureWeatherBinding
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : BindableItem<ItemFutureWeatherBinding>() {

    override fun getLayout() = R.layout.item_future_weather

    override fun initializeViewBinding(view: View): ItemFutureWeatherBinding {
        return ItemFutureWeatherBinding.bind(view)
    }

    override fun bind(viewBinding: ItemFutureWeatherBinding, position: Int) {
        viewBinding.apply {
            textViewCondition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage(viewBinding)
        }
    }

    private fun updateConditionImage(viewBinding: ItemFutureWeatherBinding) {
        Glide.with(viewBinding.root.context)
            .load("https:" + weatherEntry.conditionIconUrl)
            .into(viewBinding.imageViewConditionIcon)
    }

    private fun ItemFutureWeatherBinding.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textViewDate.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ItemFutureWeatherBinding.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry) "°C"
        else "°F"
        textViewTemperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }
}

