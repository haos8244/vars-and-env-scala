package problem8

import org.scalatest.funsuite.AnyFunSuite

class Problem8Test extends AnyFunSuite {

  val emptyEnv: Environment = Map()

  // Helper expressions for readable tests
  val tru = Gt(Const(2), Const(1))   // evaluates to BoolValue(true)
  val fls = Gt(Const(1), Const(2))   // evaluates to BoolValue(false)

  // ---- And: full truth table ----
  test("true && true = true") {
    assert(And(tru, tru).eval(emptyEnv) == BoolValue(true))
  }

  test("true && false = false") {
    assert(And(tru, fls).eval(emptyEnv) == BoolValue(false))
  }

  test("false && true = false") {
    assert(And(fls, tru).eval(emptyEnv) == BoolValue(false))
  }

  test("false && false = false") {
    assert(And(fls, fls).eval(emptyEnv) == BoolValue(false))
  }

  // ---- And: e2 is a non-bool ----
  // If e1 is TRUE, we must look at e2; a non-bool e2 -> ERROR.
  test("true && 1 = ERROR  (e1 true, e2 is a number)") {
    assert(And(tru, Const(1)).eval(emptyEnv) == ErrorValue)
  }

  // ---- And: SHORT-CIRCUIT ----
  // If e1 is FALSE, e2 is never evaluated, so even a bad e2 doesn't matter.
  test("false && 1 = false  (short-circuit: e2 not evaluated, no error)") {
    assert(And(fls, Const(1)).eval(emptyEnv) == BoolValue(false))
  }

  test("false && <unbound x> = false  (short-circuit: unbound e2 not evaluated)") {
    assert(And(fls, Ident("x")).eval(emptyEnv) == BoolValue(false))
  }

  // ---- And: e1 is a non-bool -> ERROR ----
  test("1 && (1 > 2) = ERROR  (e1 is a number)") {
    assert(And(Const(1), fls).eval(emptyEnv) == ErrorValue)
  }

  test("1 && 2 = ERROR  (both numbers, e1 checked first)") {
    assert(And(Const(1), Const(2)).eval(emptyEnv) == ErrorValue)
  }

  // ---- And: e1 errors -> ERROR ----
  test("((1 > 2) + 1) && true = ERROR  (e1 evaluates to ErrorValue)") {
    val badE1 = Plus(Gt(Const(1), Const(2)), Const(1))
    assert(And(badE1, tru).eval(emptyEnv) == ErrorValue)
  }

  // ---- And: result is the actual value of e2 (not just "true") ----
  test("true && (4 > 3) = BoolValue(true)  (returns e2's value)") {
    assert(And(tru, Gt(Const(4), Const(3))).eval(emptyEnv) == BoolValue(true))
  }

  test("true && (1 > 3) = BoolValue(false)  (returns e2's value)") {
    assert(And(tru, Gt(Const(1), Const(3))).eval(emptyEnv) == BoolValue(false))
  }

  // ---- And combined with Let / Ident ----
  test("let b = (2 > 1) in b && (3 > 1) = true") {
    val e = Let("b", Gt(Const(2), Const(1)),
              And(Ident("b"), Gt(Const(3), Const(1))))
    assert(e.eval(emptyEnv) == BoolValue(true))
  }

  // ---- Nested And ----
  test("(true && true) && false = false") {
    assert(And(And(tru, tru), fls).eval(emptyEnv) == BoolValue(false))
  }

  test("(true && true) && true = true") {
    assert(And(And(tru, tru), tru).eval(emptyEnv) == BoolValue(true))
  }

  // ---- Regression: earlier features still work ----
  test("let x = 5 in if (x > 3) 100 else 200 = 100") {
    val e = Let("x", Const(5),
              IfThenElse(Gt(Ident("x"), Const(3)), Const(100), Const(200)))
    assert(e.eval(emptyEnv) == NumValue(100))
  }
}
