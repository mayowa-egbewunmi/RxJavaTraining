package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import com.mayowa.android.rxjavatraining.utils.ApiClient
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * Zip This operator combine the emissions of multiple Observables
 * together via a specified function and emit single items for each
 * combination based on the results of this function.
 *
 * Note: Multi Threading doesn't affect the result of a zip operation
 * but it makes the operation faster
 */
class RxZipDemo {

    /**Mock location query to return lat/long*/
    fun getUserLocation() = Single.just(Pair(6.88888, 3.33222))
    /**Mock local DB query to return username*/
    fun getUsername() = Single.just("Mayowa")

    fun observable1(): Observable<Int> = Observable.create<Int> {
        Thread.sleep(50)

        it.onNext(1)
        Thread.sleep(50)

        it.onNext(3)

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

data class User(val name: String, val location: Pair<Double, Double>)

fun main() {

    val rxZipDemo = RxZipDemo()
    val compositeDisposable = CompositeDisposable()
    val apiClient = ApiClient()

    val subscription = Observable.zip(
        rxZipDemo.observable1()
            .subscribeOn(Schedulers.io()),
        rxZipDemo.observable2()
            .subscribeOn(Schedulers.io()),

        BiFunction<Int, Int, Int> { value1, value2 -> value1 + value2 }
    )
//        .flatMap {
//            Single.fromCallable { apiClient.registerUser(it) }
//                .subscribeOn(Schedulers.io())
//        }
        .subscribe({
            println("message value = ${it}, thread_name = ${Thread.currentThread().name}")
        }, { error ->
            error.printStackTrace()
        })

    compositeDisposable.add(subscription)
    Thread.sleep(10000)
    compositeDisposable.dispose()
}
