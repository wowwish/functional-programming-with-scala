class Polynom(nonZeroTerms: Map[Int, Double]):

    // Secondary constructor to create a map out of the input args of Polynom() - the primary constructor
    // This eliminates the need to pass a map of (exp, coeff) pairs as the input argument to Polynom()
    // We can directly pass the (exp, coeff) pairs as 'exp -> coeff'
    // This secondary constructor has to deal with varying number of parameters - (exp, coeff) pairs
    def this(bindings: (Int, Double)*) = this(bindings.toMap)

    // creating a map with dfault value, out of the inout argument 'nonZeroTerms'
    val terms = nonZeroTerms.withDefaultValue(0.0)

    // Addition - add two polynomials
    // def + (other: Polynom): Polynom = 
    //     Polynom(terms ++ other.terms.map((exp, coeff) => (exp, terms(exp) + coeff)))


    // EXERCISE - Implementation of '+' function using 'foldLeft'
    def + (other: Polynom) =
      Polynom(other.terms.foldLeft(terms)(addTerm))
    
    // defining the operator used in 'foldLeft' to combine
    def addTerm(terms: Map[Int, Double], term: (Int, Double)): Map[Int, Double] = 
      val (exp, coeff) = term
      terms + (exp -> (terms(exp) + coeff))   // update the Map 'terms' with the key/value pair (exp, terms(exp) + coeff)
      // Scala can figure out that the '+' used here is in context of 'Map' and not in context of 'Polynom'
      // Remember how foldledt works. The 'other.terms' Map is used as the accumulator and the 'addTerm' operation
      // is performed on each of the elements of 'this.terms'.

    // Print the polynomial in a nicely formatted way
    override def toString = 
      val termStrings = 
        for (exp, coeff) <- terms.toList.sorted.reverse      // Get the exponent, coefficient pair from the 'exp' based descending order list of 'terms'
        yield
          val exponent = if (exp == 0) "" else s"x^${exp}"
          s"${coeff}${exponent}"
      if (terms.isEmpty) "0"
      // The mkString() method is utilized to display all the elements of the list in a string along with a separator.
      else termStrings.mkString(" + ").replace("+ -", "- ")          


val x = Polynom(0 -> 2, 1 -> 3, 2 -> 1)
val y = Polynom(0 -> 2, 1 -> -3, 2 -> 1)
val z = Polynom()

x + x
x + x + z


