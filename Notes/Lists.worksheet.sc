trait LIST[T]:
  def isEmpty: Boolean
  def head: T
  def tail: LIST[T]

class CONS[T](val head: T, val tail: LIST[T]) extends LIST[T]:
  def isEmpty = false
  // Since defining the parameters with 'val' already defines them as read-only fields, we do't have to
  // define them again inside the class body

class NIL[T] extends LIST[T]:
  def isEmpty = true
  def head = throw new NoSuchElementException("NIL.head")
  def tail = throw new NoSuchElementException("NIL.tail")

def nth[T](xs: LIST[T], n: Int): T = 
  if (xs.isEmpty) throw IndexOutOfBoundsException()
  else if (n == 0) xs.head
  else nth(xs.tail, n - 1)

// Testing
nth(CONS(1, CONS(2, CONS(3, NIL()))), 2)
nth(CONS(1, CONS(2, CONS(3, NIL()))), 0)
// nth(CONS(1, CONS(2, CONS(3, NIL()))), 3)