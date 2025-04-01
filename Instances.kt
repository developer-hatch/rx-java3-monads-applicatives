
package com.damianlattenero.instances

import com.damianlattenero.core.*
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction

class ForSingle private constructor() {
    companion object
}
typealias SingleOf<A> = Kind<ForSingle, A>

fun <A> SingleOf<A>.fix(): Single<A> = this as Single<A>
fun <A> Single<A>.asKind(): SingleOf<A> = this

object SingleApplicative : ApplicativeLike<ForSingle> {
    override fun <A, B> ap(ff: SingleOf<(A) -> B>, fa: SingleOf<A>): SingleOf<B> =
        Single.zip(ff.fix(), fa.fix(), BiFunction { f, a -> f(a) })
}
