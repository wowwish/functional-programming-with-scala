# Higher Order Functions

Functional languages treat functions as _first-class_ values. This means that like any other value, a function can be passed as a parameter and returned as a result. This provides a flexible way to compose programs. Functions that take other functions as parameters or that return functions as results are called __higher order functions__.

Example:

Take the sum of integers between `a` and `b`:
```
def sumInts(a: Int, b: Int): Int =
  if (a > b) 0 else a + sumInts(a + 1, b)
```

Take the sum of cubes of all the integers between `a` and `b`:
```
def cube(x: Int): Int = x * x * x
def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)
```

Taking sum of factorials of all integers between `a` and `b`:
```
def fact(x: Int): Int = 
  if (x == 0) 1 else x * fact(x - 1)
def sumFactorial(a: Int, b: Int): Int =
  if (a > b) 0 else fact(a) + sumFactorial(a + 1, b)
```

The examples above are all special cases of $\sum_{n = a}^{b} f(n)$ for different values of f. We can factor out this common pattern. Example:
```
def sum(f: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else f(a) + sum(f, a + 1, b)
```
Here, `f` is a function that is passed in as a parameter. It takes an integer as argument and also returns an integer as indicated by `Int => Int` (The type A => B is the type of a function that takes an argument of type A and returns a result of type B). So, we can now write:
```
def id(x: Int): Int = x
def sumInts(a: Int, b: Int) = sum(id, a, b)
def cube(x: Int): Int = x * x * x
def sumCubes(a: Int, b: Int): Int = sum(cube, a, b)
def fact(x: Int): Int = if (x == 0) 1 else x * fact(x - 1)
def sumFactorials(a: Int, b: Int): Int = sum(fact, a, b)
```

Passing functions as parameters leads to the creation of many small functions. It can get tedious sometimes, to define and name these functions using def. Just like we can directly write `println("abc")` instead of `def str = "abc"; println(str)`, treating the string "abc" as a _literal_, we would like to also have _function literals_ that can be used as a function without giving it a name. These are called as __anonymous functions__.

Example: 

An _anonymous function_ that raises its argument to a cube:
```
(x: Int) => x * x * x
```
Here, `(x: Int)` is the _parameter_ of the function and `x * x * x` is its _body_. The type of the parameter can be omitted if it can be inferred by the compiler from the context.
If there are several parameters, they are seperated by commas.
Example:
```
(x: Int, y: Int) => x + y
```

Anonymous Functions are convenient but not Essential. They are considered as _syntactic sugar_. We can express an anonymous function `(x1: T1, ..., xn: Tn) => E` using `def` as follows:
```
{ def f(x1: T1, ..., xn: Tn) = E; f }
``` 
where `f` is an arbitary, fresh name (that is not yet used in the program.)

Using anonymous functions, we can write the _sums_ we saw above in a shorter way:
```
def sum(f: Int => Int, a: Int, b: Int): Int =
  if (a > b) 0
  else f(a) + sum(f, a + 1, b)
def sumInts(a: Int, b: Int): Int = sum(x => x, a, b)
def sumCubes(a: Int, b: Int): Int = sum(x => x * x * x, a, b)
```
Here, we have not defined the parameter types for the anonymous functions because the compiler can infer that the anonymous functions take an Int parameter and maps it to an Int result from our definition of the `sum` function.

The _sum_ function uses _linear_ recursion - The stack grows as the _sum_ function sums up its operands [f(a) + sum(f, a + 1, b) -->> f(a) + f(a + 1) + f(a + 2) + sum(f, a + 3, b)]. We can write a _tail recursive_ version of the _sum_ function:
```
def sum(f: Int => Int, a: Int, b: Int): Int =
  def loop(a: Int, acc: Int): Int = 
    if (a > b) acc
    else loop(a + 1, acc + f(a))
  loop(a, 0)
```

# Currying

Note from the previous section that in the _sum_ function calls, the `a` and `b` parameters are passed unchanged to the _sum_ function from _sumInts_ and _sumCubes_. We can make the _sum_ function even shorter by eliminating this repetition:
```
def sum(f: Int => Int): (Int, Int) => Int =
  def sumF(a: Int, b: Int): Int = 
    if (a > b) 0
    else f(a) + sumF(a + 1, b)
  sumF
```
Here, _sum_ is a function that returns another function. The returned function _sumF_ applies the given function parameter `f` and sums the results. It takes two integer parameters and return an integer result, as indicated in the return type of _sum_ as `(Int, Int) => Int`.

