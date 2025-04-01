
package com.damianlattenero.core

interface Kind<F, A>
interface Kinded<F> {
    fun <A> Kind<F, A>.fix(): Any
}

interface ApplicativeLike<F> {
    fun <A, B> ap(ff: Kind<F, (A) -> B>, fa: Kind<F, A>): Kind<F, B>
}
