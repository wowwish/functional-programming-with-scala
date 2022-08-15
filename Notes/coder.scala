class Coder(words: List[String]):
  val mnemonics = Map(
    '2' -> "ABC",  '3' -> "DEF", '4' -> "GHI", '5' -> "JKL", '6' -> "MNO",
    '7' -> "PQRS", '8' -> "TUV", '9' -> "WXYZ")

  /** Maps a letter to the digit it represents **/
  private val charCode: Map[Char, Char] = 
    for
      (digit, str) <- mnemonics         // (digit, str) range over all the pairs in 'mnemmonics'
      ltr <- str                        // 'ltr' ranges over all the letters (Char) in str
    yield (ltr, digit)                  // return the pair (ltr, digit), a single Char mapped to its digit

  /** Maps a word to the digit string it can represent **/
  // mapping each Char in the word with 'charCode' to get the digit string that the whole word represents
  // we convert 'word' to upper case because 'charCode' is only defined for upper case Characters
  private def wordCode(word: String): String = word.toUpperCase.map(charCode)

  /** Maps a digit string to all words in the dictionary that represents it **/
  private val wordsForNum: Map[String, List[String]] = 
    // for each word in 'words', take its digit string by calling 'wordCode' on it and use this digit string as the 
    // key for a Map. The value of this key in the Map will be a list of Strings of all the words that have this 
    // same digit string. 'wordsForNum' will return 'Nil' for digit strings that donot appear in it.
    words.groupBy(wordCode).withDefaultValue(Nil)

  /** All ways to encode a number as a list of words **/
  // Use divide and conquer principle
  def encode(number: String): Set[List[String]] = 
    if (number.isEmpty) Set(Nil)
    else
      for
        // in 'for' expressions, the collection you start with is also the collection of the result. So the result
        // in this case will also be a 'Set'
        splitPoint <- (1 to number.length).toSet
        // take the digits of 'number' upto 'splitPoint' and find the list of words that correspond to it.
        // 'word' will range over the elements of this list returned by 'wordsForNum'. Therefore, each 'word'
        // is a possible solution for the first part of 'number'
        word <- wordsForNum(number.take(splitPoint))
        // The second part of number includes the number at the 'splitPoint' and beyond. It is used in a recursive 
        // call of 'encode'. This part is smaller than 'number' as we will atleast drop one digit from 'number'.
        rest <- encode(number.drop(splitPoint))
      // putting things together
      yield word :: rest

// main function that is invoked when this script file is compiled and run
@main def code(number: String) =
  val coder = Coder(List(
    "Scala", "Python", "Ruby", "C",
    "rocks", "socks", "sucks", "works", "pack"))
  print(coder.encode(number).map(_.mkString(" ")))

