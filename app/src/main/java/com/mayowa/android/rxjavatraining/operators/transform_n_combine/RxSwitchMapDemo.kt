package com.mayowa.android.rxjavatraining.operators.transform_n_combine

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * SwitchMap This operator will unsubscribe to the Observable that was
 * generated from the previously emitted item and begin only mirroring
 * the current one. In other words, it returns the latest Observable and
 * emits the items from it.
 *
 * TODO: Demo how multi threading can affect the result here
 */
class RxSwitchMapDemo  {

    fun getTeamsObservable(): Observable<String> {
        val teams = listOf("Mobile", "QA", "Platform")
        return Observable.create<String> {
            for (name in teams) {
                it.onNext(name)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    fun getMembersObservable(team: String): Observable<List<String>> {
        val teamMembers = hashMapOf(
            Pair("Mobile", listOf("Jamie", "George")),
            Pair("Platform", listOf("Julia", "Frank")),
            Pair("QA", listOf("Asim"))
        )
        return Observable.create<List<String>> {
            if (team == "Platform") Thread.sleep(3000)
            teamMembers[team]?.let { members ->
                it.onNext(members)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }
}

fun main() {

    val rxSwitchMapDemo = RxSwitchMapDemo()
    val compositeDisposable = CompositeDisposable()

    val subscription = rxSwitchMapDemo.getTeamsObservable()
        .subscribeOn(Schedulers.io())
        .switchMap { rxSwitchMapDemo.getMembersObservable(it)
            .subscribeOn(Schedulers.io())}
        .observeOn(Schedulers.single())
        .subscribe {
            println("data = ${it}, thread_name = ${Thread.currentThread().name}")
        }

    compositeDisposable.add(subscription)
    Thread.sleep(5000)
    compositeDisposable.dispose()
}