We can then define:
```
def sumInts = sum(x => x)
def sumCubes = sum(x => x * x * x)
def sumFactorials = sum(fact)
```
These function can in-turn be applied like any other function:
```
sumCubes(1, 10) + sumFactorials(10, 20)
```

We can also avoid the middlemen functions _sumInts_ and _sumCubes_ by calling _sum_ like this:
```
sum (cube) (1, 10)
```
Here, `sum (cube)` applies the _sum_ function to _cube_ function to create the _sum of cubes_ function. `sum (cube)` is therefore equivalent to _sumCubes_. This function is next applied to the arguments `(1, 10)`.
Generally, function application associates to the left, ie,

    sum(cube)(1, 10)      ==      (sum (cube)) (1, 10) 

The definition of functions that return functions, is so useful in functional programming that there is a special syntax for it in Scala. For example, the following definition of _sum_ is equivalent to the one with the nested _sumF_ function, but shorter: 
```
def sum(f: Int => Int)(a: Int, b: Int): Int = 
  if (a > b) 0 else f(a) + sum(a + 1, b)
``` 

In general, a definition of a function with multiple parameter lists

    def f(ps1)...(psn) = E

where `n` > 1, is equivalent to

    def f(ps1)...(psn-1) = { def g(psn) = E; g}   <----- Anonymous Function

Where, `g` is a fresh identifier. The last parameter of the function becomes the nested function. Or for short:

    def f(ps1)...(psn-1) = (psn => E)

By repeating the process _n_ times, 

    def f(ps1)..(psn-1)(psn) = E

is shown to be equivalent to

    def f = (ps1 => (ps2 => ...(psn => E)...))

This stype of function definition and function application is known as __currying__, named for its instigator, Haskell Brooks Curry (1900 - 1982).

Given:
```
def sum(f: Int => Int)(a: Int, b: Int): Int = ...
```
The type of this _sum_ function will be 

    (Int => Int) => ((Int, Int) => Int) 

Note the functional types associated to the right. That is to say that 

    Int => Int => Int

is equivalent to 

    Int => (Int => Int)


Write a _product_ function that calculates the product of the values of a function for the points on a given interval. Write _factorial_ in terms of this _product_ function. Can you write a more general function, which generalizes both _sum_ and _product_ ?
[product](product.worksheet.sc)


A number `x` is called as the __fixed point__ of a function `f` if

    f(x) = x

For some functions `f`, we can locate the fixed points by starting with an initial estimate and then by applying `f` in a repetitive way

    x, f(x), f(f(x)), f(f(f(x))), ...

until the value does not vary anymore (or the change is sufficiently small).

This leads to the following function for finding a fixed point:
```
val tolerance = 0.0001

def isCloseEnough(x: Double, y: Double) =
  abs((x - y) / x) < tolerance

def fixedPoint(f: Double => Double)(firstGuess: Double): Double =
  def iterate(guess: Double): Double = 
    val next = f(guess)
    if (isCloseEnough(guess, x)) next
    else iterate(next)
  iterate(firstGuess)
```

Here is a specification of the _sqrt_ function:

    sqrt(x) = the number _y_ such that y * y = x

Or, by dividing both sides of the equation with _y_

    sqrt(x) = the number _y_ such that y = x / y

Consequently, sqrt(x) is a fixed point of the function (y => x / y)
    
    f(sqrt(x)) = x / sqrt(x) = sqrt(x)

This suggests to calculate sqrt(x) by iteration towards a fixed point using the _fixedPoint_ function declared above:
```
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

def sqrt(x: Double) =
  fixedPoint(y => x / y)(1.0)
```


Unfortunately, this does not converge. We can follow the current value of _guess_ by adding a _println_ statement to the _fixedPoint_ function. To control such oscillations due to variation in the estimation, we _average_ successive values of the original sequence. The _sqrt_ function above can hence be rewritten as:
```
def sqrt(x: Double) =
  fixedPoint(y => (y + x / y) / 2)(1.0)
```

Now, the _fixedPoint_ funtion expands to the _sqrt_ function that we developed in [__Week 1__](Week1.md).
The _sqrt_ function defined here now converges to a close enough value with just four iterations.


