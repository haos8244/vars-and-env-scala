package problem4

import org.scalatest.funsuite.AnyFunSuite

class Problem4Test extends AnyFunSuite {

  //  Const / Plus / Gt still work (no regressions) 
  test("Const(1) = NumValue(1)") {
    assert(Const(1).eval == NumValue(1))
  }

  test("1 + 2 = 3") {
    assert(Plus(Const(1), Const(2)).eval == NumValue(3))
  }

  test("(1 + 2) + (3 + 4) = 10") {
    assert(
      Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval == NumValue(10)
    )
  }

  test("4 > 3 = true") {
    assert(Gt(Const(4), Const(3)).eval == BoolValue(true))
  }

  test("1 > 2 = false") {
    assert(Gt(Const(1), Const(2)).eval == BoolValue(false))
  }

  //  Plus errors: a Bool operand is not a number 
  test("1 + (3 > 2) = ERROR  (right operand is a Bool)") {
    assert(Plus(Const(1), Gt(Const(3), Const(2))).eval == ErrorValue)
  }

  test("(2 > 3) + 4 = ERROR  (left operand is a Bool)") {
    assert(Plus(Gt(Const(2), Const(3)), Const(4)).eval == ErrorValue)
  }

  test("(1 > 2) + (3 > 4) = ERROR  (both operands Bool)") {
    assert(
      Plus(Gt(Const(1), Const(2)), Gt(Const(3), Const(4))).eval == ErrorValue
    )
  }

  //  Gt errors: a Bool operand can't be compared 
  test("(1 > 2) > 3 = ERROR  (left operand is a Bool)") {
    assert(Gt(Gt(Const(1), Const(2)), Const(3)).eval == ErrorValue)
  }

  test("3 > (1 > 2) = ERROR  (right operand is a Bool)") {
    assert(Gt(Const(3), Gt(Const(1), Const(2))).eval == ErrorValue)
  }

  //  Error propagation: an inner ERROR bubbles up 
  test("(1 + (2 > 1)) + 3 = ERROR  (inner Plus errors, outer propagates)") {
    val inner = Plus(Const(1), Gt(Const(2), Const(1)))   // -> ErrorValue
    assert(Plus(inner, Const(3)).eval == ErrorValue)
  }

  test("(1 + (2 > 1)) > 3 = ERROR  (inner error propagates through Gt)") {
    val inner = Plus(Const(1), Gt(Const(2), Const(1)))   // -> ErrorValue
    assert(Gt(inner, Const(3)).eval == ErrorValue)
  }

  //  Result shape 
  test("an erroring expression evaluates to ErrorValue (the object)") {
    assert(Plus(Const(1), Gt(Const(1), Const(2))).eval == ErrorValue)
  }

  test("a valid expression does NOT produce ErrorValue") {
    assert(Plus(Const(1), Const(2)).eval != ErrorValue)
  }
}
