package com.android.test.support.testFixtures

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Double.parseDouble

class TestDataReader {
    fun readData(fileName: String): ArrayList<Double> {
        val list = ArrayList<Double>()

        try {
            val file = this.javaClass.classLoader?.getResourceAsStream(fileName)
            val reader = BufferedReader(InputStreamReader(file))
            var line: String? = reader.readLine()

            while (line != null) {
                list.add(parseDouble(line))
                line = reader.readLine()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return list
    }
}