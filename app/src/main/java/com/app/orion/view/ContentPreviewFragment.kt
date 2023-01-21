package com.app.orion.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.app.orion.R
import com.app.orion.Result
import com.app.orion.exception.InvalidAmountException
import com.app.orion.exception.InvalidNameException
import com.app.orion.exception.InvalidPhoneNumberException
import com.app.orion.generatePdfFromHTML
import com.app.orion.viewmodel.OrionViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout

abstract class ContentPreviewFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orionViewModel = activity?.let { OrionViewModel(it.application) }

        val textViewSubtitle = view.findViewById<TextView>(R.id.textViewSubtitle)
        val textViewAdmissionNo = view.findViewById<TextView>(R.id.textViewAdmissionNo)
        val textViewAdmissionDate = view.findViewById<TextView>(R.id.textViewAdmissionDate)
        val ediTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextPhone = view.findViewById<EditText>(R.id.editTextPhone)
        val radioGroupAdmissionFor = view.findViewById<RadioGroup>(R.id.radioGroupAdmissionFor)
        val radioGroupForYear = view.findViewById<RadioGroup>(R.id.radioGroupForYear)
        val editTextAmount = view.findViewById<EditText>(R.id.editTextAmount)
        val textInputLayoutName = view.findViewById<TextInputLayout>(R.id.textInputLayoutName)
        val textInputLayoutPhone = view.findViewById<TextInputLayout>(R.id.textInputLayoutPhone)
        val textInputLayoutAmount = view.findViewById<TextInputLayout>(R.id.textInputLayoutAmount)
        val editTextAdmissionDate = view.findViewById<EditText>(R.id.editTextAdmissionDate)
        val editTextNextRenewalDate = view.findViewById<EditText>(R.id.editTextNextRenewalDate)

        textViewSubtitle.text = getSubtitle()
        val admissionNumber = orionViewModel?.getAdmissionNo()
        textViewAdmissionNo.text = admissionNumber
        val todayDate = orionViewModel?.getCurrentDateAndTime()
        textViewAdmissionDate.text = todayDate

        with(ediTextName) {
            addTextChangedListener {
                textInputLayoutName.error = null
            }
        }
        with(editTextPhone) {
            addTextChangedListener {
                textInputLayoutPhone.error = null
            }
        }
        with(editTextAmount) {
            addTextChangedListener {
                textInputLayoutAmount.error = null
            }
        }
        with(editTextAdmissionDate) {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                fragmentManager?.let { fragmentManager ->
                    with(
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText(resources.getString(R.string.title_admission_date))
                            .build()
                    ) {
                        show(fragmentManager, "tag")
                        addOnPositiveButtonClickListener {
                            editTextAdmissionDate.setText(orionViewModel?.convertIntoDate(it))
                        }
                    }
                }
            }
        }
        with(editTextNextRenewalDate) {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                fragmentManager?.let { fragmentManager ->
                    with(
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText(resources.getString(R.string.title_next_renewal_date))
                            .setCalendarConstraints(
                                CalendarConstraints.Builder()
                                    .setValidator(DateValidatorPointForward.now())
                                    .build()
                            )
                            .build()
                    ) {
                        show(fragmentManager, "tag")
                        addOnPositiveButtonClickListener {
                            editTextNextRenewalDate.setText(orionViewModel?.convertIntoDate(it))
                        }
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.buttonSubmit)
            .setOnClickListener {
                orionViewModel?.validateAdmissionForm(
                    admissionNumber = admissionNumber.toString(),
                    todayDate = todayDate.toString(),
                    name = ediTextName.text.toString().trim(),
                    phone = editTextPhone.text.toString().trim(),
                    admissionFor = radioGroupAdmissionFor
                        .findViewById<RadioButton>(
                            radioGroupAdmissionFor.checkedRadioButtonId
                        )?.text.toString().trim(),
                    duration = radioGroupForYear
                        .findViewById<RadioButton>(
                            radioGroupForYear.checkedRadioButtonId
                        )?.text.toString().trim(),
                    admissionDate = editTextAdmissionDate.text.toString().trim(),
                    newRenewalDate = editTextNextRenewalDate.text.toString().trim(),
                    amount = editTextAmount.text.toString().trim(),
                ).apply {
                    when (this) {
                        is Result.Success -> {
                            with(activity as AppCompatActivity) {
                                window.decorView.findViewById<View>(android.R.id.content)
                                    .clearFocus()
                                generatePdfFromHTML(
                                    data,
                                    "${ediTextName.text.toString().trim()}_$todayDate"
                                )
                            }
                        }
                        is Result.Error -> {
                            when (exception) {
                                is InvalidNameException -> {
                                    textInputLayoutName.error = exception.message
                                }
                                is InvalidPhoneNumberException -> {
                                    textInputLayoutPhone.error = exception.message
                                }
                                is InvalidAmountException -> {
                                    textInputLayoutAmount.error = exception.message
                                }
                            }

                        }
                    }
                }
            }
    }

    abstract fun getSubtitle(): String

}