package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import com.mayowa.android.rxjavatraining.utils.ApiClient
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

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
}

data class User(val name: String, val location: Pair<Double, Double>)

fun main() {

    val rxZipDemo = RxZipDemo()
    val compositeDisposable = CompositeDisposable()
    val apiClient = ApiClient()

    val subscription = Single.zip(
        rxZipDemo.getUsername(),
        rxZipDemo.getUserLocation(),
        BiFunction<String, Pair<Double, Double>, User> { name, location -> User(name, location) }
    )
        .flatMap {
            Single.fromCallable { apiClient.registerUser(it) }
        }
        .subscribe({
            println("message status = ${it}, thread_name = ${Thread.currentThread().name}")
        }, { error ->
            error.printStackTrace()
        })

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
