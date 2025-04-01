
package com.damianlattenero.dsl

import com.damianlattenero.instances.*
import com.damianlattenero.core.*
import io.reactivex.rxjava3.core.Single

infix fun <A, B> Single<(A) -> B>.zipApWith(fa: Single<A>): Single<B> =
    SingleApplicative.ap(this.asKind(), fa.asKind()).fix()

infix fun <A, B> Single<(A?) -> B>.zipApWithNullable(fa: Single<A>?): Single<B> =
    fa?.let { Single.zip(this, it, { f, a -> f(a) }) } ?: this.map { it(null) }

infix fun <A, B> Single<(A) -> B>.flatApWith(fa: Single<A>): Single<B> =
    this.flatMap { f -> fa.map(f) }

infix fun <A, B> Single<(A?) -> B>.flatApWithNullable(fa: Single<A>?): Single<B> =
    fa?.let { this.flatMap { f -> it.map { a -> f(a) } } } ?: this.map { it(null) }
