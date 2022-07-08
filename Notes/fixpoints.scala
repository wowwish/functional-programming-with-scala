val tolerance = 0.0001

def abs(x: Double): Double = if (x >= 0) x else -x
def isCloseEnough(x: Double, y: Double) =
  abs((x - y) / x) < tolerance

def fixedPoint(f: Double => Double)(firstGuess: Double): Double =
  def iterate(guess: Double): Double = 
    val next = f(guess)
    println(next)
    if (isCloseEnough(guess, next)) next
    else iterate(next)
  iterate(firstGuess)

// def sqrt(x: Double) = fixedPoint{y => x / y}(1.0)
// To prevent the square root estimation from osciallting too much from the actual value, 
// we average successive values of the original sequence. 
def sqrt(x: Double) = fixedPoint(y => (y + x / y) / 2)(1.0)

// The introduction of the averaging of successive values of the sequence leads to a close enough value
// with just four iterations
@main def test =
  sqrt(2)