package io.chrisdavenport.hashbrowns.jca

import cats.{Applicative, Id}
import cats.effect.Concurrent
import io.chrisdavenport.hashbrowns._

object Algorithms {
  // All The Following Algorithm Objects Will Follow This Structure
  // Would love if I could do this a better way without writing this over and over
  // again. I may just expand this in each of them, because I dislike the existence
  // of this class.
  class AlgExpansion[A](alg: String){
    def make[F[_]: Applicative]: CryptoHash[F, A] = new MessageDigestHash[F, A](alg)
    val id = make[Id]

    def hash(array: Array[Byte]): CryptoArray[A] = {
      id.hash(array)
    }
    def streamedHash[F[_]: Concurrent](stream: fs2.Stream[F, Byte]): F[CryptoArray[A]] = 
      make[F].streamedHash(stream)
  }
  
  sealed trait MD5
  object MD5 extends AlgExpansion[MD5]("MD5")

  sealed trait SHA1
  object SHA1 extends AlgExpansion[SHA1]("SHA-1")

  sealed trait SHA256
  object SHA256 extends AlgExpansion[SHA256]("SHA-256")

  sealed trait SHA512
  object SHA512 extends AlgExpansion[SHA512]("SHA-512")
}