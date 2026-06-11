package problem2

import org.scalatest.funsuite.AnyFunSuite

class Problem2Test extends AnyFunSuite {

  //  Const 
  test("Const(1) = NumValue(1)") {
    assert(Const(1).eval == NumValue(1))
  }

  //  Plus: basic 
  test("1 + 2 = 3") {
    assert(Plus(Const(1), Const(2)).eval == NumValue(3))
  }

  test("3 + 4 = 7") {
    assert(Plus(Const(3), Const(4)).eval == NumValue(7))
  }

  test("0 + 0 = 0") {
    assert(Plus(Const(0), Const(0)).eval == NumValue(0))
  }

  test("5 + 0 = 5 (additive identity)") {
    assert(Plus(Const(5), Const(0)).eval == NumValue(5))
  }

  //  Plus: negatives 
  test("5 + (-3) = 2") {
    assert(Plus(Const(5), Const(-3)).eval == NumValue(2))
  }

  test("(-2) + (-2) = -4") {
    assert(Plus(Const(-2), Const(-2)).eval == NumValue(-4))
  }

  //  Plus: nested 
  test("(1 + 2) + (3 + 4) = 10") {
    assert(
      Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval == NumValue(10)
    )
  }

  test("left-nested ((1+1)+1)+1 = 4") {
    assert(
      Plus(Plus(Plus(Const(1), Const(1)), Const(1)), Const(1)).eval == NumValue(4)
    )
  }

  test("right-nested 1+(1+(1+1)) = 4") {
    assert(
      Plus(Const(1), Plus(Const(1), Plus(Const(1), Const(1)))).eval == NumValue(4)
    )
  }

  test("deep nested sum = 15") {
    val e = Plus(Const(1), Plus(Const(2), Plus(Const(3), Plus(Const(4), Const(5)))))
    assert(e.eval == NumValue(15))
  }

  //  Result shape 
  test("Plus result is a NumValue") {
    assert(Plus(Const(2), Const(2)).eval.isInstanceOf[NumValue])
  }
}