The Expressive power of a language is greatly increased if we can pass functions as arguments to other functions.
Functions that return functions can also be very useful. Consider the iteration towards a fixed point:
  * We begin by observing that $\sqrt{x}$ is a fixed point of the function y => x / y.
  * Then, the iteration converges by averaging successive values.
  * This technique of _stabilizing_ by _averaging_ is general enough to merit being abstracted into its own function: 
```
def averageDamp(f: Double => Double)(x: Double):Double =
  (x + f(x)) / 2
```

Lets rewrite the _sqrt_ function using _fixedPoint_ and _averageDamp_:
```
val tolerance = 0.0001
def abs(x: Double): Double = if (x >= 0) x else -x
def isCloseEnough(x: Double, y: Double) = abs((x - y) / x) < tolerance
def fixedPoint(f: Double => Double)(firstGuess: Double): Double = {
  def iterate(guess: Double): Double = 
    val next = f(guess)
    println(next)
    if (isCloseEnough(guess, next)) next
    else iterate(next)
  iterate(firstGuess)
}
def averageDamp(f: Double => Double)(x: Double):Double = (x + f(x)) / 2  
def sqrt(x: Double): Double = fixedPoint(averageDamp(y => x / y))(1.0)
``` 

As a Programmer, one must look for opportunities to _abstract_ and _reuse_ code. The highest level of abstraction is not always the best, but it is important to know the techniques of abstraction, so as to use them when appropriate.


# Language Elements Seen So Far

We have seen language elements to express types, expressions and definitions
Below, we give their context-free syntax in Extended Backus-Naur Form (EBNF), where:
  * __|__ denotes an alternative
  * __\[...\]__ denotes an option (0 or 1)
  * __{...}__ denotes a repetition (0 or more)
  * __ident__ denotes an identifier
  * __~__ denotes one's complement
  * __Def__ denotes a definition such as `val x = 1`.
  * __indent__ and __outdent__ markers denote the indentation (the next line is to the right of the _indent_ marker, and to the left of the _outdent_ marker)


Type            =   SimpleType | FunctionType
FunctionType    =   SimpleType '=>' Type | '(' \[Types\] ')' '=>' Type
SimpleType      =   ident
Types           =   Type {',' Type}


A __type__ can be:
  * A __numeric type__: Int, Double (and Byte, Short, Char, Long, Float).
  * The __Boolean__ type with the values _true_ and _false_
  * The __String__ type
  * A __function type__, like Int => Int, (Int, Int) => Int etc.


__Expressions__:

Expr            =   InfixExpr | FunctionExpr | if Expr (Expr) else Expr 
InfixExpr       =   PrefixExpr | InfixExpr Operator InfixExpr
Operator        =   ident
PrefixExpr      =   \['+' | '-' | '\!' | '~'\] SimpleExpr
SimpleExpr      =   ident | literal | SimpleExpr '.' ident | Block
FunctionExpr    =   Bindings '=>' Expr
Bindings        =   ident | '(' \[Binding {'.' Binding}\] ')'
Binding         =   ident \[':' Type\]
Block           =   '{' {Def ';'} Expr '}' | &lt;ident&gt; {Def ';'} Expr &lt;outdent&gt;


An __Expression__ can be:
  * An __identifier__ such as `x`, `isGoodEnough`
  * A __literal__, like `0`, `1.0` or `"abc"`
  * A __function application__, like `sqrt(x)`
  * An __operator application__, like `-x`, `y + x`
  * A __selection__, like `math.abs`
  * A __conditional expression__, like `if (x < 0) -x else x`
  * A __block__, like `{ val x = math.abs(y) ; x * 2 }`
  * An __anonymous function__, like `x => x + 1`


__Definitions__:

Def           =   FunDef | ValDef
FunDef        =   def ident {'(' \[Parameters\] ')'} \[':' Type\] '=' Expr
ValDef        =   val ident \[':' Type\] '=' Expr
Parameter     =   ident ':' \[ '=>' \] Type
Parameters    =   Parameter {',' Parameter}


A __definition__ can be:
  * A __function definition__, like `def square(x: Int) = x * x` 
  * A __value definition__, like `val y = square(2)`

A __parameter__ can be:
  * A __call-by-value parameter__, like `(x: Int)`
  * A __call-by-name parameter__, like `(y: => Double)` 



# Function and Data

In this section, we'll learn how functions create and encapsulate data structures.

__Example:__ Rational Numbers

