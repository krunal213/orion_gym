package com.app.orion_gym.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.app.orion_gym.R
import com.app.orion_gym.Result
import com.app.orion_gym.Result.Success
import com.app.orion_gym.Result.Error
import com.app.orion_gym.exception.*
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class OrionViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    var googleAccountCredential: GoogleAccountCredential? = null
    private var database = FirebaseDatabase.getInstance().getReference("column_number")

    fun validateAdmissionForm(
        admissionNumber: String,
        todayDate: String,
        name: String,
        phone: String,
        admissionFor: String,
        duration: String,
        admissionDate: String,
        newRenewalDate: String,
        amount: String
    ) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        emit(
            try {
                if (admissionNumber.trim().isEmpty()) {
                    throw SomethingWentWrongException(
                        getApplication<Application>()
                            .resources
                            .getString(R.string.error_something_went_wrong)
                    )
                }
                if (todayDate.trim().isEmpty()) {
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
                if (isNotAvailableAdmissionForIsSelected(admissionFor)) {
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
                if (admissionDate.trim().isEmpty()) {
                    throw SomethingWentWrongException(
                        getApplication<Application>()
                            .resources
                            .getString(R.string.error_something_went_wrong)
                    )
                }
                if (newRenewalDate.trim().isEmpty()) {
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
                updateDataInSheet(
                    admissionNumber,
                    todayDate,
                    name,
                    phone,
                    admissionFor,
                    duration,
                    admissionDate,
                    newRenewalDate,
                    amount
                )
                Success(
                    fetchAdmissionReceipt(
                        admissionNumber,
                        todayDate,
                        name,
                        phone,
                        admissionFor,
                        duration,
                        admissionDate,
                        newRenewalDate,
                        amount
                    )
                )
            } catch (ex: InvalidNameException) {
                Error(ex)
            } catch (ex: InvalidPhoneNumberException) {
                Error(ex)
            } catch (ex: InvalidAdmissionException) {
                Error(ex)
            } catch (ex: InvalidDurationException) {
                Error(ex)
            } catch (ex: InvalidAmountException) {
                Error(ex)
            } catch (ex: SomethingWentWrongException) {
                Error(ex)
            } catch (ex : Exception){
                println(ex.message)
                Error(SomethingWentWrongException(
                    getApplication<Application>()
                        .resources
                        .getString(R.string.error_something_went_wrong)
                ))
            }
        )
    }

    fun getAdmissionNo(): CharSequence =
        getApplication<Application>().resources.getString(
            R.string.title_admission_no,
            System.currentTimeMillis().toString()
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
        admissionNumber: String,
        todayDate: String,
        name: String,
        phone: String,
        admissionFor: String,
        duration: String,
        admissionDate: String,
        newRenewalDate: String,
        amount: String
    ): String = with(
        Jsoup.parse(getApplication<Application>().assets.open("admissionFor.html")
            .bufferedReader().use {
                it.readText()
            })
    ) {
        getElementById("sr_no").appendText(admissionNumber.trim())
        getElementById("date").appendText(todayDate.trim())
        getElementById("name").appendText(name.trim())
        getElementById("phone").appendText(phone.trim())
        getElementById("admission_for").appendText(admissionFor.trim())
        getElementById("duration").appendText(duration.trim())
        getElementById("admission_date").appendText(admissionDate.trim())
        getElementById("new_renewal_date").appendText(newRenewalDate.trim())
        getElementById("amount").appendText(amount.trim())
        return this.toString()
    }

    private suspend fun updateDataInSheet(
        admissionNumber: String,
        todayDate: String,
        name: String,
        phone: String,
        admissionFor: String,
        duration: String,
        admissionDate: String,
        newRenewalDate: String,
        amount: String
    ) {
        val spreadsheetId = "1VNXUc7oqD_GtQUtMXYsVQxnUjZVZTdNqigj9QOme4RA"
        val number = readColumnNumber().first()
        val range = "A$number:I$number"
        val sheetService = Sheets.Builder(
            NetHttpTransport(), GsonFactory.getDefaultInstance(), googleAccountCredential
        ).setApplicationName("orion-gym-android")
            .build()
        val values: List<List<Any>> = Arrays.asList(
            Arrays.asList(
                admissionNumber,
                todayDate,
                name,
                phone,
                admissionFor,
                duration,
                admissionDate,
                newRenewalDate,
                amount
            )
        )
        val body: ValueRange = ValueRange()
            .setValues(values)
        sheetService.spreadsheets().values().update(spreadsheetId, range, body)
            .setValueInputOption("USER_ENTERED")
            .execute()
        writeColumnNumber(number = number.inc())
    }

    private fun writeColumnNumber(number : Long) {
        database.setValue(number)
    }

    private fun readColumnNumber() : Flow<Long> = callbackFlow{
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value as Long
                trySend(value)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(throw Exception())
            }
        })
        awaitClose {  }
    }


}