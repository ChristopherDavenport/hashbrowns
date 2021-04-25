package io.chrisdavenport.hashbrowns

import fs2._
import cats.effect._

trait CryptoHash[F[_], A]{
  def algorithm: F[String]
  def hash(bytes: Array[Byte]): F[CryptoArray[A]]
  def streamedHash(stream: Stream[F, Byte])(implicit F: Concurrent[F]): F[CryptoArray[A]]
  def check(l: Array[Byte], r: CryptoArray[A]): F[Boolean]
}