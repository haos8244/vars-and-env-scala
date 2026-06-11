package problem3

import org.scalatest.funsuite.AnyFunSuite

class Problem3Test extends AnyFunSuite {

  //  Const 
  test("Const(1) = NumValue(1)") {
    assert(Const(1).eval == NumValue(1))
  }

  //  Plus (still works) 
  test("1 + 2 = 3") {
    assert(Plus(Const(1), Const(2)).eval == NumValue(3))
  }

  test("(1 + 2) + (3 + 4) = 10") {
    assert(
      Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval == NumValue(10)
    )
  }

  //  Gt: basic true / false 
  test("1 > 2 = false") {
    assert(Gt(Const(1), Const(2)).eval == BoolValue(false))
  }

  test("4 > 3 = true") {
    assert(Gt(Const(4), Const(3)).eval == BoolValue(true))
  }

  //  Gt: equal operands (not strictly greater) 
  test("5 > 5 = false (strict, not >=)") {
    assert(Gt(Const(5), Const(5)).eval == BoolValue(false))
  }

  //  Gt: negatives 
  test("-1 > -2 = true") {
    assert(Gt(Const(-1), Const(-2)).eval == BoolValue(true))
  }

  test("-5 > 0 = false") {
    assert(Gt(Const(-5), Const(0)).eval == BoolValue(false))
  }

  //  Gt: operands are themselves expressions (nested eval) 
  test("(1 + 2) > (3 + 4) = false") {
    assert(
      Gt(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval == BoolValue(false)
    )
  }

  test("100 > (3 + 4) = true") {
    assert(
      Gt(Const(100), Plus(Const(3), Const(4))).eval == BoolValue(true)
    )
  }

  test("(5 + 5) > (2 + 3) = true") {
    assert(
      Gt(Plus(Const(5), Const(5)), Plus(Const(2), Const(3))).eval == BoolValue(true)
    )
  }

  //  Result shape: Gt produces a Bool, Plus produces a Num 
  test("Gt result is a BoolValue") {
    assert(Gt(Const(1), Const(2)).eval.isInstanceOf[BoolValue])
  }

  test("Plus result is a NumValue") {
    assert(Plus(Const(1), Const(2)).eval.isInstanceOf[NumValue])
  }

  //  Result shape: unwrap the boolean 
  test("4 > 3 unwraps to true") {
    Gt(Const(4), Const(3)).eval match {
      case BoolValue(b) => assert(b)
      case other        => fail(s"expected BoolValue, got $other")
    }
  }
}
