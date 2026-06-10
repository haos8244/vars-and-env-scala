package problem5

// Interpreter - Env

type Environment = Map[String, Value]

case class Const(n: Int) extends Expr

sealed trait Value
case class NumValue(n: Int) extends Value
case class BoolValue(b: Boolean) extends Value

case class Plus(e1: Expr, e2: Expr) extends Expr

case class Gt(e1: Expr, e2: Expr) extends Expr

case object ErrorValue extends Value

sealed trait Expr {
  def eval(env: Environment) : Value = {
    this match {
      case Const(n) => NumValue(n)

      case Plus(e1, e2) => (e1.eval(env)) match {
        case NumValue(n1) => (e2.eval(env)) match {
          case NumValue(n2) => NumValue(n1 + n2)
          case _ => ErrorValue
        }
        case _ => ErrorValue
      }


      case Gt(e1, e2) => (e1.eval(env)) match {
        case NumValue(n1) => (e2.eval(env)) match {
          case NumValue(n2) => BoolValue(n1 > n2)
          case _ => ErrorValue
        }
        case _ => ErrorValue
      }
    }
  }
}


