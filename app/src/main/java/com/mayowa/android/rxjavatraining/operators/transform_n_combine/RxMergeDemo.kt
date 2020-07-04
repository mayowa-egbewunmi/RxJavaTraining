package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * This operator combines multiple Observables into one by merging
 * their emissions
 *
 * Note: Multi Threading doesn't affect the result of a merge operation
 * but it makes the operation faster
 */
class RxMergeDemo {

    fun firstFiveDigits() = Observable.just(
        1, 2, 3, 4, 5
    )

    fun otherDigits() = Observable.just(
        6, 7, 8, 9, 10
    )
}

fun main() {

    val rxMergeDemo = RxMergeDemo()
    val compositeDisposable = CompositeDisposable()

    val subscription = Observable.merge(
        rxMergeDemo.otherDigits(),
        rxMergeDemo.firstFiveDigits()
    )
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.single())
        .subscribe({
            println("data = ${it}, thread_name = ${Thread.currentThread().name}")
        }, { error ->
            error.printStackTrace()
        })

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
