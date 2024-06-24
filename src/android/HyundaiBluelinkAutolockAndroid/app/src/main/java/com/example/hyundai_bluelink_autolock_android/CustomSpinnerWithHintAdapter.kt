package com.example.hyundai_bluelink_autolock_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

data class Country(val name: String, val flagResId: Int) {
    override fun toString(): String {
        return name
    }
}

class CustomSpinnerWithHintAdapter(context: Context, countries: List<Country>) :
    ArrayAdapter<Country>(context, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.country_spinner_item)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, R.layout.country_spinner_item)
    }

    private fun createViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        resource: Int
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val country = getItem(position)

        val flagIcon = view.findViewById<ImageView>(R.id.imageView_flag)
        val countryName = view.findViewById<TextView>(R.id.textView_country)

        country?.let {
            flagIcon.setImageResource(it.flagResId)
            countryName.text = it.name
        }

        return view
    }
}
