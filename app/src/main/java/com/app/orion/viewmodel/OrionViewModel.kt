package com.app.orion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.orion.R
import com.app.orion.Result
import com.app.orion.Result.Success
import com.app.orion.Result.Error
import com.app.orion.exception.*
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class OrionViewModel(application: Application) : AndroidViewModel(application) {

    fun validateAdmissionForm(
        admissionNumber : String,
        todayDate: String,
        name: String,
        phone: String,
        admissionFor: String,
        duration: String,
        admissionDate: String,
        newRenewalDate: String,
        amount: String
    ): Result<String> = try {
        if(admissionNumber.trim().isEmpty()){
            throw SomethingWentWrongException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_something_went_wrong)
            )
        }
        if(todayDate.trim().isEmpty()){
            throw SomethingWentWrongException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_something_went_wrong)
            )
        }
        if (name.trim().isEmpty()) {
            throw InvalidNameException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_name)
            )
        }
        if (!phone.trim().matches(Regex("\\d{10}"))) {
            throw InvalidPhoneNumberException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_phone_number)
            )
        }
        if (admissionFor.trim().isEmpty()) {
            throw InvalidAdmissionException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_admission)
            )
        }
        if(isNotAvailableAdmissionForIsSelected(admissionFor)){
            throw InvalidAdmissionException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_admission)
            )
        }
        if (duration.trim().isEmpty()) {
            throw InvalidDurationException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_duration)
            )
        }
        if (isNotAvailableDurationIsSelected(duration)) {
            throw InvalidDurationException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_duration)
            )
        }
        if(admissionDate.trim().isEmpty()){
            throw SomethingWentWrongException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_something_went_wrong)
            )
        }
        if(newRenewalDate.trim().isEmpty()){
            throw SomethingWentWrongException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_something_went_wrong)
            )
        }
        if (amount.trim().isEmpty()) {
            throw InvalidAmountException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_amount)
            )
        }
        Success(fetchAdmissionReceipt(
            admissionNumber,
            todayDate,
            name,
            phone,
            admissionFor,
            duration,
            admissionDate,
            newRenewalDate,
            amount
        ))
    } catch (ex: InvalidNameException) {
        Error(ex)
    } catch (ex: InvalidPhoneNumberException) {
        Error(ex)
    } catch (ex: InvalidAdmissionException) {
        Error(ex)
    }  catch (ex: InvalidDurationException) {
        Error(ex)
    } catch (ex: InvalidAmountException) {
        Error(ex)
    } catch (ex: SomethingWentWrongException){
        Error(ex)
    }

    fun getAdmissionNo(): CharSequence =
        getApplication<Application>().resources.getString(
            R.string.title_admission_no,
            Random(Int.MAX_VALUE).nextInt().toString()
        )

    fun getCurrentDateAndTime(): String =
        SimpleDateFormat("dd-MM-yyyy hh:mm a").format(Calendar.getInstance().time)

    fun convertIntoDate(long: Long): String =
        SimpleDateFormat("dd-MM-yyyy").format(Date(long))

    private fun isNotAvailableAdmissionForIsSelected(admissionFor: String) =
        !arrayListOf(
            getApplication<Application>().resources.getString(R.string.title_gym),
            getApplication<Application>().resources.getString(R.string.title_cardio),
            getApplication<Application>().resources.getString(R.string.title_gym_cardio)
        ).contains(admissionFor)

    private fun isNotAvailableDurationIsSelected(duration: String) =
        !arrayListOf(
            getApplication<Application>().resources.getString(R.string.title_monthly),
            getApplication<Application>().resources.getString(R.string.title_qly),
            getApplication<Application>().resources.getString(R.string.title_hly),
            getApplication<Application>().resources.getString(R.string.title_yearly)
        ).contains(duration)

    private fun fetchAdmissionReceipt(
        admissionNumber : String,
        todayDate : String,
        name: String,
        phone: String,
        admissionFor: String,
        duration: String,
        admissionDate: String,
        newRenewalDate: String,
        amount: String
    ) : String {
        val htmlString = getApplication<Application>().assets.open("admissionFor.html").bufferedReader().use {
            it.readText()
        }
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
        return doc.toString()
    }


}