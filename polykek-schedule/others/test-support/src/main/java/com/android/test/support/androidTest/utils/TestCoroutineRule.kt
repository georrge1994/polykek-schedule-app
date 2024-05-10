package com.android.test.support.androidTest.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test coroutine rule.
 *
 * @property testDispatcher Test dispatcher
 * @constructor Create [TestCoroutineRule]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineRule(private val testDispatcher: TestDispatcher) : TestRule {
    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testDispatcher)
            base.evaluate()
            Dispatchers.resetMain()
        }
    }
}