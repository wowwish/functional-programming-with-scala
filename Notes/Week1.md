# Reference Resources

[Online Scala3 Documentation](https://docs.scala-lang.org/scala3/book/introduction.html)

[Online book for Scala 2.7.2](https://www.artima.com/pins1ed/)

[Old Scala Book](https://www.scala-lang.org/old/sites/default/files/linuxsoft_archives/docu/files/ScalaByExample.pdf)

[Scala 2 Tutorial](https://twitter.github.io/scala_school/)

[Scala3 E-book](https://www.creativescala.org/creative-scala.html)

# Declaring Variables and Defining Functions

__Scala__ is Primarily a Functional Programming Language. It comes with a REPL (Read, Evaluate, Print, Loop) console
where you can type Scala code and view the Output.

Example:
```
scala> 87 + 135
val res2: Int = 222
```
Here, `87 + 135` was evaluated and saved to a variable called _res2_ of Type _Int_ automatically. Expressions
evaluated in the Scala REPL will be stored to variables like this with the Type automatically inferred by Scala.


Variables can be declared in Scala using three keywords: __def__, __val__ and __var__

A variable declared with __def__ will be evaluated only when they have to be reduced to its value. Keep in mind that
it will be evaluated everywhere it is used. This keyword provides an easy way to create an Infinite Loop:

```
scala> def loop: Int = loop
1 warning found
-- Warning: --------------------------------------------------------------------
1 |def loop: Int = loop
  |^^^^^^^^^^^^^^^^^^^^
  |Infinite recursive call
def loop: Int

scala> loop
```
(Use `Ctrl + C` to terminate the infinite loop)
This becomes an infinite loop because the _loop_ varialble evaluates to itself forever and the expression assigned to it will never reduce to a value. `The Scala REPL also provides a warning about this`. 

Variables declared with __val__ are evaluated during declaration itself and the reduced value of the expression
assigned to the variable is stored. It will cause only one evaluation. These variables are Immutable. Unlike __def__ where the variable had to be used to initiate its evaluation, __val__ will cause evaluation of the variable during its declaration itself, hence, the _loop_ variable when declared with __val__ will immediately create an infinite loop without the need to use the variable anywhere:
```
scala> def loop: Int = loop
1 warning found
-- Warning: --------------------------------------------------------------------
1 |def loop: Int = loop
  |^^^^^^^^^^^^^^^^^^^^
  |Infinite recursive call
def loop: Int

scala> def x = loop
def x: Int
```
This does not run into an infinite loop yet. You need to use the variable __x__ after its declaration to cause the infinite loop. However,
```
scala> def loop: Int = loop
1 warning found
-- Warning: --------------------------------------------------------------------
1 |def loop: Int = loop
  |^^^^^^^^^^^^^^^^^^^^
  |Infinite recursive call
def loop: Int

scala> val x = loop

```
This creates the infinite loop during the declaration of variable __x__.


Variables declared with __var__ are Mutable. They will be evaluated during declaration and when a new expression is
assigned to them.

Scala supports all DataTypes supported by JAVA. The first letter of the DataType has to be capitalized.
DataTypes supported by Scala are:

__Int__     : 32-bit integer
__Long__    : 64-bit integers
__Float__   : 32-bit floating point numbers
__Double__  : 64-bit floating point numbers
__Char__    : 16-bit unicode characters
__Short__   : 16-bit integer
__Byte__    : 8-bit integers
__Boolean__ : boolean values _true_ and _false__


The __def__ Keyword is used to declare functions. The DataTypes of function arguments are declared after the argument names, seperated by a colon. The ReturnType of the function is specified after the function's declaration, seperated by a colon. This ReturnType is mandatory for __recursive__ Functions, but can be skipped for normal Functions. __Pure functions__ are functions that donot create any side-effects like the creation of a variable within a function, updating a global variable, or reading/writing to a file etc. The inputs to the function directly map to the output without any changes in the environment. 
Example Function:

```
scala> def square(x: Int) = x * x
def square(x: Int): Int

scala> def sumOfSquares(x: Int, y: Int): Int = square(x) + square(y)
def sumOfSquares(x: Int, y: Int): Int

scala> sumOfSquares(3, 4)
val res4: Int = 25
```

# CBV (call-by-Value) and CBN (call-by-name)

In _call-by-value_ which is the default evaluation strategy in Scala, the function arguments are first reduced to their expression's value and then used in the function. The function arguments are always evaluated exactly once.
Example:

    sumOfSquares(3, 2+2)
    sumOfSquares(3, 4)
    sumOfSquares(3, 4)
    3 * 3 + square(4)
    9 + square(4)
    9 + 4 * 4
    9 + 16
    25

The Advantage of CBV is that the argument expressions are evaluated only once and their values are cached into the argument variables.


In _call-by-name_, the function is applied to unreduced arguments and the arguments are reduced at the end of the function. The function arguments can be evaluated zero times, one time, or multiple times.
Example:


    sumOfSquares(3, 2+2)
    square(3) + square(2+2)
    3 * 3 + square(2+2)
    9 + square(2+2)
    9 + (2+2) * (2+2)
    9 + 4 * (2+2)
    9 + 4 * 4
    9 + 16
    25

CBN has the advantage where argument expressions that are not used in the function body will not be evaluated.



Both CBV and CBN reduce to the same final value as long as:
  * The reduced expression and sub-function calls consist of pure functions with no side effects
  * Both evaluation strategies terminate
However, there are significant runtime advantages depending on the evaluation strategy used. For example, consider the functon:
```
scala> def test(x: Int, y: Int) = x * x
def test(x: Int, y: Int): Int
```
    test(2, 3)         ->   Both CBV and CBN take same number of steps to arrive at the final value
    test(3+4, 8)       ->   CBV has the advantage here
    test(7, 2+4)       ->   CBN has the advantage here.
    test(3+4, 2*4)     ->   CBV has advantage of single time evaluation but it evaluates the unused argument as well. CBN will do two evaluations of the _x_ argument, but it will not evaluate _y_. Here the advantages of each evaluation strategy cancel out. 


Theorems on CBV/CBN evaluation and its termination:
* If CBV evaluation of an expression _e_ terminates, then CBN evaluation of _e_ will also terminate
* The opposite of the first statement is not true, _ie_, CBV evaluation may not terminate even when CBN terminates.
Example expression to support the theorems (Infinite Loop):
```
scala> def first(x: Int, y: Int) = x
def first(x: Int, y: Int): Int

scala> first(1, loop)

```
Here, the argument _y_ which evaluates to the _loop_ variable defined above, will be evaluated first in the function before the function body is executed. Since the _loop_ variable evaluation is an infinite loop, the function is also stuck in an infinite loop. The _y_ argument is evaluated even though it is not used in the function body. We can prevent this forcing CBN evaluation on the _y_ argument by adding __=>__ after the argument colon next to the argument name:
```
scala> def first(x: Int, y: => Int) = x
def first(x: Int, y: => Int): Int

scala> first(1+2, loop)
val res0: Int = 1

scala> first(loop, 1+2)

```
`first(loop, 1+2)` becomes an infinite loop because the value of loop can never be evaluated.

Scala uses CBV by Default for the following Reasons:
* Since CBV avoids duplicate evaluations of argument expressions by caching the reduced values, it avoids exponential evaluations
* CBV allows traceability of side-effects for functions.


# Conditionals

The _if-else_ conditional condition in Scala is an __expression__ and not a statement. This means that the _if-then_ condition should not have any _side-effects_ such as assigning a value to a variable within the conditional. The _if-else_ condition resembles a pure function that can return one of two values, depending on the whether the _predicate_ condition is _true_ or _false_.

```
scala> def abs(x: Int) = if (x >= 0) x else -x
def abs(x: Int): Int
```

`x >= 0` is a _predicate_ of type Boolean.

A Boolean expression __b__ (or predicate for the if condition) can be composed of the Boolean constants `true` and `false`, negations (`!b`), Conjunctions (`b && b`) or Disjunctions (`b || b`). The expression can have the useual comparison operators: `e <= e`, `e >= e`, `e < e`, `e > e`, `e == e`, `e != e`.

Reduction rules for Boolean Expressions follow "short-circuit evaluation" in Scala (refer the rules with `&&` and `||` below to understand "short-circuit evaluation". `&&` and `||` donot always need their right operand to be evaluated):
* !true         -->     false
* !false        -->     true
* true && e     -->     e
* false && e    -->     false
* true || e     -->     true
* false || e    -->     e

Example showing short-circuit evaluation:
```
scala> def loop: Boolean = loop
1 warning found
-- Warning: --------------------------------------------------------------------
1 |def loop: Boolean = loop
  |^^^^^^^^^^^^^^^^^^^^^^^^
  |Infinite recursive call
def loop: Boolean

scala> true || loop
val res0: Boolean = true

scala> true && loop

```
The value true is returned without evaluating the _right-hand-side_ of the __or__ operator `||`.
However, using `true && loop` will lead to an infinite loop becuase the __and__ operator `&&` will try to evaluate the loop variable.

# Functions for __AND__ and __OR__ operators without using `&&`, `||`

Function for __AND__ operator which also passes the non-terminating test-case

```
scala> def and(x: Boolean, y: => Boolean): Boolean =
     | if (x) then y else false
def and(x: Boolean, y: => Boolean): Boolean

scala> and(true, true)
val res0: Boolean = true

scala> and(true, false)
val res1: Boolean = false

scala> and(false, false)
val res2: Boolean = false

scala> def loop: Boolean = loop
1 warning found
-- Warning: --------------------------------------------------------------------
1 |def loop: Boolean = loop
  |^^^^^^^^^^^^^^^^^^^^^^^^
  |Infinite recursive call
def loop: Boolean

scala> and(false, loop)
val res3: Boolean = false
```

Function for __OR__ operator which also passes the non-terminating test-case:
```
scala> def loop: Boolean = loop
1 warning found
-- Warning: --------------------------------------------------------------------
1 |def loop: Boolean = loop
  |^^^^^^^^^^^^^^^^^^^^^^^^
  |Infinite recursive call
def loop: Boolean

scala> def or(x: Boolean, y: => Boolean): Boolean =
     | if (x) true else y
def or(x: Boolean, y: => Boolean): Boolean

scala> or(true, true)
val res4: Boolean = true

scala> or(true, false)
val res5: Boolean = true

scala> or(false, false)
val res6: Boolean = false

scala> or(true, loop)
val res7: Boolean = true
```

# Square roots with Newton's method:

A classical way to achieve an approximation of the square root of a number is by Newton's method.
To compute sqrt(x):
* Start with an __initial guess__ value __y__
* Repeatedly improve the estimate by taking the __mean__ of __y__ and __x/y__

For example, take the case of sqrt(2) and start with a guess value of 1:

  Estimation              Quotient                Mean
      1                   2 / 1 = 2               1.5
      1.5                 2 / 1.5 = 1.333         1.4162
      1.4162              2 / 1.4162 = 1.4118     1.4142
      1.4142              .....                   ....

Define a function that computes one _iteration_ step:
```
def sqrtIter(guess: Double, x: Double): Double = 
  if isGoodEnough(guess, x) guess
  else sqrtIter(improve(guess, x), x) 
```
Note that _sqrtIter_ is _recursive_, its right-hand-side calls itself.
Recursive functions `require` an explicit `return type` in Scala.
For non-recursive functions, this return type is optional. But it is recommended to add it as a Good Practice.

Also define functions to _improve_ the estimate and to _test_ for termination (check for the difference between the square of _guess_ and the actual value _x_):
```
def improve(guess: Double, x: Double): Double = 
  (guess + x / guess) / 2
```

```
def isGoodEnough(guess:Double, x: Double): Boolean = 
    abs(guess * guess - x) < 0.001
```
Keep in mind that the cutoff for the difference that we are using - 0.001 can be imprecise for smaller numbers and non-termination for very large numbers. Large numbers could have abs(guess * guess - x) >> 0.001 and the difference may never come to a value < 0.001, smaller numbers could have more accurate square roots with difference << 0.001
Inherent errors in floating-point number calculations also add to this problem. Try a different method to check if the guess is good enough and come up with a version that works for large as well as small numbers.

```
def abs(x:Double) = if (x < 0) -x else x

def isGoodEnough(guess: Double, x: Double):Boolean = abs(guess * guess - x) < 0.001

def improve(guess: Double, x: Double) =(guess + x / guess) / 2

def sqrtIter(guess: Double, x: Double): Double =
  if (isGoodEnough(guess, x)) guess
  else sqrtIter(improve(guess, x), x)

def sqrt(x: Double) = sqrtIter(1.0, x)
```

try finding sqrt(0.001), sqrt(1e-20), sqrt(1e20), sqrt(1e50).

My Version for example:
```
def isGoodEnough(guess: Double, x: Double):Boolean = abs(guess * guess - x) < 0.001 * x
```


# Blocks and Lexical Scoping

It's a good practice in programming to split up a task into many small functions. Functions like _isGoodEnough_, _improve_ and _sqrIter_ pollute the global namespace and we normally would not want these functions to be user-accessible as they are only part of the implementation of _sqrt_. We can put these _auxiliary_ functions or sub-functions inside _sqrt_ using a block delimited by braces `{ ... }`.

```
def sqrt(x: Double) = {
  def abs(x: Double): Double = if (x >= 0) x else -x

  def sqrtIter(guess: Double, x: Double): Double = 
    if (isGoodEnough(guess, x)) guess
    else sqrtIter(improve(guess, x), x)

  def improve(guess: Double, x: Double): Double = 
    (guess + x / guess) / 2

  def isGoodEnough(guess: Double, x: Double) = 
    abs(guess * guess - x) < 0.001 * x

  sqrtIter(1.0, x)
}
```

A block consists of a seuqence of definitions or expressions. __The last element of a block is an expression that defines its value__. This return expression can be preceded by _auxiliary_ definitions. Blocks are themselves an expression and they can appear everywhere an expression can. Scala3 also suppports indentation for blocks similar to python instead of braces. We can also write the above function like this in Scala3:
```
def sqrt(x: Double) =
  def abs(x: Double): Double = if (x >= 0) x else -x
  def sqrtIter(guess: Double, x: Double): Double = 
    if (isGoodEnough(guess, x)) guess
    else sqrtIter(improve(guess, x), x)
  def improve(guess: Double, x: Double): Double =
    (guess + x / guess) / 2
  def isGoodEnough(guess: Double, x: Double) =
    abs(guess * guess - x) < 0.001 * x
  sqrtIter(1.0, x)
```

The definitions inside a block are only visible from within the block. The defnitions inside a block "shadow" (override) the definitions of the same names outside the block. For example:
```
scala> val x = 0
val x: Int = 0

scala> def f(y: Int) = y + 1
def f(y: Int): Int

scala> val result =
     |   val x = f(3)
     |   x * x

scala> result
val res4: Int = 16
```

Another Example:
```
val x = 0
def f(y: Int) = y + 1
val y = 
  val x = f(3)
  x * x
val result = y + x
```

In the `val result = y + x` expression, the `x` in the right-hand-side will be evaluated as 0 becuase the `x` inside `val y = { ... }` is actually a local variable whose scope is restricted to the block which evaluates to the value of variable `y`. Similarly, when calculating the value of `y`, the `x` variable declared on the right-hand-side block expression will take precedence over the `val x = 0` declaration outside. Hence, the result becomes `result = 4 * 4 + 0`, `result = 16`.

`Variable declared outside a block are accessible inside it. Such variables are only shadowed when a declaration of a variable of the same name is done inside the Block.`

For example, out _sqrt_ function from before can be written as:
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

sqrt(4)

sqrt(25)
```

The above code snippet works without the argument __x__ being declared as an argument in any of the _auxiliary_ functions because it is outside the block of the _auxiliary_ functions and is not shadowed by any local declaration in the body of the _auxiliary_ functions.


# Semicolons

If there are more than one statement in a line, they have to be seperated by a `;`s.
Example:
```
scala> val y = x + 1; y * y
val y: Int = 1
val res7: Int = 1
```

However, for lines with single statements, the semicolons at the end of the line are usually left out.



# Evaluating a Function Application in Scala

One evaluates a function application `f(e1, ...., en)` by:
* Evaluating the expressions `e1, ...., en` resulting in the values `v1, ..., vn`.
* Then, replacing the application with the body of the function `f`, in which the actual parameters `v1, ..., vn` replace the formal parameters of `f`.

This can be formalized as a _rewriting of the program itself_.

    def f(x1 ... xn) = B; .... f(v1, .... vn)
    def f(x1 ... xn) = B; ... [v1/x1, .... vn/xn]B

Here, `[v1/x1, ...., vn/xn]B` means the expression `B` in which all occurences of `xi` have been replaced by the corresponding values `vi`. `[v1/x1, ...., vn/xn]` is called as __substitution__.

Consider the example of a function that computes the _greatest common divisor_ using Euclid's Algorithm:
```
def gcd(a: Int, b: Int): Int =
  if (b == 0) a else gcd(b, a % b) 
```

Here, `gcd(14, 21)` is evaluated as follows:

    -->     if (21 == 0) 14 else gcd(21, 14 % 21) 
    -->     if (false) 14 else gcd(21, 14 % 21)
    -->     gcd(21, 14 % 21)
    -->     gcd(21, 14)
    -->     if (14 == 0) 21 else gcd(14, 21 % 14)
    -->     if (false) 21 else gcd(14, 21 % 14)
    -->     gcd(14, 21 % 14)
    -->     gcd(14, 7)
    -->>    gcd(7, 0)
    -->     if (0 == 0) 7 else gcd(0, 7 % 0)
    -->     7

Here, `-->>` means reduction by several steps.


Also consider the fatorial function:
```
def factorial(n: Int): Int = 
  if (n == 0) 1 else n * factorial(n - 1)
```

Here, `factorial(4)` is evaluated as follows:

    -->     if (4 == 0) 1 else factorial(4 - 1)
    -->>    4 * (3 * factorial(2)) 
    -->>    4 * (3 * (2 * factorial(1)))
    -->>    4 * (3 * (2 * (1 * factorial(0))))
    -->>    4 * (3 * (2 * (1 * 1)))
    -->>    24

Here, `-->>` means reduction by several steps. We see deeper and deeper nesting (more `-->>`) in the reduction of the `factorial` function compared to the `gcd` function which is relatively flat. In `gcd`, the recursive call is the last action in the function whereas in `factorial`, the recursive call is not the last action (we multiply the value of the recursive call with `n`). Hence, the intermediate expressions in the `gcd` function always have the same size, but not in `factorial`. This abstract observation on the reduction traces translates to the hardware level as well. The function call stack consists of __stack frames__ and if a function calls itself as the last action in its body, its stack frame can be reused for subsequenct recursive calls. This phenomenon is known as __tail recursion__. Tail recursive functions are in essence, iterative processes. In general, if the last action of a function consists of calling a function (which may be the same), one stack frame would be sufficient for both functions. Such calls are called __tail-calls__.

In Scala, only directly recursive calls to the current function can be optimized. Such an optimization can improve the runtime speed of a function and prevent stack overflows. Large programs and data structures need _tail recursive_ calls to prevent stack overflow in the JVM. The JVM normally only supports 2000 - 3000 recursive calls untill you get a stack overflow.

One can require that a function is _tail-recursive_ during compile-time, using a `@tailrec` annotation:
```
import scala.annotation.tailrec

@tailrec
def gcd(a: Int, b: Int): Int = 
  if (b == 0) a else gcd(b, a % b)
```

This will make the compiler to issue a warning if the `gcd` function implementation is not _tail recursive_. Even if the `@tailrec` annotation is not used, the compiler will try to optimise the `gcd` function to be _tail recursive_, but you won't get an error as a check for you that `gcd` is _tail recursive_.


