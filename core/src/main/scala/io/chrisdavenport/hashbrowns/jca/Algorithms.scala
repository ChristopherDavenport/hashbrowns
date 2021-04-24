package io.chrisdavenport.hashbrowns.jca

import cats.Applicative
import io.chrisdavenport.hashbrowns._

object Algorithms {

  sealed trait MD5
  object MD5 {
    def hash[F[_]: Applicative]: RawHash[F] = new MessageDigestHash[F, MD5]("MD5")
    def crypto[F[_]: Applicative]: CryptoHash[F, MD5] = new MessageDigestHash[F, MD5]("MD5")
  }

  sealed trait SHA1
  object SHA1 {
    def hash[F[_]: Applicative]: RawHash[F] = new MessageDigestHash[F, SHA1]("SHA-1")
    def crypto[F[_]: Applicative]: CryptoHash[F, SHA1] = new MessageDigestHash[F, SHA1]("SHA-1")
  }

  sealed trait SHA256
  object SHA256 {
    def hash[F[_]: Applicative]: RawHash[F] = new MessageDigestHash[F, SHA256]("SHA-256")
    def crypto[F[_]: Applicative]: CryptoHash[F, SHA256] = new MessageDigestHash[F, SHA256]("SHA-256")
  }

  sealed trait SHA512
  object SHA512 {
    def hash[F[_]: Applicative]: RawHash[F] = new MessageDigestHash[F, SHA512]("SHA-512")
    def crypto[F[_]: Applicative]: CryptoHash[F, SHA512] = new MessageDigestHash[F, SHA512]("SHA-512")
  }
}