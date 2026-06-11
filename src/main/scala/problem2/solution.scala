package problem2

// NOTE: Plus(e1, e2):

case class Const(n: Int) extends Expr

sealed trait Value
case class NumValue(n: Int) extends Value

case class Plus(e1: Expr, e2: Expr) extends Expr

sealed trait Expr {
  def eval : Value = {
    this match {
      case Const(n) => NumValue(n)

      case Plus(e1, e2) => (e1.eval, e2.eval) match {
        case (NumValue(n1), NumValue(n2)) => NumValue(n1 + n2)
        case _ => ??? // We will get to this later, what would this maybe be?
      }
    }
  }
}
