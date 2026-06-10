package problem6

import org.scalatest.funsuite.AnyFunSuite

class Problem6Test extends AnyFunSuite {

  val emptyEnv: Environment = Map()

  // ---- Ident: lookup under a given environment ----
  test("x under {} = ERROR  (unbound variable)") {
    assert(Ident("x").eval(emptyEnv) == ErrorValue)
  }

  test("x under { y -> 2 } = ERROR  (different variable bound)") {
    assert(Ident("x").eval(Map("y" -> NumValue(2))) == ErrorValue)
  }

  test("x under { x -> 2 } = NumValue(2)  (found)") {
    assert(Ident("x").eval(Map("x" -> NumValue(2))) == NumValue(2))
  }

  test("x under { x -> true } = BoolValue(true)  (looks up whatever value)") {
    assert(Ident("x").eval(Map("x" -> BoolValue(true))) == BoolValue(true))
  }

  // ---- Let: basic binding ----
  test("let x = 2 in x = NumValue(2)") {
    assert(Let("x", Const(2), Ident("x")).eval(emptyEnv) == NumValue(2))
  }

  test("let x = 3 in x = NumValue(3)") {
    assert(Let("x", Const(3), Ident("x")).eval(emptyEnv) == NumValue(3))
  }

  test("let x = 3 + 4 in x = NumValue(7)  (e1 is evaluated before binding)") {
    assert(Let("x", Plus(Const(3), Const(4)), Ident("x")).eval(emptyEnv) == NumValue(7))
  }

  // ---- Let: the bound variable is usable in the body ----
  test("let x = 5 in x + 1 = NumValue(6)") {
    assert(
      Let("x", Const(5), Plus(Ident("x"), Const(1))).eval(emptyEnv) == NumValue(6)
    )
  }

  // ---- Let: nested lets, two distinct variables ----
  test("let x = 3 in let y = 2 in x + y = NumValue(5)") {
    val e = Let("x", Const(3),
              Let("y", Const(2),
                Plus(Ident("x"), Ident("y"))))
    assert(e.eval(emptyEnv) == NumValue(5))
  }

  // ---- Let: shadowing (inner binding overrides outer) ----
  test("let x = 1 in let x = 2 in x = NumValue(2)  (inner shadows outer)") {
    val e = Let("x", Const(1),
              Let("x", Const(2),
                Ident("x")))
    assert(e.eval(emptyEnv) == NumValue(2))
  }

  // ---- Let: the e1 of an inner let sees the OUTER binding ----
  // let x = 2 in
  //   let y = (let x = x + 1 in x + 1) in
  //     x + y
  // inner x+1 uses outer x=2 -> 3, then +1 -> 4 (that's y);
  // outer x is still 2; result = 2 + 4 = 6
  test("nested shadowing example evaluates to NumValue(6)") {
    val e =
      Let("x", Const(2),
        Let("y",
          Let("x", Plus(Ident("x"), Const(1)),   // inner x = outer x + 1 = 3
            Plus(Ident("x"), Const(1))),          // body: 3 + 1 = 4  -> y = 4
          Plus(Ident("x"), Ident("y"))))          // outer x (2) + y (4) = 6
    assert(e.eval(emptyEnv) == NumValue(6))
  }

  // ---- Let: outer binding survives after inner scope ends ----
  test("outer x unchanged after inner let: let x = 10 in (let x = 99 in x) ... outer still 10") {
    // (let x = 99 in x) -> 99, but that's a separate subexpression;
    // here we add the inner result to the outer x to prove outer x is still 10.
    val e =
      Let("x", Const(10),
        Plus(
          Let("x", Const(99), Ident("x")),  // inner scope: 99
          Ident("x")))                       // outer x: still 10
    assert(e.eval(emptyEnv) == NumValue(109))
  }

  // ---- Let: error in e1 propagates ----
  test("let x = (1 > 2) + 3 in x = ERROR  (e1 errors)") {
    val badE1 = Plus(Gt(Const(1), Const(2)), Const(3))  // bool + num -> ErrorValue
    assert(Let("x", badE1, Ident("x")).eval(emptyEnv) == ErrorValue)
  }

  // ---- Ident inside arithmetic / comparison ----
  test("let x = 5 in x > 3 = BoolValue(true)") {
    assert(
      Let("x", Const(5), Gt(Ident("x"), Const(3))).eval(emptyEnv) == BoolValue(true)
    )
  }

  test("using an unbound variable in Plus = ERROR") {
    // x is never bound, so Ident(x) -> ErrorValue, and Plus propagates it
    assert(Plus(Ident("x"), Const(1)).eval(emptyEnv) == ErrorValue)
  }

  // ---- Regression: earlier features still work with full eval ----
  test("plain arithmetic still works: (1 + 2) + (3 + 4) = 10") {
    assert(
      Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval(emptyEnv) == NumValue(10)
    )
  }
}
