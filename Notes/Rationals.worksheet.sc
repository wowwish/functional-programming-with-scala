class Rational(x: Int, y: Int) {
  require(y > 0, s"denominator must be positive, was ${x}/${y}")
  def this(x: Int) = this(
    x,
    1
  ) // Note: the 'this' on the right hand side calls the primary constructor Rational()
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  def numer = x / gcd(
    x.abs,
    y
  ) // Using x.abs in gcd() to prevent it from returning negative value which will fail the second test case under 'Usage''
  def denom = y / gcd(
    x.abs,
    y
  ) // Using x.abs in gcd() to prevent it from returning negative value which will fail the second test case under 'Usage''
  def add(r: Rational) =
    Rational(numer * r.denom + r.numer * denom, denom * r.denom)
  def mul(r: Rational) =
    Rational(numer * r.numer, denom * r.denom)
  // A method with no input arguments
  def neg = Rational(-numer, denom)
  def sub(r: Rational) = add(r.neg)
  def less(that: Rational): Boolean =
    numer * that.denom < that.numer * denom
  def max(that: Rational): Rational =
    if (this.less(that)) that else this
  override def toString = s"${numer}/${denom}"
} // end class definition

// Usage
val x = Rational(1, 3)
val y = Rational(5, 7)
val z = Rational(3, 2)
x.add(y).mul(z)
x.sub(y).sub(z)
