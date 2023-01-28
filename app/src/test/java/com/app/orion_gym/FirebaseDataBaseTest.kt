package com.app.orion_gym

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import com.google.firebase.database.DatabaseReference




@RunWith(RobolectricTestRunner::class)
class FirebaseDataBaseTest {

    @Test
    fun write_data() = runTest{
        FirebaseApp.initializeApp(RuntimeEnvironment.application.applicationContext)
        val myRef = FirebaseDatabase.getInstance().getReference("message")
        val result = callbackFlow<String> {
            myRef.child(myRef.push().key!!).setValue("hello").addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "Data added successfully.")
                    trySend("Data added successfully.")
                } else {
                    //Never ignore potential errors!
                    Log.d("TAG", "Failed with: ${it.exception?.message}")
                    it.exception?.message?.let { it1 -> trySend(it1) }
                }
            }
            awaitClose()
        }.first()
        println(result)
    }


    @Test
    fun read_data() = runTest{
        FirebaseApp.initializeApp(RuntimeEnvironment.application.applicationContext)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("column_number")
        val data = callbackFlow<String> {
            myRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    println(snapshot)
                    trySend("onDataChange")
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error)
                    trySend("onCancelled")
                }

            })
            awaitClose()
        }.first()
        println(data)

    }

}