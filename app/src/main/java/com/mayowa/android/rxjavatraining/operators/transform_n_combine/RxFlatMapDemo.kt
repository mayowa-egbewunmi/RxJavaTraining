package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * FlatMap This operator transforms each item emitted by an Observable but
 * instead of returning the modified item, it returns the Observable itself
 * which can emit data again. The important difference between FlatMap and
 * other transformation operators is that the order in which the items are emitted is
 * not maintained.
 *
 * TODO: Demo returning the members in a team as a group
 * TODO: Demo returning each member in the team
 * TODO: Demo how multi threading affects the result of flatMap
 */
class RxFlatMapDemo  {

    fun getTeamsObservable(): Observable<String> {
        val teams = listOf("Mobile", "Platform", "QA")
        return Observable.create<String> {
            for (name in teams) {
                it.onNext(name)
            }
            it.onComplete()
        }
    }

    fun getMembersObservable(team: String): Observable<String> {
        val teamMembers = hashMapOf(
            Pair("Mobile", listOf("Jamie", "George")),
            Pair("Platform", listOf("Julia", "Frank")),
            Pair("QA", listOf("Asim"))
        )
        return Observable.create<String> {
            teamMembers[team]?.let { members ->
                if (team == "Platform") Thread.sleep(1000)
                for (name in members) {
                    it.onNext(name)
                }
            }
            it.onComplete()
        }
    }
}

fun main() {

    val rxFlatMapDemo = RxFlatMapDemo()
    val compositeDisposable = CompositeDisposable()

    val subscription = rxFlatMapDemo.getTeamsObservable()
        .subscribeOn(Schedulers.io())
        .flatMap { team ->
            rxFlatMapDemo.getMembersObservable(team).subscribeOn(Schedulers.io())
        }
        .observeOn(Schedulers.single())
        .subscribe {
            println("data = ${it}, thread_name = ${Thread.currentThread().name}")
        }

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
