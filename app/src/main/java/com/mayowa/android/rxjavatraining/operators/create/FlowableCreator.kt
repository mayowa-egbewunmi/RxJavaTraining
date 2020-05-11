package com.mayowa.android.rxjavatraining.operators.create

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * Flowable is useful for listening to changes in database.
 * Room DB has integration with Rx that makes this work out of the box.
 *
 * Flowable data stream created using the [Flowable.create] operator
 *
 * Flowable has a STRATEGY for managing BackPressure,
 * When is BackPressure STRATEGY required?
 * What happens if the BackPressure is missing
 * Flowable uses observer called ObservableObserver that implements onNext, onError and onComplete
 * What happens if a FlowableObserver does not implement onError?
 *
 * TODO: Demonstrate use case without change in thread to explain BackPressure
 * TODO: Demonstrate a change in thread to explain BackPressure further
 * TODO: Demonstrate onError implemented and not implemented
 */
class FlowableCreator {

    private fun flowableFromCreate() = Flowable.create<Int>(FlowableOnSubscribe { emitter ->
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
