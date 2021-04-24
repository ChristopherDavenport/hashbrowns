package io.chrisdavenport.hashbrowns

import fs2._
import cats.effect._

trait CryptoHash[F[_], A]{
  def algorithm: F[String]
  def cryptoHash(bytes: Array[Byte]): F[CryptoArray[A]]
  def cryptoStreamedHash(stream: Stream[F, Byte])(implicit F: Concurrent[F]): F[CryptoArray[A]]
  def cryptoCheck(l: Array[Byte], r: CryptoArray[A]): F[Boolean]
}