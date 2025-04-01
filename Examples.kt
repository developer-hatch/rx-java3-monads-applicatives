
package com.damianlattenero.examples

import com.damianlattenero.dsl.*
import com.damianlattenero.transformers.MaybeT
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

data class User(val name: String, val email: String, val age: Int, val address: String)
fun curriedUser(): (String) -> (String) -> (Int) -> (String) -> User =
    { name -> { email -> { age -> { address -> User(name, email, age, address) } } } }

fun fetchName(): Single<String> = Single.just("Damian")
fun fetchEmail(): Single<String> = Single.just("damian@example.com")
fun fetchAge(): Single<Int> = Single.just(33)
fun fetchAddress(): Single<String> = Single.just("Buenos Aires")

fun fetchMaybeName(): Single<Maybe<String>> = Single.just(Maybe.just("Damian"))
fun fetchMaybeEmail(): Single<Maybe<String>> = Single.just(Maybe.just("damian@example.com"))
fun fetchMaybeAge(): Single<Maybe<Int>> = Single.just(Maybe.just(33))
fun fetchMaybeAddress(): Single<Maybe<String>> = Single.just(Maybe.just("Buenos Aires"))

fun buildUserMaybeT(): MaybeT<User> =
    MaybeT.just(::curriedUser)
        .zipApWith(MaybeT(fetchMaybeName()))
        .zipApWith(MaybeT(fetchMaybeEmail()))
        .flatMap { f -> MaybeT(fetchMaybeAge()).map(f) }
        .zipApWith(MaybeT(fetchMaybeAddress()))

fun main() {
    buildUserMaybeT().value.subscribe { maybeUser -> println("Result: ${maybeUser.orElse(null)}") }
}
