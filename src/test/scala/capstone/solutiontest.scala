package capstone

import org.scalatest.funsuite.AnyFunSuite

class CapstoneTest extends AnyFunSuite {

  val emptyEnv: Environment = Map()

  // Reusable bool-producing expressions
  val tru = Gt(Const(2), Const(1))   // -> BoolValue(true)
  val fls = Gt(Const(1), Const(2))   // -> BoolValue(false)

  // =========================================================================
  // Const
  // =========================================================================
  test("Const(1) = NumValue(1)") {
    assert(Const(1).eval(emptyEnv) == NumValue(1))
  }
  test("Const(-7) = NumValue(-7)") {
    assert(Const(-7).eval(emptyEnv) == NumValue(-7))
  }

  // =========================================================================
  // Plus
  // =========================================================================
  test("1 + 2 = 3") {
    assert(Plus(Const(1), Const(2)).eval(emptyEnv) == NumValue(3))
  }
  test("(1 + 2) + (3 + 4) = 10  (nested)") {
    assert(Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval(emptyEnv) == NumValue(10))
  }
  test("5 + (-3) = 2  (negative)") {
    assert(Plus(Const(5), Const(-3)).eval(emptyEnv) == NumValue(2))
  }
  test("1 + (3 > 2) = ERROR  (bool operand)") {
    assert(Plus(Const(1), Gt(Const(3), Const(2))).eval(emptyEnv) == ErrorValue)
  }
  test("(2 > 3) + 4 = ERROR  (bool operand on left)") {
    assert(Plus(Gt(Const(2), Const(3)), Const(4)).eval(emptyEnv) == ErrorValue)
  }

