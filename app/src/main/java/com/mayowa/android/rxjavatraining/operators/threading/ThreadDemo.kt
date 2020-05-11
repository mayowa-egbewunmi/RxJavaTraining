package com.mayowa.android.rxjavatraining.operators.threading

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * 1. If Thread is not specified in an Rx chain,
 * the system main thread will be used
 *
 * 2. Schedulers.io  is for I/O operations,
 * Scheduler.computation is for performing intensive logic,
 * AndroidSchedulers.mainThread is for ui update
 *
 * 3. In Rx, subscribeOn operator determines on which thread “data stream”
 * will be computed and emitted. Without observeOn operator in Rx Chain,
 * data will be observed from subscribeOn thread
 *
 * 4. In Rx, observeOn operator determines the thread of all the rx operators
 * beneath it. To avoid bad behaviour, observeOn operator must come
 * immediately before subscribe()
 *
 * 5. The subscribeOn thread and the observeOn thread do not block each other
 */
class ThreadDemo {

    fun testThreading() = Flowable.create<Int>({ emitter ->
        for (i in 0..5) {
            println("emitting data    thread_name = ${Thread.currentThread().name}")
            emitter.onNext(i)
        }
        emitter.onComplete()
    }, BackpressureStrategy.BUFFER)
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")

    val compositeDisposable = CompositeDisposable()

    val underTest = ThreadDemo()

    val subscription =
        underTest.testThreading()
            .subscribeOn(Schedulers.io())
            .doOnNext { println("on next thread name = ${Thread.currentThread().name}") }
            .observeOn(Schedulers.single())
            .subscribe {
                println("observed data = $it, thread_name = ${Thread.currentThread().name}\n===================================")
            }

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
