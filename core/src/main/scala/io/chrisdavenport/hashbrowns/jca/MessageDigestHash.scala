package io.chrisdavenport.hashbrowns.jca

import io.chrisdavenport.hashbrowns._
import java.security.MessageDigest

import cats.Applicative
import cats.effect._
import cats.syntax.all._
import fs2.Stream

private class MessageDigestHash[F[_]: Applicative, A] private[jca](val alg: String) extends CryptoHash[F, A] {

  private def genInstance = MessageDigest.getInstance(alg)


  def algorithm: F[String] = alg.pure[F]

  def hash(bytes: Array[Byte]): F[CryptoArray[A]] =
    Applicative[F].pure(genInstance.digest(bytes)).map(CryptoArray.declare[A])

  /** In this case, we use the same code as fs2, but we resolve
    * the hash string prefix from the implicit. This is so we can not
    * force the Concurrent bound, and can hash purely in applicative
    * if we do not do streaming hashes.
    */
  def streamedHash(stream: Stream[F, Byte])(implicit F: Concurrent[F]): F[CryptoArray[A]] = 
    stream.compile.foldChunks(genInstance){
      case (digest, chunk) => 
        val bytes = chunk.toArray
        digest.update(bytes, 0, bytes.size)
        digest
    }.map(_.digest()).map(CryptoArray.declare[A])

  def check(l: Array[Byte], r: CryptoArray[A]) = 
    hash(l).map(_.array).map(MessageDigest.isEqual(_, r.array))

}

