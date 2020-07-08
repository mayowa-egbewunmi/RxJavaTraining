package com.mayowa.android.rxjavatraining.operators.create

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * Flowable is useful for listening to changes in database.
 *
 * Flowable data stream created using the [Flowable.create] operator
 *
 * Flowable uses observer called ObservableObserver that implements onNext, onError and onComplete
 *
 * Flowable has a built in STRATEGY for managing BackPressure,
 *
 * TODO: Demonstrate when BackPressure is required
 * TODO: Demonstrate a change in thread to explain BackPressure further
 * TODO: Demonstrate onError implemented and not implemented
 */
class FlowableCreator {

    private fun flowableFromCreate() = Flowable.create<Int>({ emitter ->
        for (i in 0..9999) {
            emitter.onNext(i)
        }
        emitter.onComplete()
    }, BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io())

    fun testFlowable(): Flowable<Int> = flowableFromCreate()
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")

    val compositeDisposable = CompositeDisposable()

    val underTest = FlowableCreator()

    val subscription =
        underTest.testFlowable()
            .observeOn(Schedulers.single())
            .subscribe(
                {
                    println("data observed = $it, thread_name = ${Thread.currentThread().name}")
                },
                {
                    error -> error.printStackTrace()
                }
            )

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
