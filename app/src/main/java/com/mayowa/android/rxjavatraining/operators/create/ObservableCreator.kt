package com.mayowa.android.rxjavatraining.operators.create

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream


/**
 * Observable is useful for managing events.
 * E.g Click events from the UI, Location Update, Changes in System properties e.t.c
 *
 * Explain the behaviour of the following observable operators
 *
 * never()
 * empty() { Useful for setting default Observable in base classes }
 * error() { Useful for rethrowing caught exceptions }
 * just() { Useful in an observable chain to continue data flow }
 * interval() { Useful for observing changes at interval }
 * range()
 * startWith() { Useful to returning deferred result before the actual result in an observable }
 *
 */
class ObservableCreator {

    fun testNever(): Observable<Unit> = Observable.never<Unit>()

    fun testEmpty(): Observable<Unit> = Observable.empty<Unit>()

    fun testThrow(): Observable<Unit> = Observable.error<Unit>(NullPointerException())

    fun testRange(): Observable<Int> = Observable.range(100, 20)

    fun testInterval(): Observable<Long> = Observable.interval(1, TimeUnit.SECONDS)

    fun testStartWith(): Observable<Long> = Observable.interval(1, TimeUnit.SECONDS).startWith(-1L)
}

fun main() {
    println("The application is running on thread called = ${Thread.currentThread().name}")
    val compositeDisposable = CompositeDisposable()

    //HOT Observable
//    val source = PublishSubject.create<Int>()
//    compositeDisposable.add(
//        source
//            .hide()
//            .subscribe(
//                {
//                    println("data observed = ${it}, thread_name = ${Thread.currentThread().name}")
//                },
//                { error ->
//                    error.printStackTrace()
//                },
//                {
//                    println("Observable completed")
//                }
//            )
//    )
//    (0..1_000_000).forEach(source::onNext)

    //COLD Observable
    val coldObservable = Observable.create<Int> { emitter ->
        for (i in 0..10) {
            emitter.onNext(i)
        }
        emitter.onComplete()
    }
    compositeDisposable.add(
        coldObservable
            .subscribe(
                {
                    println("data observed = ${it}, thread_name = ${Thread.currentThread().name}")
                },
                { error ->
                    error.printStackTrace()
                },
                {
                    println("Observable completed")
                }
            )
    )
    compositeDisposable.dispose()
}


