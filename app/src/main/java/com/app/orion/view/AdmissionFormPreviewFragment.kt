package com.app.orion.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.app.orion.R
import com.app.orion.Result
import com.app.orion.exception.InvalidAmountException
import com.app.orion.exception.InvalidNameException
import com.app.orion.exception.InvalidPhoneNumberException
import com.app.orion.viewmodel.OrionViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout

class AdmissionFormPreviewFragment : ContentPreviewFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_admission_form, container, false)

    override fun getSubtitle(): String = resources.getString(R.string.title_admission_form)


}