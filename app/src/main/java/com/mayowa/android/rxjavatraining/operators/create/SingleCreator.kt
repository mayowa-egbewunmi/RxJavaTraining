package com.mayowa.android.rxjavatraining.operators.create

import com.mayowa.android.rxjavatraining.utils.DatabaseClient
import com.mayowa.android.rxjavatraining.utils.ProxyUser
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Single is useful from operations where we care about the result of the operation.
 * E.g API request, Saving a record in DB.
 *
 * Single data stream created via the [Function] functional interface
 *
 * The Observer for Single is SingleObserver that implements onSuccess, onError
 * What happens if a null result is returned?
 *
 * TODO: Demonstrate onError implemented and not implemented
 */

fun main() {
    val compositeDisposable = CompositeDisposable()

    val dbClient = DatabaseClient()

    val singleFromCallable: Single<ProxyUser> = Single.fromCallable {
        dbClient.getUser(-1)
    }

//    val singleObserver = object : SingleObserver<ProxyUser> {
//
//        override fun onSuccess(proxyUser: ProxyUser) {
//            println("data observed = ${proxyUser.fullname}, thread_name = ${Thread.currentThread().name}")
//        }
//
//        override fun onSubscribe(disposable: Disposable) {
//            compositeDisposable.add(disposable)
//        }
//    }

    val disposable = singleFromCallable
         .subscribe(Consumer {
             println("data observed = ${it.fullname}, thread_name = ${Thread.currentThread().name}")
         })

    compositeDisposable.add(disposable)
    compositeDisposable.dispose()
}