We want to design a package for doing rational arithmetic.
A rational number $\frac{x}{y}$ is represented by two integers:
  * Its _numerator_ `x` and
  * Its _denominator_ `y`

Suppose we want to implement the addition of two rational numbers:
```
def addRationalNumerator(n1: Int, d1: Int, n2: Int, d2: Int): Int
def addRationalDenominator(n1: Int, d1: Int, n2: Int, d2: Int): Int
```
It would be difficult to manage all these numerators and denominators

A better choice is to combine the numerator and denominator of a rational number into a data structure.

In Scala, we do this by defining a __Class__:
```
class Rational(x: Int, y: Int):
  def numer = x
  def denom = y
```
This definition introduces two entities:
  * A new __type__, named `Rational`
  * A __constructor__ `Rational` to create elements of this type.

Scala keeps the names of types, and values (like constructors) in __different namespace__. So there's no conflict between two entities named `Rational`.

# Objects

We call the elements of _Class_ type __Objects__. 
We create an _object_ by calling the _constructor_ of the _Class_.
Example:
```
class Rational(x: Int, y: Int):
  def numer = x
  def denom = y
Rational(1, 2)
```

Objects of the class _Rational_ have two __members__, _numer_ and _denom_.
We select members of an object with the _infix operator_ '__.__'
Example:
```
class Rational(x: Int, y: Int):
  def numer = x
  def denom = y
val x = Rational(1, 2)    //  val x: Rational = Rational@4b87074a
x.numer     //  1
x.denom     //  2
```

We can now define rational arithmetic functions that implement the standard rules:

  $\frac{n_1}{d_1} + \frac{n_2}{d_2} = \frac{n_1d_2 + n_2d_1}{d_1d_2}$

  $\frac{n_1}{d_1} - \frac{n_2}{d_2} = \frac{n_1d_2 - n_2d_1}{d_1d_2}$

  $\frac{n_1}{d_1} . \frac{n_2}{d_2} = \frac{n_1n_2}{d_1d_2}$

  $\frac{n_1}{d_1} / \frac{n_2}{d_2} = \frac{n_1d_2}{d_1n_2}$

  $\frac{n_1}{d_1} = \frac{n_2}{d_2}$  iff  $n_1d_2 = d_1n_2$


Implementing Rational Arithmetic:
```
def addRational(r: Rational, s: Rational): Rational = 
  Rational(
    r.numer * s.denom + s.numer * r.denom, r.denom * s.denom
  )
def makeString(r: Rational): String =
  s"${r.numer}/${r.denom}"

makeString(addRational(Rational(1, 2), Rational(2, 3)))     // 7/6
```
__Note:__ `s"..."` in _makeString_ is an __interpolated string__, with values `r.numer` and `r.denom` in the places enclosed by `${...}`


One can go further and also package functions operating on a data abstraction (Class) in the data abstraction itself. Such functions are called __methods__.

Example:
Rational numbers now would have in addition to the functions numer and denom, the functions _add_, _sub_, _multiply_, _equal_ and _toString_. Here's a possible implementation:
```
class Rational(x: Int, y: Int) {
  def numer = x
  def denom = y
  def add(r: Rational) =
    Rational(numer * r.denom + r.numer * denom, denom * r.denom)
  def mul(r: Rational) =
    Rational(numer * r.numer, denom * r.denom)
  def sub(r: Rational) =
    Rational(numer * r.denom - r.numer * denom, denom * r.denom)
  override def toString = s"${numer}/${denom}"
}    // end class definition
```
__Remark:__ the modifier `override` declares that _toString_ redefines a method that already exists (in the class `java.lang.Object`). Every class in Scala already has an in-built definition of the _toString_ method which may need customization for your use cases using the `override` modifier. Using the methods defined above, you can add one rational number to another for example, using `x.add(y)`, where `x` and `y` are objects created for the `Rational` class and `.add()` is the method defined within the `Rational` class. Also note that `numer` and `denom` are class _members_ initialized in the current `Rational` object. 

Here is how one might use the new [Rational abstraction](Rationals.worksheet.sc)



# Data Abstraction


The previous example has shown that rational numbers are'nt always represented in their simplest form. One would expect the rational numbers to be _simplified_, ie, reduced to their smallest numerator and denominator by dividing both with a divisor.

We could implement this in each rational operation, but it would be easy to forget this division in an operation. A better alternative consists of simplifying the representation in the class when the objects are constructed.

