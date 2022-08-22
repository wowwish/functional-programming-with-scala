type Occurrences = List[(Char, Int)]

val test = List(('a', 2), ('b', 2), ('c', 2))

val result = List(
         List(),
         List(('a', 1)),
         List(('a', 2)),
         List(('b', 1)),
         List(('a', 1), ('b', 1)),
         List(('a', 2), ('b', 1)),
         List(('b', 2)),
         List(('a', 1), ('b', 2)),
         List(('a', 2), ('b', 2))
       )

def combinations(occurrences: Occurrences): List[Occurrences] = occurrences match
    case Nil => List(Nil)
    case x :: xs => for i <- (0 to x._2).toList; rest <- combinations(xs) yield ((x._1, i) :: rest).filter(y => y._2 != 0)

def combinations2(occurrences: Occurrences): List[Occurrences] = occurrences match
    case Nil => List(Nil)
    case x :: xs => 
      val others = combinations(xs)
      others ++ (for i <- 1 to x._2; remaining <- others yield (x._1, i) :: remaining)  


print(combinations(test))