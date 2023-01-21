package com.app.orion.viewmodel

import com.app.orion.Result
import org.jsoup.Jsoup
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class OrionViewModelTest {

    @Test
    fun test_validateAdmissionForm(){
        val orionViewModel = OrionViewModel(RuntimeEnvironment.application)
        val result = orionViewModel.validateAdmissionForm(
            orionViewModel.getAdmissionNo().toString(),
            orionViewModel.getCurrentDateAndTime(),
            "krunal",
            "8806616913",
            "GYM",
            "MLY",
            "12-06-2023",
            "12-07-2023",
            "1221"
        ) as Result.Success
        Assert.assertTrue(result.data.trim().isNotEmpty())
    }

    @Test
    fun test_parse_admission_receipt(){
        val orionViewModel = OrionViewModel(RuntimeEnvironment.application)

        val htmlString = RuntimeEnvironment.application.assets.open("admissionFor.html").bufferedReader().use {
            it.readText()
        }
        val admissionNumber = orionViewModel.getAdmissionNo()
        val todayDate = orionViewModel.getCurrentDateAndTime()
        val name = "krunal"
        val phone = "8806616913"
        val admissionFor = "GYM"
        val duration = "XY"
        val admissionDate = "12-06-2023"
        val newRenewalDate = "12-07-2023"
        val amount = "1221"

        val doc = Jsoup.parse(htmlString)
        doc.getElementById("sr_no").appendText(admissionNumber.toString().trim())
        doc.getElementById("date").appendText(todayDate.trim())
        doc.getElementById("name").appendText(name.trim())
        doc.getElementById("phone").appendText(phone.trim())
        doc.getElementById("admission_for").appendText(admissionFor.trim())
        doc.getElementById("duration").appendText(duration.trim())

        doc.getElementById("admission_date").appendText(admissionDate.trim())
        doc.getElementById("new_renewal_date").appendText(newRenewalDate.trim())
        doc.getElementById("amount").appendText(amount.trim())

        Assert.assertTrue(doc.toString().trim().isNotEmpty())

    }

}