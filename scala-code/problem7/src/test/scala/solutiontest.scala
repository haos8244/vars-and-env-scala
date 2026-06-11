package problem7

import org.scalatest.funsuite.AnyFunSuite

class Problem7Test extends AnyFunSuite {

  val emptyEnv: Environment = Map()

  //  Prior features still work (regression) 
  test("(1 + 2) + (3 + 4) = 10") {
    assert(Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval(emptyEnv) == NumValue(10))
  }

  test("let x = 5 in x + 1 = 6") {
    assert(Let("x", Const(5), Plus(Ident("x"), Const(1))).eval(emptyEnv) == NumValue(6))
  }

  //  IfThenElse: condition true / false 
  test("if (2 > 1) 5 else 6 = 5  (true branch)") {
    assert(IfThenElse(Gt(Const(2), Const(1)), Const(5), Const(6)).eval(emptyEnv) == NumValue(5))
  }

  test("if (1 > 2) 5 else 6 = 6  (false branch)") {
    assert(IfThenElse(Gt(Const(1), Const(2)), Const(5), Const(6)).eval(emptyEnv) == NumValue(6))
  }

  //  The taken branch's expression is evaluated and returned 
  test("if (1 > 2) 5 else (6 + 1) = 7") {
    assert(
      IfThenElse(Gt(Const(1), Const(2)), Const(5), Plus(Const(6), Const(1))).eval(emptyEnv)
        == NumValue(7)
    )
  }

  test("if (2 > 1) (5 + 1) else 6 = 6") {
    assert(
      IfThenElse(Gt(Const(2), Const(1)), Plus(Const(5), Const(1)), Const(6)).eval(emptyEnv)
        == NumValue(6)
    )
  }

  //  Non-boolean condition -> ERROR (if-error rule) 
  test("if (1) 5 else 6 = ERROR  (numeric condition)") {
    assert(IfThenElse(Const(1), Const(5), Const(6)).eval(emptyEnv) == ErrorValue)
  }

  test("if (1 + 1) 5 else 6 = ERROR  (numeric condition)") {
    assert(IfThenElse(Plus(Const(1), Const(1)), Const(5), Const(6)).eval(emptyEnv) == ErrorValue)
  }

  //  Condition itself errors -> ERROR 
  test("if ((1 > 2) + 1) 5 else 6 = ERROR  (condition evaluates to ErrorValue)") {
    val badCond = Plus(Gt(Const(1), Const(2)), Const(1))
    assert(IfThenElse(badCond, Const(5), Const(6)).eval(emptyEnv) == ErrorValue)
  }

  //  LAZY EVALUATION: the UNTAKEN branch is NOT evaluated 
  // The else-branch references an unbound variable, but the condition is true,
  // so the else-branch is never evaluated -> no error.
  test("if (2 > 1) 5 else <unbound x> = 5  (untaken branch not evaluated)") {
    val e = IfThenElse(Gt(Const(2), Const(1)), Const(5), Ident("x"))
    assert(e.eval(emptyEnv) == NumValue(5))
  }

  test("if (1 > 2) <unbound x> else 6 = 6  (untaken true-branch not evaluated)") {
    val e = IfThenElse(Gt(Const(1), Const(2)), Ident("x"), Const(6))
    assert(e.eval(emptyEnv) == NumValue(6))
  }

  //  Branch result can be any value type (e.g. Bool) 
  test("if (2 > 1) (3 > 1) else (1 > 3) = BoolValue(true)") {
    assert(
      IfThenElse(Gt(Const(2), Const(1)), Gt(Const(3), Const(1)), Gt(Const(1), Const(3)))
        .eval(emptyEnv) == BoolValue(true)
    )
  }

  //  If combined with Let / Ident 
  test("let x = 5 in if (x > 3) 100 else 200 = 100") {
    val e = Let("x", Const(5),
              IfThenElse(Gt(Ident("x"), Const(3)), Const(100), Const(200)))
    assert(e.eval(emptyEnv) == NumValue(100))
  }

  test("let x = 1 in if (x > 3) 100 else 200 = 200") {
    val e = Let("x", Const(1),
              IfThenElse(Gt(Ident("x"), Const(3)), Const(100), Const(200)))
    assert(e.eval(emptyEnv) == NumValue(200))
  }

  //  Nested if 
  test("nested if: if (1>2) 0 else (if (3>2) 9 else 8) = 9") {
    val e = IfThenElse(Gt(Const(1), Const(2)),
              Const(0),
              IfThenElse(Gt(Const(3), Const(2)), Const(9), Const(8)))
    assert(e.eval(emptyEnv) == NumValue(9))
  }
}
