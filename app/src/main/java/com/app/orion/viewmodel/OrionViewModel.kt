package com.app.orion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.orion.R
import com.app.orion.exception.InvalidNameException
import com.app.orion.Result
import com.app.orion.Result.Success
import com.app.orion.Result.Error
import com.app.orion.exception.InvalidAdmissionException
import com.app.orion.exception.InvalidAmountException
import com.app.orion.exception.InvalidPhoneNumberException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

class OrionViewModel(application: Application) : AndroidViewModel(application) {

    fun validateAdmissionForm(
        name: String,
        phone: String,
        admissionFor: String,
        amount: String
    ): Result<*> = try {
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
        if (amount.trim().isEmpty()) {
            throw InvalidAmountException(
                getApplication<Application>()
                    .resources
                    .getString(R.string.error_invalid_amount)
            )
        }
        Success(null)
    } catch (ex: InvalidNameException) {
        Error(ex)
    } catch (ex: InvalidPhoneNumberException) {
        Error(ex)
    } catch (ex: InvalidAdmissionException) {
        Error(ex)
    } catch (ex: InvalidAmountException) {
        Error(ex)
    }

    fun getAdmissionNo(): CharSequence =
        getApplication<Application>().resources.getString(
            R.string.title_admission_no,
            Random(Int.MAX_VALUE).nextInt().toString()
        )

    fun getCurrentDateAndTime(): String =
        SimpleDateFormat("dd-MM-yyyy HH:mm a").format(Calendar.getInstance().time)


    fun convertIntoDate(long: Long): String =
        SimpleDateFormat("dd-MM-yyyy").format(Date(long))

}