abstract class IntSet:
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(other: IntSet): IntSet
end IntSet


class Empty() extends IntSet:
  def contains(x: Int): Boolean = false // an empty set contains no element so it always returns false
  def incl(x: Int): IntSet = NonEmpty(x, Empty(), Empty()) // including an element into the empty set will return a 
  // node linked to two empty nodes
  def union(s: IntSet): IntSet = s


class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet:
  def contains(x: Int): Boolean =
    if (x < elem) left.contains(x)
    else if (x > elem) right.contains(x)
    else true
  def incl(x: Int): IntSet =
    if (x < elem) NonEmpty(elem, left.incl(x), right)
    else if (x > elem) NonEmpty(elem, left, right.incl(x))
    else this
  def union(s: IntSet): IntSet = 
    // A single union call will be recursively replaced with two union calls of smaller trees. 
    // The union call terminates when it reaches an Empty node which will evaluate to the argument of the union call.
    // First, the left.union(right) call evaluates, collecting each element at the current node with every 
    // recursive call (due to .incl(elem)). Each of the following unions in the recursive loop are called on sets 
    // that are smaller than the set it started with. We start with the current set, which is clearly bigger than 
    // left or right, so left.union(right) will work on smaller sets. Finally, the other union with 's' works on 
    // the result of left.union(right).
    left.union(right).union(s).incl(elem)
    // This is a very inefficient way to perform union because the sets are decomposed and reconstituted multiple
    // times.
end NonEmpty