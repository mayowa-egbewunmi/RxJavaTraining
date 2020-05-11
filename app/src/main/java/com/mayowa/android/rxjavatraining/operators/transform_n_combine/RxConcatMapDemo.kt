package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 * ConcatMap This operator functions the same way as flatMap(),
 * the difference being in concatMap() the order in which items are emitted
 * are maintained. One disadvantage of concatMap() is that it waits for
 * each observable to finish all the work until next one is processed.
 *
 *
 * TODO: Demo that order is always maintained irrespective of multi threading.
 */
class RxConcatMapDemo  {

    fun getTeamsObservable(): Observable<String> {
        val teams = listOf("Mobile", "Platform", "QA")
        return Observable.create<String> {
            for (name in teams) {
                it.onNext(name)
            }
            it.onComplete()
        }
    }

    fun getMembersObservable(team: String): Observable<List<String>> {
        val teamMembers = hashMapOf(
            Pair("Mobile", listOf("Jamie", "George")),
            Pair("Platform", listOf("Julia", "Frank")),
            Pair("QA", listOf("Asim"))
        )
        return Observable.create<List<String>> {
            teamMembers[team]?.let { members ->
                if (team == "Platform") Thread.sleep(1000)
                it.onNext(members)
            }
            it.onComplete()
        }
    }
}

fun main() {

    val rxConcatMapDemo = RxConcatMapDemo()
    val compositeDisposable = CompositeDisposable()

    val subscription = rxConcatMapDemo.getTeamsObservable()
        .concatMap { rxConcatMapDemo.getMembersObservable(it) }
        .subscribe {
            println("data = ${it}, thread_name = ${Thread.currentThread().name}")
        }

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
