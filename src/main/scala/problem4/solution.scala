package problem4

// ERRORS

case class Const(n: Int) extends Expr

sealed trait Value
case class NumValue(n: Int) extends Value
case class BoolValue(b: Boolean) extends Value

case class Plus(e1: Expr, e2: Expr) extends Expr

case class Gt(e1: Expr, e2: Expr) extends Expr

case object ErrorValue extends Value

sealed trait Expr {
  def eval : Value = {
    this match {
      case Const(n) => NumValue(n)

      case Plus(e1, e2) => (e1.eval) match {
        case NumValue(n1) => (e2.eval) match {
          case NumValue(n2) => NumValue(n1 + n2)
          case _ => ErrorValue
        }
        case _ => ErrorValue
      }


      case Gt(e1, e2) => (e1.eval) match {
        case NumValue(n1) => (e2.eval) match {
          case NumValue(n2) => BoolValue(n1 > n2)
          case _ => ErrorValue
        }
        case _ => ErrorValue
      }
    }
  }
}

