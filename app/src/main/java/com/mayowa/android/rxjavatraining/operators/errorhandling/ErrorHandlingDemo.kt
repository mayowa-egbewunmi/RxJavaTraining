package com.mayowa.android.rxjavatraining.operators.errorhandling

import com.mayowa.android.rxjavatraining.utils.DatabaseClient
import com.mayowa.android.rxjavatraining.utils.Result
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeoutException

/**
 * An unexpected exception is an error whose source is unknown while
 * an expected exception has a known source.
 *
 * 1. Unexpected exception thrown by an Observable or its Operator
 * can be leaked to the Observer’s onError method but should not be swallowed
 *
 * 2. Expected exception thrown by an Observable or its Operator
 * should not be leaked to the Observer’s onError method
 *
 * 3. RxJava Toolkits provide operators for handling exception/error
 *
 * 4. Sometimes we want to log an exception after the data stream has been observed/consumed,
 * where in the rx flow will this be done?
 *
 * TODO: Demo onErrorReturnItem, onErrorReturn, onErrorResumeNext, Retry, RetryWhen
 */

class ErrorHandlingDemo {

    private val dbClient = DatabaseClient()

    fun testCompletableException() = Completable.fromAction {
        dbClient.saveUserName(null)
    }

    fun testSingleException() = Single.fromCallable {
        dbClient.getUser(-1)
    }

    fun fetchTransactionHistory() = Single.fromCallable {
        throw TimeoutException()
    }

    fun fetchFromLocalDB() = Single.fromCallable {
        listOf("#5000 Transfer from Access to GTB",
            "DSTV subscription", "1000 Recharge card purchase")
    }

    fun testNumberFormatException() = Observable.just("33A")

    val clickEventStream = Observable.just(Event)
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")

    val compositeDisposable = CompositeDisposable()
    val underTest = ErrorHandlingDemo()

    val subscription =
        underTest.testSingleException()
            .retry { count: Int, _: Throwable ->
                if (count == 3) {
                    false
                } else {
                    println("Retry at $count")
                    true
                }
            }
            .subscribe(
                 {
                    println("failure = ${it?.email}, thread_name = ${Thread.currentThread().name}")
                },
                {
                    it.printStackTrace()
                }
            )

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()

}

object Event