__Rationals with Data Abstraction:__

In this example, we calculate the _gcd_ (_greatest common divisor_) immediately, so that its value can be re-used in the calculations of _numer_ and _denom_.
```
class Rational(x: Int, y: Int) {
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b,  a % b)
  private val g = gcd(x, y)
  def numer = x / g
  def denom = y / g
  def add(r: Rational) =
    Rational(numer * r.denom + r.numer * denom, denom * r.denom)
  def mul(r: Rational) =
    Rational(numer * r.numer, denom * r.denom)
  def sub(r: Rational) =
    Rational(numer * r.denom - r.numer * denom, denom * r.denom)
  override def toString = s"${numer}/${denom}"
}
```
Here, `gcd` and `g` are __private__ members, we can only access them from inside the _Rational_ class.

It is also possible to call _gcd_ in the code of _numer_ and _denom_:
```
class Rational(x: Int, y: Int) {
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b,  a % b)
  def numer = x / gcd(x, y)
  def denom = y / gcd(x, y)
}
```
This can be advantageous if it is expected that the functions _numer_ and _denom_ are called infrequently. We can also define _numer_ and _denom_ using `val`. This will be advantageous if they are called often as in the normal case:
```
class Rational(x: Int, y: Int) {
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b,  a % b)
  val numer = x / gcd(x, y)
  val denom = y / gcd(x, y)
}
```

__The Client's View:__

Clients of the class Rational observe exactly the same behaviour in each case. This ability to choose different implementations of data without affecting clients is called __data abstraction__. It is a cornerstone of software engineering as  it allows you to adapt and refine the implementation without affecting the clients.


__Self Reference:__

Inside a class, the name `this` represents the object on which the current method is executed.

Example:
The _less_ function compares two rational numbers and returns a boolean if the first _Rational_ is less than the second.
  
  $\frac{x_1}{y_1} < \frac{x_2}{y_2}$ is equivalent to $x_1y_2 < x_2y_1$ 

The _max_ function returns the maximum of two rational numbers.

Adding the functions _less_ and _max_ to the class _Rational_:
```
class Rational(x: Int, y: Int) {
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b,  a % b)
  val numer = x / gcd(x, y)
  val denom = y / gcd(x, y)
  def add(r: Rational) =
    Rational(numer * r.denom + r.numer * denom, denom * r.denom)
  def mul(r: Rational) =
    Rational(numer * r.numer, denom * r.denom)
  def sub(r: Rational) =
    Rational(numer * r.denom - r.numer * denom, denom * r.denom)
  def less(that: Rational): Boolean =
    numer * that.denom < that.numer * denom
  def max(that: Rational): Rational =
    if (this.less(that)) that else this
  override def toString = s"${numer}/${denom}"
}
```

Note that a simple name `m`, which refers to another member of the class, is an abbreviation of `this.m`. Thus, an equivalent way to formulate _less_ is as follows:
```
def less(that: Rational): Rational =
  this.numer * that.denom < that.numer * this.denom
```


# Preconditions

Let's say our _Rational_ class requires that the denominator is positive. We can enforce this by calling the `require` function:
```
class Rational(x: Int, y: Int) {
  require(y > 0, "denominator must be positive")
}
```

`require` is a _predefined_ function. It takes a condition and an optional message string. If the condition passed to _require_ is _false_, an _IllegalArgumentException_ is thrown with the given message string.

Besides _require_, there is also `assert` which also takes a condition and an optional message string as parameters.
Example:
```
def sqrt(x: Double): Double = {
  def abs(y: Double) = if (y >= 0) y else -y
  def sqrtIter(guess: Double): Double =
    if (isGoodEnough(guess)) guess
    else sqrtIter(improve(guess))
  def improve(guess: Double): Double =
    (guess + x / guess) / 2
  def isGoodEnough(guess: Double): Boolean =
    abs(guess * guess - x) < 0.001 * x
  sqrtIter(1.0)
}
val x = sqrt(2)
assert(x <= 0)
```

Like _require_, a failing `assert` will also throw an exception, but it's a different one: _AssertionError_ for _assert_, _IllegalArgumentException_ for _require_. This reflects a difference in intent
  * _require_ is used to enforce a precondition on the caller of a function. If _require_ goes wrong, the fault is with the code that called the function.
  * _assert_ is used to check the code of the function itself. If _assert_ is wrong, then essentially, we have a bug in the implementation of the function, even though it was called with the correct arguments.


