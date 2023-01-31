package com.android.shared.code.utils.liveData

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicInteger

/**
 * Event live data provide similar functionality like liveData except observer recalls moments:
 * - value will be sent to all observers one time - it's warranty;
 * - value will not be resent again after re-observing (for ex. after view recreation).
 *
 * @param T Any object
 * @constructor Create empty Event live data
 */
class EventLiveData<T> : MutableLiveData<T> {
    private val observerCounter = AtomicInteger(0)
    private val eventCounter = AtomicInteger(0)

    constructor() : super()

    constructor(value: T) : super(value)

    @MainThread
    override fun setValue(value: T) {
        eventCounter.set(0)
        super.setValue(value)
    }

    @MainThread
    override fun postValue(value: T) {
        super.postValue(value)
    }

    @MainThread
    fun postValueIfChanged(value: T) {
        if (this.value != value)
            super.postValue(value)
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) =
        super.observe(owner, ObserverWrapper(eventCounter, observerCounter, observer))

    @MainThread
    override fun observeForever(observer: Observer<in T>) =
        super.observeForever(ObserverWrapper(eventCounter, observerCounter, observer))

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observerCounter.get() > 0)
            observerCounter.decrementAndGet()
        super.removeObserver(observer)
    }
}

/**
 * Observer wrapper.
 *
 * @param T ObserverWrapper data type
 * @property eventCounter [AtomicInteger] event counter
 * @property observerCounter [AtomicInteger] observer counter
 * @property originalObserver Original observer
 * @constructor Create [ObserverWrapper]
 */
private class ObserverWrapper<T>(
    private var eventCounter: AtomicInteger,
    private var observerCounter: AtomicInteger,
    private val originalObserver: Observer<in T>
) : Observer<T> {
    init {
        observerCounter.getAndIncrement()
    }

    private val wrappedObserver: Observer<in T> = Observer {
        if (eventCounter.get() < observerCounter.get()) {
            eventCounter.getAndIncrement()
            originalObserver.onChanged(it)
        }
    }

    override fun onChanged(value: T) {
        wrappedObserver.onChanged(value)
    }
}