// def product(f: Int => Int)(a: Int, b: Int): Int =
//   if (a > b) 1 else f(a) * product(f)(a + 1, b)

product(x => x * x)(1, 5)

def fact(n: Int) = product(x => x)(1, n)

fact(5)

// A function to generalize both product and sum
def mapReduce(f: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int) = 
  def recur(a: Int): Int =
    if (a > b) zero
    else combine(f(a), recur(a + 1))
  recur(a)

def sum(f: Int => Int) = mapReduce(f, (x, y) => x + y, 0) 

// Note that the interval points are mentioned only later when the sum() function is actually used.
sum(fact)(1, 5)

def product(f: Int => Int) = mapReduce(f, (x, y) => x * y, 1)

product(identity)(1, 6)