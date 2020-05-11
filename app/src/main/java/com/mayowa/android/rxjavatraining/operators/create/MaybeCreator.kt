package com.mayowa.android.rxjavatraining.operators.create

import com.mayowa.android.rxjavatraining.utils.ApiClient
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

/**
 * Maybe is useful for transactions where we cannot guarantee
 * that a result will be returned.E.g Building Offline First System
 * where we check the DB first, if result is not available we fetch remotely
 *
 * The Observer for Completable is CompletableObserver that implements onComplete and onError
 * What happens if the Action invoked throws an exception?
 *
 * TODO: Test implementation without switchIfEmpty
 * TODO: Test implementation with switchIfEmpty
 */
class MaybeCreator {

    /**
     * Mock implementation of a database interaction to fetch survey from DB
     * Returns empty stream if the survey is not available
     */
    fun testMaybe(surveyId: Long): Maybe<String> {
        return if (surveyId == 0L) {
            Maybe.empty()
        } else {
            Maybe.just("Demographics Survey From Local DB")
        }
    }
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")

    val underTest = MaybeCreator()
    val compositeDisposable = CompositeDisposable()
    val apiClient = ApiClient()

    val subscription =
        underTest.testMaybe(0L)
            .switchIfEmpty(Single.fromCallable { apiClient.getSurvey(0L) })
            .subscribe(
                {
                    println("observed data = $it, thread_name = ${Thread.currentThread().name}")
                },
                {
                    error -> error.printStackTrace()
                }
            )

    compositeDisposable.add(subscription)
    compositeDisposable.dispose()
}
