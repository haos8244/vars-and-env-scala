package problem1

// Value Domain - Integration of Const(n)

case class Const(n: Int) extends Expr

sealed trait Value
case class NumValue(n: Int) extends Value

sealed trait Expr {
  def eval : Value = {
    this match {
      case Const(n) => NumValue(n)
    }
  }
}
