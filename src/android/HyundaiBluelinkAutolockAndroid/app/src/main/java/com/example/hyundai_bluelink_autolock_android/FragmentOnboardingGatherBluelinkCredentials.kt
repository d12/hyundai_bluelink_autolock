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
import com.google.android.material.textfield.TextInputLayout

class FragmentOnboardingGatherBluelinkCredentials : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var pinEditText: EditText
    private lateinit var countryAutoCompleteTextView: AutoCompleteTextView
    private lateinit var verifyButton: Button

    private val countries = listOf(
        Country("Canada", R.drawable.ca_80x)
    )

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
        countryAutoCompleteTextView = view.findViewById(R.id.country_selector)
        verifyButton = view.findViewById(R.id.gather_bluelink_credentials_verify)

//        Handler for verify button press
        verifyButton.setOnClickListener {
            verifyClicked()
        }

        // Set up AutoCompleteTextView adapter
        val adapter = CustomSpinnerWithHintAdapter(
            requireContext(),
            countries
        )
        countryAutoCompleteTextView.setAdapter(adapter)

        // Add TextWatchers
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        pinEditText.addTextChangedListener(textWatcher)
        countryAutoCompleteTextView.addTextChangedListener(textWatcher)

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
        val country = countryAutoCompleteTextView.text.toString()

        verifyButton.isEnabled = email.isNotEmpty() &&
                password.isNotEmpty() &&
                pin.isNotEmpty() &&
                pin.length == 4 &&
                country.isNotEmpty() && country != "Country"
    }
}
