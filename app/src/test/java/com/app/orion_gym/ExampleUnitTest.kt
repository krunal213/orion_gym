package com.app.orion_gym

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun or_operator_test() {
        val data = "GYM"
        val result = arrayListOf<String>("GYM","Cardio","GYM + Cardio").contains(data)
        assertTrue(result)
    }
}