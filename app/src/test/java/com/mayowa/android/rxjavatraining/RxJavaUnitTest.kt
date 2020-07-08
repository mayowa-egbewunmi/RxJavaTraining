package com.mayowa.android.rxjavatraining

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.concurrent.TimeUnit


class RxJavaUnitTest {

    @Test
    fun `traditional test observable result from iterable`() {

        val letters: List<String> = listOf("A", "B", "C", "D", "E")
        val results = mutableListOf<String>()

        val observable: Observable<String> = Observable
            .fromIterable(letters)
            .zipWith(Observable.range(1, Int.MAX_VALUE),
                BiFunction { string, index -> "$index-$string" })

        observable.subscribe {
            results.add(it)
        }

        assertThat(results, notNullValue())
        assertThat(results.size, `is`(5))
        assertThat(results, hasItems("1-A", "2-B", "3-C", "4-D", "5-E"))
    }

    @Test
    fun `test observable result from iterable with TestObserver`() {

        val letters: List<String> = listOf("A", "B", "C", "D", "E")
        val subscriber = TestObserver<String>()

        val observable: Observable<String> = Observable
            .fromIterable(letters)
            .zipWith(Observable.range(1, Int.MAX_VALUE),
                BiFunction { string, index -> "$index-$string" })

        observable.subscribe(subscriber)

        subscriber.assertSubscribed()
        subscriber.assertNoErrors()
        subscriber.assertValueCount(5)
        subscriber.assertValues("1-A", "2-B", "3-C", "4-D", "5-E")
    }

    @Test
    fun `test exception`() {
        val subscriber = TestObserver<String>()

        val observable: Observable<String> = Observable.error(java.lang.RuntimeException())

        observable.subscribe(subscriber)

        subscriber.assertError(RuntimeException::class.java)
        subscriber.assertNotComplete()
    }

    @Test
    fun `test time advance`() {
        val letters: List<String> = listOf("A", "B", "C", "D", "E")
        val scheduler = TestScheduler()
        val subscriber = TestObserver<String>()
        val tick =
            Observable.interval(1, TimeUnit.SECONDS, scheduler)

        val observable: Observable<String> =
            Observable.fromIterable(letters)
                .zipWith(tick,
                    BiFunction { string, index -> "$index-$string" })

        observable.subscribeOn(scheduler)
            .subscribe(subscriber)

        subscriber.assertNoValues();
        subscriber.assertNotComplete();

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValues("0-A");
    }
}
