package io.chrisdavenport.hashbrowns.jca

import io.chrisdavenport.hashbrowns._
import java.security.MessageDigest

import cats.Applicative
import cats.effect._
import cats.syntax.all._
import fs2.{Chunk, Pipe, Stream}

private class MessageDigestHash[F[_]: Applicative, A] private[jca](val alg: String) extends RawHash[F] with CryptoHash[F, A] {

  private def genInstance = MessageDigest.getInstance(alg)


  def algorithm: F[String] = alg.pure[F]

  def hash(bytes: Array[Byte]): F[Array[Byte]] =
    Applicative[F].pure(genInstance.digest(bytes))

  /** In this case, we use the same code as fs2, but we resolve
    * the hash string prefix from the implicit. This is so we can not
    * force the Concurrent bound, and can hash purely in applicative
    * if we do not do streaming hashes.
    */
  def streamedHash(stream: Stream[F, Byte])(implicit F: Concurrent[F]): F[Array[Byte]] = 
    stream.compile.foldChunks(genInstance){
      case (digest, chunk) => 
        val bytes = chunk.toArray
        digest.update(bytes, 0, bytes.size)
        digest
    }.map(_.digest())

  def check(l: Array[Byte], r: Array[Byte]) = 
    hash(l).map(MessageDigest.isEqual(_, r))

  def cryptoHash(bytes: Array[Byte]): F[CryptoArray[A]] = hash(bytes).map(CryptoArray.declare[A])
  def cryptoStreamedHash(stream: Stream[F, Byte])(implicit F: Concurrent[F]): F[CryptoArray[A]] = streamedHash(stream).map(CryptoArray.declare[A])
  def cryptoCheck(l: Array[Byte], r: CryptoArray[A]): F[Boolean] = 
    check(l, r.array)

}

