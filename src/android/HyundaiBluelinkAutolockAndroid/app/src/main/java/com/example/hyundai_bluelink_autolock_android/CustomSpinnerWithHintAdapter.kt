package com.example.hyundai_bluelink_autolock_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomSpinnerWithHintAdapter(context: Context, resource: Int, objects: List<Country>) :
    ArrayAdapter<Country>(context, resource, objects) {

    override fun isEnabled(position: Int): Boolean {
        // Disable the first item from Spinner
        // First item will be used for hint
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.country_spinner_item, parent, false)

        val imageViewFlag = view.findViewById<ImageView>(R.id.imageView_flag)
        val textViewCountry = view.findViewById<TextView>(R.id.textView_country)

        val country = getItem(position)

        country?.let {
            if (position == 0) {
                textViewCountry.setTextColor(context.resources.getColor(android.R.color.darker_gray))
            } else {
                textViewCountry.setTextColor(context.resources.getColor(android.R.color.black))
            }
            if (it.flagResId == null) {
                imageViewFlag.visibility = View.GONE
            } else {
                imageViewFlag.visibility = View.VISIBLE
                imageViewFlag.setImageResource(it.flagResId)
            }
            textViewCountry.text = it.name
        }

        return view
    }
}

data class Country(val name: String, val flagResId: Int?)
