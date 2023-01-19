package com.app.orion.viewmodel

import com.app.orion.Result
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
            "krunal",
            "8806616913",
            "GYM",
            "XY",
            "1221"
        ) as Result.Error
        Assert.assertEquals("Enter Valid Duration.",result.exception.message)
    }

}