  // =========================================================================
  // Gt
  // =========================================================================
  test("4 > 3 = true") {
    assert(Gt(Const(4), Const(3)).eval(emptyEnv) == BoolValue(true))
  }
  test("1 > 2 = false") {
    assert(Gt(Const(1), Const(2)).eval(emptyEnv) == BoolValue(false))
  }
  test("5 > 5 = false  (strict, not >=)") {
    assert(Gt(Const(5), Const(5)).eval(emptyEnv) == BoolValue(false))
  }
  test("(1 + 2) > (3 + 4) = false  (nested operands)") {
    assert(Gt(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval(emptyEnv) == BoolValue(false))
  }
  test("(1 > 2) > 3 = ERROR  (bool operand)") {
    assert(Gt(Gt(Const(1), Const(2)), Const(3)).eval(emptyEnv) == ErrorValue)
  }

  // =========================================================================
  // Ident  (variable lookup)
  // =========================================================================
  test("x under {} = ERROR  (unbound)") {
    assert(Ident("x").eval(Map()) == ErrorValue)
  }
  test("x under { y -> 2 } = ERROR  (wrong var)") {
    assert(Ident("x").eval(Map("y" -> NumValue(2))) == ErrorValue)
  }
  test("x under { x -> 2 } = NumValue(2)  (found)") {
    assert(Ident("x").eval(Map("x" -> NumValue(2))) == NumValue(2))
  }
  test("b under { b -> true } = BoolValue(true)") {
    assert(Ident("b").eval(Map("b" -> BoolValue(true))) == BoolValue(true))
  }

  // =========================================================================
  // Let  (binding + scope)
  // =========================================================================
  test("let x = 2 in x = NumValue(2)") {
    assert(Let("x", Const(2), Ident("x")).eval(emptyEnv) == NumValue(2))
  }
  test("let x = 3 + 4 in x = NumValue(7)  (e1 evaluated before binding)") {
    assert(Let("x", Plus(Const(3), Const(4)), Ident("x")).eval(emptyEnv) == NumValue(7))
  }
  test("let x = 5 in x + 1 = NumValue(6)  (var used in body)") {
    assert(Let("x", Const(5), Plus(Ident("x"), Const(1))).eval(emptyEnv) == NumValue(6))
  }
  test("let x = 3 in let y = 2 in x + y = NumValue(5)  (two vars)") {
    val e = Let("x", Const(3), Let("y", Const(2), Plus(Ident("x"), Ident("y"))))
    assert(e.eval(emptyEnv) == NumValue(5))
  }
  test("shadowing: let x = 1 in let x = 2 in x = NumValue(2)") {
    val e = Let("x", Const(1), Let("x", Const(2), Ident("x")))
    assert(e.eval(emptyEnv) == NumValue(2))
  }
  test("nested-scope classic: let x = 2 in let y = (let x = x+1 in x+1) in x+y = NumValue(6)") {
    val e =
      Let("x", Const(2),
        Let("y",
          Let("x", Plus(Ident("x"), Const(1)),   // inner x = 3
            Plus(Ident("x"), Const(1))),          // y = 4
          Plus(Ident("x"), Ident("y"))))          // outer x (2) + y (4) = 6
    assert(e.eval(emptyEnv) == NumValue(6))
  }
  test("outer binding survives inner scope: let x = 10 in (let x = 99 in x) + x = NumValue(109)") {
    val e = Let("x", Const(10), Plus(Let("x", Const(99), Ident("x")), Ident("x")))
    assert(e.eval(emptyEnv) == NumValue(109))
  }
  test("let x = ((1>2)+3) in x = ERROR  (e1 errors, propagates)") {
    assert(Let("x", Plus(Gt(Const(1), Const(2)), Const(3)), Ident("x")).eval(emptyEnv) == ErrorValue)
  }

  // =========================================================================
  // IfThenElse
  // =========================================================================
  test("if (2 > 1) 5 else 6 = 5") {
    assert(IfThenElse(Gt(Const(2), Const(1)), Const(5), Const(6)).eval(emptyEnv) == NumValue(5))
  }
  test("if (1 > 2) 5 else 6 = 6") {
    assert(IfThenElse(Gt(Const(1), Const(2)), Const(5), Const(6)).eval(emptyEnv) == NumValue(6))
  }
  test("if (1) 5 else 6 = ERROR  (numeric condition)") {
    assert(IfThenElse(Const(1), Const(5), Const(6)).eval(emptyEnv) == ErrorValue)
  }
  test("if (2 > 1) 5 else <unbound> = 5  (untaken branch NOT evaluated)") {
    assert(IfThenElse(Gt(Const(2), Const(1)), Const(5), Ident("x")).eval(emptyEnv) == NumValue(5))
  }
  test("if (1 > 2) <unbound> else 6 = 6  (untaken branch NOT evaluated)") {
    assert(IfThenElse(Gt(Const(1), Const(2)), Ident("x"), Const(6)).eval(emptyEnv) == NumValue(6))
  }
  test("let x = 5 in if (x > 3) 100 else 200 = 100") {
    val e = Let("x", Const(5), IfThenElse(Gt(Ident("x"), Const(3)), Const(100), Const(200)))
    assert(e.eval(emptyEnv) == NumValue(100))
  }

  // =========================================================================
  // And  (truth table + short-circuit + errors)
  // =========================================================================
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
  test("true && 1 = ERROR  (e1 true, e2 not bool)") {
    assert(And(tru, Const(1)).eval(emptyEnv) == ErrorValue)
  }
  test("false && 1 = false  (short-circuit: e2 not evaluated)") {
    assert(And(fls, Const(1)).eval(emptyEnv) == BoolValue(false))
  }
  test("false && <unbound> = false  (short-circuit)") {
    assert(And(fls, Ident("x")).eval(emptyEnv) == BoolValue(false))
  }
  test("1 && (1 > 2) = ERROR  (e1 not bool)") {
    assert(And(Const(1), fls).eval(emptyEnv) == ErrorValue)
  }
  test("(true && true) && false = false  (nested)") {
    assert(And(And(tru, tru), fls).eval(emptyEnv) == BoolValue(false))
  }

  // =========================================================================
  // Big integration test: combine everything
  // =========================================================================
  test("integration: let x = 5 in if ((x > 3) && (x > 1)) (x + 100) else 0 = 105") {
    val e =
      Let("x", Const(5),
        IfThenElse(
          And(Gt(Ident("x"), Const(3)), Gt(Ident("x"), Const(1))),
          Plus(Ident("x"), Const(100)),
          Const(0)))
    assert(e.eval(emptyEnv) == NumValue(105))
  }
}
