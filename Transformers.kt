
package com.damianlattenero.transformers

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction

class MaybeT<A>(val value: Single<Maybe<A>>) {

    companion object {
        fun <A> just(a: A): MaybeT<A> = MaybeT(Single.just(Maybe.just(a)))
        fun <A> none(): MaybeT<A> = MaybeT(Single.just(Maybe.empty()))
        fun <A> lift(fa: Single<A>): MaybeT<A> = MaybeT(fa.map { Maybe.just(it) })
        fun <A> liftMaybe(m: Maybe<A>): MaybeT<A> = MaybeT(Single.just(m))
    }

    fun <B> map(f: (A) -> B): MaybeT<B> =
        MaybeT(value.map { maybe -> maybe.map(f) })

    fun <B> flatMap(f: (A) -> MaybeT<B>): MaybeT<B> =
        MaybeT(value.flatMap { maybe -> maybe.map { f(it).value }.orElse(Single.just(Maybe.empty())) })

    fun <B> zipApWith(ff: MaybeT<(A) -> B>): MaybeT<B> =
        MaybeT(Single.zip(ff.value, this.value, BiFunction { mf, ma -> mf.flatMap { f -> ma.map(f) } }))
}
