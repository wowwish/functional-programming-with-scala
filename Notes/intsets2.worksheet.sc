import javax.imageio.plugins.tiff.ExifInteroperabilityTagSet
abstract class IntSet:
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(s: IntSet): IntSet
end IntSet

// Exercise methods for object IntSet
object IntSet:
  def apply(): IntSet = Empty
  def apply(x: Int): IntSet = Empty.incl(x)
  def apply(x: Int, y: Int) = Empty.incl(x).incl(y)
end IntSet

object Empty extends IntSet:
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = NonEmpty(x, Empty, Empty)
  def union(s: IntSet): IntSet = s
end Empty

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet:
  def contains(x: Int): Boolean =
    if (x < elem) left.contains(x)
    else if (x > elem) right.contains(x)
    else true
  def incl(x: Int): IntSet =
    if (x < elem) NonEmpty(elem, left.incl(x), right)
    else if (x > elem) NonEmpty(elem, left, right.incl(x))
    else this
  def union(s: IntSet) =
    left.union(right).union(s).incl(elem)
end NonEmpty

// Testing
IntSet()
IntSet(1)
IntSet(2, 3)