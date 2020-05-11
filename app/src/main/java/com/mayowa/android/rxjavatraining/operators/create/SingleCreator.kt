package com.mayowa.android.rxjavatraining.operators.create

import com.mayowa.android.rxjavatraining.utils.DatabaseClient
import com.mayowa.android.rxjavatraining.utils.ProxyUser
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

/**
 * Single is useful from operations where we care about the result of the operation.
 * E.g API request, Saving a record in DB.
 *
 * Single data stream created via the [Function] functional interface
 *
 * The Observer for Single is SingleObserver that implements onSuccess, onError
 * What happens if a null result is returned?
 *
 * TODO: Update the implementation to use Lambda expression
 * TODO: Demonstrate onError implemented and not implemented
 */
class SingleCreator {

    private val dbClient = DatabaseClient()

    private fun singleFromCallable(): Single<ProxyUser> = Single.fromCallable {
        dbClient.getUser(-1)
    }

    fun testSingle(): Single<ProxyUser> = singleFromCallable()
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")

    val underTest = SingleCreator()
    val compositeDisposable = CompositeDisposable()

    val subscription =
        underTest.testSingle()
            .subscribe(
                {
                    println("data observed = ${it.fullname}, thread_name = ${Thread.currentThread().name}")
                },
                {
                    error -> error.printStackTrace()
                }
            )

    compositeDisposable.add(subscription)
    compositeDisposable.dispose()
}
