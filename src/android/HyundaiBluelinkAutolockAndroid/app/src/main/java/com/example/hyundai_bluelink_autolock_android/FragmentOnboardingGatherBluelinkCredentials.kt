package com.example.hyundai_bluelink_autolock_android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment

class FragmentOnboardingGatherBluelinkCredentials : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var pinEditText: EditText
    private lateinit var countrySpinner: Spinner
    private lateinit var verifyButton: Button

    private fun verifyClicked() {
        println("Wee woo we got clicked!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_onboarding_gather_bluelink_credentials,
            container,
            false
        )

        emailEditText = view.findViewById(R.id.email)
        passwordEditText = view.findViewById(R.id.password)
        pinEditText = view.findViewById(R.id.pin)
        countrySpinner = view.findViewById(R.id.country_selector)
        verifyButton = view.findViewById(R.id.gather_bluelink_credentials_verify)

//        Handler for verify button press
        verifyButton.setOnClickListener {
            verifyClicked()
        }

        // Set up Spinner adapter and listener
        val countries = listOf(
            Country("Country", null), // Placeholder
            Country("Canada", R.drawable.ca_80x)
            // Add other countries here
        )

        val adapter = CustomSpinnerWithHintAdapter(
            requireContext(),
            R.layout.country_spinner_item,
            countries
        )
        adapter.setDropDownViewResource(R.layout.country_spinner_item)
        countrySpinner.adapter = adapter

        // Add TextWatchers and OnItemSelectedListener
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        pinEditText.addTextChangedListener(textWatcher)
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                checkFieldsForEmptyValues()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Initially disable the button
        verifyButton.isEnabled = false

        return view
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkFieldsForEmptyValues()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun checkFieldsForEmptyValues() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val pin = pinEditText.text.toString()
        val countryPosition = countrySpinner.selectedItemPosition

        verifyButton.isEnabled = email.isNotEmpty() &&
                password.isNotEmpty() &&
                pin.isNotEmpty() &&
                pin.length == 4 &&
                countryPosition != 0 // Assuming 0 is the position of the default "Country" placeholder
    }
}
