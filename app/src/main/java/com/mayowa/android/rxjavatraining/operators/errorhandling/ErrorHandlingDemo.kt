package com.mayowa.android.rxjavatraining.operators.errorhandling

import com.mayowa.android.rxjavatraining.utils.DatabaseClient
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

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
 */
class ErrorHandlingDemo {

    private val dbClient = DatabaseClient()

    fun testBackPressureException(): Flowable<Int> = Flowable.create<Int>(
        { emitter ->
            for (i in 0..9999) {
                emitter.onNext(i)
            }
            emitter.onComplete()
        },
        BackpressureStrategy.MISSING
    )
        .subscribeOn(Schedulers.io())

    fun testCompletableException() = Completable.fromAction {
        dbClient.saveUserName(null)
    }
        .subscribeOn(Schedulers.io())

    fun testSingleException() = Single.fromCallable {
        dbClient.getUser(-1)
    }
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")

    val compositeDisposable = CompositeDisposable()

    val underTest = ErrorHandlingDemo()

    val subscription =
        underTest.testSingleException()
            .observeOn(Schedulers.single())
            .map { Result.success(it) }
            .onErrorReturn { Result.failure(it) }
            .subscribe(
                Consumer {
                    if(it.isSuccess) {

                    } else {
                        //show dialog
                    }
                    println("failure = ${it.isFailure}, success = ${it.isSuccess}, thread_name = ${Thread.currentThread().name}")
                }
            )

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()

}
