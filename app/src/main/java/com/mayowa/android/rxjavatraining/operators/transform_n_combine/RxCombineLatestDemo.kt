package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * CombineLatest when an item is emitted by either of two Observables,
 * combine the latest item emitted by each Observable via a
 * specified function and emit items based on the results of this function
 *
 * TODO: Demo how threading can affect the result here
 */
class RxCombineLatestDemo {

    fun observable1(): Observable<Int> = Observable.create<Int> {
        Thread.sleep(50)

        it.onNext(1)
        Thread.sleep(50)

        it.onNext(3)
        Thread.sleep(50)

        it.onNext(5)

        it.onComplete()
    }
        .subscribeOn(Schedulers.io())

    fun observable2(): Observable<Int> = Observable.create<Int> {
        it.onNext(2)
        it.onNext(4)
        it.onNext(6)
        it.onComplete()
    }
        .subscribeOn(Schedulers.io())
}

fun main() {

    val rxCombineLatestDemo = RxCombineLatestDemo()
    val compositeDisposable = CompositeDisposable()

    val subscription = Observable.combineLatest(
        rxCombineLatestDemo.observable1(),
        rxCombineLatestDemo.observable2(),
        BiFunction<Int, Int, Int> { data1, data2 ->
            print("data1 = $data1, data2 = $data2 ")
            data1 + data2
        }
    )
        .observeOn(Schedulers.single())
        .subscribe({
            println("result = ${it}, thread_name = ${Thread.currentThread().name}")
        }, { error ->
            error.printStackTrace()
        })

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
