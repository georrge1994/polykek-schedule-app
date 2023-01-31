package com.android.test.support.androidTest.base

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import java.util.concurrent.Executors
import kotlin.reflect.KClass

/**
 * Base room repository test contains general code for room repositories testing.
 *
 * @constructor Create empty Base room repository test
 */
abstract class BaseRoomRepositoryTest<T : RoomDatabase>(private val clazz: KClass<T>) : BaseAndroidUnitTestForSubscriptions() {
    protected lateinit var db: T
    private var isInit = false

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    override fun beforeTest() {
        super.beforeTest()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val builder = Room.inMemoryDatabaseBuilder(context, clazz.java)
        builder.setTransactionExecutor(Executors.newSingleThreadExecutor())
        db = builder.build()
    }

    override fun afterTest() {
        super.afterTest()
        db.close()
    }
}