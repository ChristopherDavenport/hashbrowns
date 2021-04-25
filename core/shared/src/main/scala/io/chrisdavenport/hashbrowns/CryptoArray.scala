package io.chrisdavenport.hashbrowns

// Phantom Type For What This Array Was Signed By
class CryptoArray[A] private (val array: Array[Byte]) extends AnyVal
object CryptoArray {
  def declare[A](array: Array[Byte]) = new CryptoArray[A](array)
}