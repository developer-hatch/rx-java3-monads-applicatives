
package com.damianlattenero.dsl

import com.damianlattenero.typeclasses.Monad
import com.damianlattenero.core.Kind
import com.damianlattenero.core.fix

// Aplica una función dentro de un contexto a un valor dentro del mismo contexto en paralelo
infix fun <F, A, B> Kind<F, (A) -> B>.zipApWith(
    value: Kind<F, A>,
    monad: Monad<F>
): Kind<F, B> =
    monad.run {
        this@zipApWith.flatMap { f -> value.map { a -> f(a) } }
    }

// Variante Nullable para zip
infix fun <F, A, B> Kind<F, (A?) -> B>.zipApWithNullable(
    value: Kind<F, A>?,
    monad: Monad<F>
): Kind<F, B> =
    monad.run {
        if (value == null) this@zipApWithNullable.map { it(null) }
        else this@zipApWithNullable.flatMap { f -> value.map { a -> f(a) } }
    }

// Aplica una función dentro de un contexto a un valor dentro del mismo contexto en secuencia (flatMap)
infix fun <F, A, B> Kind<F, (A) -> B>.flatApWith(
    value: Kind<F, A>,
    monad: Monad<F>
): Kind<F, B> =
    monad.run {
        this@flatApWith.flatMap { f -> value.map { a -> f(a) } }
    }

// Variante Nullable para flat
infix fun <F, A, B> Kind<F, (A?) -> B>.flatApWithNullable(
    value: Kind<F, A>?,
    monad: Monad<F>
): Kind<F, B> =
    monad.run {
        if (value == null) this@flatApWithNullable.map { it(null) }
        else this@flatApWithNullable.flatMap { f -> value.map { a -> f(a) } }
    }

// Estilo ergonómico con scope de monad
fun <F> withMonad(monad: Monad<F>, block: Monad<F>.() -> Unit) = monad.block()
