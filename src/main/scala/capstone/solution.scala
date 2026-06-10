package capstone

/* =============================================================================
 * CAPSTONE: The Lettuce Interpreter (Const, Plus, Gt, Ident, Let, IfThenElse, And)
 * =============================================================================
 *
 * GOAL
 *   Implement `eval(env)` for the small Lettuce expression language below. This
 *   pulls together everything from the week: values vs. expressions, error
 *   handling, the environment, variables (Ident), bindings (Let), conditionals,
 *   and short-circuiting And.
 *
 * THE GRAMMAR (abstract syntax)
 *   e ::= Const(n)                 -- an integer literal
 *       | Plus(e1, e2)             -- e1 + e2
 *       | Gt(e1, e2)               -- e1 > e2   (strict greater-than)
 *       | Ident(id)                -- use a variable
 *       | Let(id, e1, e2)          -- let id = e1 in e2
 *       | IfThenElse(c, t, f)      -- if (c) t else f
 *       | And(e1, e2)              -- e1 && e2
 *
 *   v ::= NumValue(n)              -- a number result
 *       | BoolValue(b)             -- a boolean result
 *       | ErrorValue               -- something went wrong (type mismatch / unbound var)
 *
 *   n is an Int. id is a String. The environment maps id -> Value.
 *
 * THE JUDGMENT
 *   eval(e, env) = v
 *   "expression e, evaluated under environment env, produces value v."
 *
 * SEMANTICS (what each case must do)
 *
 *   Const(n):
 *     -> NumValue(n).
 *
 *   Plus(e1, e2):
 *     Evaluate both. If BOTH are NumValue(n1), NumValue(n2) -> NumValue(n1 + n2).
 *     Otherwise (either side not a number) -> ErrorValue.
 *
 *   Gt(e1, e2):
 *     Evaluate both. If BOTH are NumValue(n1), NumValue(n2) -> BoolValue(n1 > n2).
 *     Strict: 5 > 5 is false. Otherwise -> ErrorValue.
 *
 *   Ident(id):
 *     If id is bound in env -> the bound value (env(id)).
 *     If id is NOT bound    -> ErrorValue.
 *
 *   Let(id, e1, e2):
 *     Evaluate e1. If it is ErrorValue -> ErrorValue (propagate).
 *     Otherwise bind id to that value in a NEW environment (env + (id -> value))
 *     and evaluate e2 under the new environment. Return e2's value.
 *     NOTE: the original env must not be mutated; an inner binding shadows an
 *     outer one only within e2.
 *
 *   IfThenElse(c, t, f):
 *     Evaluate c.
 *       BoolValue(true)  -> evaluate and return t   (do NOT evaluate f)
 *       BoolValue(false) -> evaluate and return f   (do NOT evaluate t)
 *       anything else (number / ErrorValue) -> ErrorValue
 *     Only the taken branch is evaluated (lazy).
 *
 *   And(e1, e2):
 *     Evaluate e1.
 *       BoolValue(false) -> return BoolValue(false) WITHOUT evaluating e2 (short-circuit!)
 *       BoolValue(true)  -> evaluate e2:
 *                             if e2 is a BoolValue -> return that BoolValue
 *                             otherwise            -> ErrorValue
 *       anything else (number / ErrorValue) -> ErrorValue
 *
 * YOUR TASK
 *   Replace the body of `eval` (the ???s) so all tests in
 *   src/test/scala/capstone/CapstoneTest.scala pass:
 *       sbt "testOnly capstone.*"
 * ===========================================================================*/

type Environment = Map[String, Value]

sealed trait Value
case class NumValue(n: Int) extends Value
case class BoolValue(b: Boolean) extends Value
case object ErrorValue extends Value

sealed trait Expr {
  def eval(env: Environment): Value = this match {
    case Const(n)             => ???
    case Plus(e1, e2)         => ???
    case Gt(e1, e2)           => ???
    case Ident(id)            => ???
    case Let(id, e1, e2)      => ???
    case IfThenElse(c, t, f)  => ???
    case And(e1, e2)          => ???
  }
}

case class Const(n: Int) extends Expr
case class Plus(e1: Expr, e2: Expr) extends Expr
case class Gt(e1: Expr, e2: Expr) extends Expr
case class Ident(id: String) extends Expr
case class Let(id: String, e1: Expr, e2: Expr) extends Expr
case class IfThenElse(eCond: Expr, eTrue: Expr, eFalse: Expr) extends Expr
case class And(e1: Expr, e2: Expr) extends Expr