# Constructor

In Scala, a class implicitly introduces a constructor to construct the elements of the class. This implicit constructor is called the __primary constructor__ of the class.
The `primary constructor`
  * takes the parameters of the class
  * and executes all statements in the class body (such as the _require_ or the value definitions of _numer_ and _denom_ or any other statements of the class body)

Scala also allows the declaration of __auxiliary constructors__. These are _methods_ named `this`.
Example: 
Adding an auxiliary constructor to the class _Rational_:
```
class Rational(x: Int, y: Int) {
  def numer = x
  def denom = y
  def this(x: Int) = this(x, 1) // Note: the 'this' on the right hand side calls the primary constructor Rational()
  override def toString = s"${numer}/${denom}"
}
Rational(2) // returns 2/1
```


# The End Marker

Indentation-based syntax has many advantages over other conventions. But one possible problem is that with longer lists of definitions and deep nesting, is is sometimes hard to see where a _class_ or other _construct_ ends.
`end` markers are a tool to make this explicit.
Example:
```
class Rational(x: Int, y: Int):
  def this(x: Int) = this(x, 1)


end Rational // end class definition
```

  * An `end` marker is followed by the name that's defined in the definition that ends at this point
  * It must align with the opening keyword (_class_ in this case)

End markers are also allowed for other _constructs_:
```
def sqrt(x: Double): Double =
  def abs(y: Double) = if (y >= 0) y else -y
  def sqrtIter(guess: Double): Double =
    if (isGoodEnough(guess)) guess
    else sqrtIter(improve(guess))
  def improve(guess: Double): Double =
    (guess + x / guess) / 2
  def isGoodEnough(guess: Double): Boolean =
    abs(guess * guess - x) < 0.001 * x
  sqrtIter(1.0)
end sqrt

val x = sqrt(2)

if (x > 0)
  "it is positive"
else
  "it is negative"
end if
```
If the `end` marker terminates a control expression such as `if`, the begining keyword is repeated.

__Exercise__:

Modify the _Rational_ class so that rational numbers are kept unsimplified internally, but the simplification is applied when numbers are converted to strings.
```
class Rational(x: Int, y: Int):
  require(y > 0, "denominator must be positive")
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  val numer = x
  val denom = y
  def add(r: Rational) =
    Rational(numer * r.denom + r.numer * denom, denom * r.denom)
  def mul(r: Rational) =
    Rational(numer * r.numer, denom * r.denom)
  def neg = Rational(-numer, denom)
  def sub(r: Rational) = add(r.neg)
  def less(that: Rational): Boolean =
    numer * that.denom < that.numer * denom
  def max(that: Rational): Rational =
    if (this.less(that)) that else this
  override def toString = s"${numer / gcd(x.abs, y)}/${denom / gcd(x.abs, y)}"
```
Do clients observe the same behaviour when interacting with the _Rational_ class ? 
Yes for small sizes of denominators and numerators and small number of operations - because if kept unsimplified in the class body, _numer_ and _denom_ can get increasingly larger, until they don't fit into the range of an `Int` type anymore



# Evaluations and Operators

We previously defined the meaning of a function application using a __computation model based on substitution__. Now we extend this model to _classes_ and _objects_. In the instantiation of a class `C(e1, ..., em)`, the expression arguments `e1, ..., em` are evaluated like the arguments of a normal function. That's it. The resulting expression,say, `C(v1, ..., vm)`, is already a value. 

Suppose that we have a class definition

    class C(x1, ..., xm) {... def f(y1, ..., yn) = b ...}

Where
  * The formal parameters of the class are `x1, ..., xm`
  * The class defines a method `f` with formal parameters `y1, ..., ym`

(The list of function parameters can be absent. For simplicity, we have omitted the parameter type.)

Then, the following expression

  C(v1, ..., vm).f(w1, ..., wn)

is evaluated as

  \[w1/y1, ..., wn/yn\]\[v1/x1, ..., vm/xm\]\[C(v1, ..., vm)/this\]b

There are three substitutions at work here:
  * The substitution of the formal parameters `y1, ..., yn` of the function `f` by the arguments `w1, ..., wn`
  * The substitution of the formal parameters `x1, ..., xm` of the class `C` by the class arguments `v1, ..., vm`
  * The substitution of the self reference `this` by the value of the object `C(v1, ..., vm)` in the class body `b`

