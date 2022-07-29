abstract class Nat:
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat
  def + (that: Nat): Nat
  def - (that: Nat): Nat
  def * (that: Nat): Nat
  def / (that: Nat): Nat
end Nat

object Zero extends Nat:
  def isZero: Boolean = true
  def predecessor: Nat = ???    // '???' can be used to mark methods that are remain to be implemented
  def successor: Nat = Succ(this)
  def + (that: Nat): Nat = that                                                         // infix operator +
  def - (that: Nat): Nat = if (that.isZero) this else ???                               // infix operator -
  def * (that: Nat): Nat = this                                                         // infix operator *
  def / (that: Nat): Nat = Zero                                                         // infix operator /
  override def toString = "Zero" // Create a custom toSting by overriding the default toString
end Zero

class Succ(n: Nat) extends Nat:
  def isZero: Boolean = false
  def predecessor: Nat = n
  def successor: Nat = Succ(this)
  def + (that: Nat): Nat = Succ(n + that)                                               // infix operator +
  def - (that: Nat): Nat = if (that.isZero) this else n - that.predecessor              // infix operator -
  def * (that: Nat): Nat = if (that.isZero) Zero else Succ(n) + (Succ(n) * that.predecessor)  // infix operator *
  def / (that: Nat): Nat =                                                                  
    if (that.isZero) ??? else Succ(Zero) + ((Succ(n) - that) / that)   
  override def toString = s"Succ(${n})" // Create a custom toSting by overriding the default toString
end Succ
// This is an Implementation of Peano Numbers - a simple way of representing the natural numbers 
// using only a zero value and a successor function


// Testing
val two = Succ(Succ(Zero))
val one = Succ(Zero)
val three = Succ(Succ(Succ(Zero)))
val four = Succ(Succ(Succ(Succ(Zero))))
two + one
two - one
two - two
// NotImplementedError for the test below
// one - two
three * three
three / one
four / two