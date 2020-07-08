package com.mayowa.android.rxjavatraining.operators.create

import com.mayowa.android.rxjavatraining.utils.DatabaseClient
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action

/**
 * Completable is useful for operations where we don’t care about the result of the transaction.
 * E.g Caching, Saving a record in DB.
 *
 * Completable data stream created via the [Action] functional interface
 *
 * The Observer for Completable is CompletableObserver that implements onComplete and onError
 * What happens if the Action invoked throws an exception?
 *
 * TODO: Update the implementation to use Lambda expression
 * TODO: Demonstrate onError implemented and not implemented
 */
class CompletableCreator {

    private val dbClient = DatabaseClient()

    private val action = Action { dbClient.saveUserName(null) }

    private fun completableFromAction() = Completable.fromAction(action)

    fun testCompletable(): Completable = completableFromAction()
}

fun main() {
    println("The application is running on ${Thread.currentThread().name} thread ")

    val underTest = CompletableCreator()
    val compositeDisposable = CompositeDisposable()

    val subscription =
        underTest.testCompletable()
            .subscribe(
                {
                    println("Result observed on ${Thread.currentThread().name} thread")
                },
                {
                    error -> error.printStackTrace()
                }
            )

    compositeDisposable.add(subscription)
    compositeDisposable.dispose()
}
