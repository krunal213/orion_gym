package com.app.orion_gym.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.orion_gym.ProgressBarDialog
import com.app.orion_gym.Result
import com.app.orion_gym.exception.InvalidAmountException
import com.app.orion_gym.exception.InvalidNameException
import com.app.orion_gym.exception.InvalidPhoneNumberException
import com.app.orion_gym.generatePdfFromHTML
import com.app.orion_gym.viewmodel.OrionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.app.orion_gym.R
import com.app.orion_gym.databinding.ContentPreviewBinding

abstract class ContentPreviewFragment : Fragment(),View.OnClickListener {

    private lateinit var progressBarDialog: AlertDialog
    private val orionViewModel by activityViewModels<OrionViewModel>()
    private lateinit var admissionNumber : CharSequence
    private lateinit var todayDate : String
    private lateinit var binding : ContentPreviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getContentPreviewBinding()
        binding.textViewSubtitle.text = getSubtitle()
        admissionNumber = orionViewModel.getAdmissionNo()
        binding.textViewAdmissionNo.text = admissionNumber
        todayDate = orionViewModel.getCurrentDateAndTime()
        binding.textViewAdmissionDate.text = todayDate

        with(binding.editTextName) {
            addTextChangedListener {
                binding.textInputLayoutName.error = null
            }
        }
        with(binding.editTextPhone) {
            addTextChangedListener {
                binding.textInputLayoutPhone.error = null
            }
        }
        with(binding.editTextAmount) {
            addTextChangedListener {
                binding.textInputLayoutAmount.error = null
            }
        }
        with(binding.editTextAdmissionDate) {
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
                            binding.editTextAdmissionDate.setText(orionViewModel.convertIntoDate(it))
                        }
                    }
                }
            }
        }
        with(binding.editTextNextRenewalDate) {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                fragmentManager?.let { fragmentManager ->
                    with(
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText(resources.getString(R.string.title_next_renewal_date))
                            .build()
                    ) {
                        show(fragmentManager, "tag")
                        addOnPositiveButtonClickListener {
                            binding.editTextNextRenewalDate.setText(orionViewModel.convertIntoDate(it))
                        }
                    }
                }
            }
        }
    }

    abstract fun getContentPreviewBinding(): ContentPreviewBinding

    abstract fun getSubtitle(): String

    protected fun showProgressBar() {
        progressBarDialog = ProgressBarDialog(requireContext()).show()
    }

    protected fun cancelProgressBar() {
        progressBarDialog.dismiss()
    }

    override fun onClick(p0: View?) {
        orionViewModel.validateAdmissionForm(
            admissionNumber = admissionNumber.toString(),
            todayDate = todayDate.toString(),
            name = binding.editTextName.text.toString().trim(),
            phone = binding.editTextPhone.text.toString().trim(),
            admissionFor = binding.radioGroupAdmissionFor
                .findViewById<RadioButton>(
                    binding.radioGroupAdmissionFor.checkedRadioButtonId
                )?.text.toString().trim(),
            duration = binding.radioGroupForYear
                .findViewById<RadioButton>(
                    binding.radioGroupForYear.checkedRadioButtonId
                )?.text.toString().trim(),
            admissionDate = binding.editTextAdmissionDate.text.toString().trim(),
            newRenewalDate = binding.editTextNextRenewalDate.text.toString().trim(),
            amount = binding.editTextAmount.text.toString().trim(),
        ).observe(viewLifecycleOwner){
            when (it) {
                is Result.Loading->{
                    showProgressBar()
                }
                is Result.Success -> {
                    with(activity as AppCompatActivity) {
                        window.decorView.findViewById<View>(android.R.id.content)
                            .clearFocus()
                        generatePdfFromHTML(
                            it.data,
                            "${binding.editTextName.text.toString().trim()}_$todayDate"
                        )
                    }
                    cancelProgressBar()
                }
                is Result.Error -> {
                    when (it.exception) {
                        is InvalidNameException -> {
                            binding.textInputLayoutName.error = it.exception.message
                        }
                        is InvalidPhoneNumberException -> {
                            binding.textInputLayoutPhone.error = it.exception.message
                        }
                        is InvalidAmountException -> {
                            binding.textInputLayoutAmount.error = it.exception.message
                        }
                    }
                    cancelProgressBar()
                }
            }
        }
    }
}