package recfun

object RecFun extends RecFunInterface:

  def main(args: Array[String]): Unit =
    println("Pascal's Triangle")
    for row <- 0 to 10 do
      for col <- 0 to row do
        print(s"${pascal(col, row)} ")
      println()

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = 
    // return 1 if the value falls in the boundary values of the triangle
    // return the sum of the value in the previous row above current value and 
    // the value to the left position of current value in the previous row
    if (c == 0  || c == r) 1 else pascal(c, r - 1) + pascal(c - 1, r - 1)


  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = { 
    // Define a custom function to count the number of open Paranthesis. Add 1 to this count if you encounter
    // an open parentheses and subtract 1 from this count when you encounter a closed parentheses in the 
    // list of characters.
    def check(stringList: List[Char], openPars: Int): Boolean = {
      if (stringList.isEmpty) openPars == 0 // Set count to 0 if string list is empty
      else
        // When you encounter a '(', continue with the rest of the character list and increment the count by 1
        if (stringList.head == '(') check(stringList.tail, openPars + 1)
        else
          // When you encounter a ')', continue with the rest of the list and decrement count  by 1
          // only if the count is already above 0. If count is not > 0, We have encountered an unpaired 
          // closing parentheses and so the && condition will become false. The function will hence return false.
          if (stringList.head == ')') openPars > 0 && check(stringList.tail, openPars - 1)
          else
            // If no parenthesis is encountered in current character, do a recursive call with the rest of the list.
            check(stringList.tail, openPars)
    }  
    check(chars, 0)
  }   

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    // If the coin denominations list is empty and the total amount > 0, return 0
    if (coins.isEmpty && money > 0) 0
    else
      // If total amount is exactly 0, We have only one solution - use no coins. So we return 1.
      if (money == 0) 1
      else 
        // If total amount is negative, then we have no way to reach a solution with coin denominations. 
        // If the inputs donot fall into these simple cases, we find the total number of ways of finding change to the total amount
        // with the given coin denominations by finding the sum of ways of finding change for total amount when the current denomination 
        // is definitely included in the solution (with repetition)
        // and the ways of finding change for the total amount when the current denomination is not included in the solution.
        // Here, countChange(money - coins.head, coins) counts the number of ways achieving the remaining amount when the current element of coins 
        // is included in the solution set atleast once.  The other term, countChange(money, coins.tail), calculates the number of ways of achieving 
        // the total amount without including the current element in the coins List. 
        if (money < 0) 0 else countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }
