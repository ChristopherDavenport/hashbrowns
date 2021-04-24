package io.chrisdavenport.hashbrowns

import fs2._
import cats.effect._
// Raw Hash - Like Message Digest but 
trait RawHash[F[_]]{
  def algorithm: F[String]
  def hash(bytes: Array[Byte]): F[Array[Byte]]
  def streamedHash(stream: Stream[F, Byte])(implicit F: Concurrent[F]): F[Array[Byte]]
  def check(l: Array[Byte], r: Array[Byte]): F[Boolean]
}
