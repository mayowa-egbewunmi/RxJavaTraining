package com.mayowa.android.rxjavatraining.operators.create

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Observable is useful for managing events.
 * E.g Click events from the UI, Location Update, Changes in System properties e.t.c
 *
 * Explain the behaviour of the following observable operators
 *
 * never()
 * empty() { MVIVMBaseActivity: Useful for setting default Observable in base classes }
 * throw() { SurveyUseCase: Premise use case: Useful for rethrowing caught exceptions }
 * just() { TaskSync: Useful in an observable chain to continue data flow }
 * interval() { ReactivePermission: Useful for observing changes at interval }
 * range()
 * startWith() { SurveyInteractor: Useful to returning deferred result before the actual result in an observable }
 *
 */
class ObservableCreator {

    private val observableSource: Observable<Int> = Observable.create<Int>(ObservableOnSubscribe { emitter ->
        Thread.sleep(5000)
        for (i in 0..10) {
            emitter.onNext(i)
        }
        emitter.onComplete()
    })

    fun hotObservable() = Observable.just("Something")

    fun testNever(): Observable<Unit> = Observable.never<Unit>()

    fun testEmpty(): Observable<Unit> = Observable.empty<Unit>()

    fun testThrow(): Observable<Unit> = Observable.error<Unit>(NullPointerException())

    fun testRange(): Observable<Int> = Observable.range(100, 20)

    fun testInterval(): Observable<Long> = Observable.interval(1, TimeUnit.SECONDS)

    fun testStartWith(): Observable<Int> = observableSource.startWith(-1)
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")
    val compositeDisposable = CompositeDisposable()
    val underTest = ObservableCreator()

    compositeDisposable.add(
        underTest.testRange()
            .subscribe(
                {
                    println("data observed = ${it}, thread_name = ${Thread.currentThread().name}")
                },
                {
                    error -> error.printStackTrace()
                },
                {
                    println("Observable completed")
                }
            )
    )

    compositeDisposable.dispose()
}