__Object Rewriting Examples:__

        Rational(1, 2).numer
  -->   \[1/x, 2/y\][][Rational(1, 2)/this]x
  =     1


        Rational(1, 2).less(Rational(2, 3))
  -->   \[1/x, 2/y\][Rational(2, 3)/that][Rational(1, 2)/this]
            this.numer * that.denom < that.numer * this.denom
  =     Rational(1, 2).numer * Rational(2, 3).denom < Rational(2, 3).numer * Rational(1, 2).denom
  -->>  1 * 3 < 2 * 2
  -->>  true



# Extension Methods

Having to define all _methods_ that belong to a class inside the class itself can lead to very large classes, and is not very modular. Methods that do not need to access the internals of a class can alternatively be defined as __extension methods__. For instance, we can add _min_ and _abs_ methods to class _Rational_ like this:
```
extension (r: Rational)
  def min(s: Rational): Rational = if(s.less(r)) s else r
  def abs: Rational = Rational(r.numer.abs, r.denom)
```
The advantage of _extension methods_ is that you can define them anywhere - together with the class, or in a different module, different people can define different extensions of the same class without stepping over each other's source code. 

Extensions of a class are visible if they are listed in the companion object of a class, or if they are defined or importer in the current scope. _Members_ of a visible extension of a class `C` can be called as if they were _members_ of `C`. Example:
```
Rational(1, 2).min(Rational(2, 3))
```
__Caveats:__
  * Extensions can only add mew _members_/_methods_, not override existing ones
  * Extensions cannot refer to other class _members_ via `this` as they donot have priviledged access to the class.

_Extension method_ substitution works like normal _method_ substitution, but
  * instead of `this` it's the extension parameter that gets substituted
  * class parameters are not visible, so do not need to be substituted at all

Example:
  
        Rational(1, 2).min(Rational(2, 3))
  -->   \[Rational(1, 2)/r\]\[Rational(2, 3)/s\] if (x.less(r)) s else r
  =     if(Rational(2, 3).less(Rational(1, 2))) Rational(2, 3) else Rational(1, 2)


In principle, the rational numbers defined by _Rational_ are as natural as integers. But for the user of these abstractions, there is a noticeable difference:
  * We write `x + y`, if `x` and `y` are integers, but
  * We write `r.add(s)` if `r` and `s` are rational numbers

In Scala, we can eliminate this difference. We proceed in two steps:

__Step1: Relaxed Identifiers:__
In Scala, operators such as `+` or `<` count as _identifiers_. Thus, an _identifier_ can be
  * `Alphanumeric:` starting with a letter, followed by a sequence of letters or numbers
  * `Symbolic:` starting with an operator symbol, followed by other operator symbols
  * The underscore character `'_'` counts as a letter
  * Alphanumeric identifiers can also end in an underscore, followed by some operator symbols.

Examples of identifiers: x1, *, +?%&, vector_++, counter_=

Since operators are identifiers, it is possible to use them as _method_ names. These are called __operator methods__.
Example:
```
extension(x: Rational)
  def + (y: Rational): Rational = x.add(y)
  def * (y: Rational): Rational = x.mul(y)
```
This allows rational numbers to be used like _Int_ or _Double_:
```
val x = Rational(1, 2)
val y = Rational(1, 3)
x * x + y * y
```

__Step2: Infix Notation:__
An _operator method_ with a single parameter can be used as an __infix operator__ where you can call it without the period after the instance and without parentheses around the argument. An alphanumeric _method_ with a single parameter can also be used as an infix operator if it is declared with an `infix` modifier. Example:
```
extension(x: Rational)
  infix def min(that: Rational): Rational = ...
```
It is therefore possible to write `r min s` instead of `r.min(s)` when calling the _min_ function.



# Operator Precedence

The __precedence__ of an operator is determined by its first character. The following table lists the characters in `increasing order` of priority precedence:
  (all letters)
  |                                                                                 (lowest precedence)
  ^
  &
  <>
  =!
  :                                                                                               
  +-
  */%                                                                                (highest precedence)
  (all other special characters)


__Exercise:__
Provide a fully parenthesized version of 
  
  a + b ^? c ?^ d less a ==> b | c

Every binary opetarion needs to be put in parantheses, but the structure of the expression should not change.
Solution:

  ((a + b) ^? (c ?^ d)) less ((a ==> b) | c)

