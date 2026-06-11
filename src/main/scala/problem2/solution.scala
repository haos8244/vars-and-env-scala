package problem2

// NOTE: Plus(e1, e2):

case class Const(n: Int) extends Expr

sealed trait Value
case class NumValue(n: Int) extends Value

// FIXME:
??? Plus(e1: ???, e2: ???) ???

sealed trait Expr {
  def eval : Value = {
    this match {
      case Const(n) => NumValue(n)

      // FIXME: - consider case _ => ???, We will get to this later, what would this maybe be?
    }
  }
}
