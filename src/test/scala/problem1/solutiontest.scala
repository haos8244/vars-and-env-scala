package problem1

import org.scalatest.funsuite.AnyFunSuite

class Problem1Test extends AnyFunSuite {

  // ---- Const: basic ----
  test("Const(1) evaluates to NumValue(1)") {
    assert(Const(1).eval == NumValue(1))
  }

  test("Const(2) evaluates to NumValue(2)") {
    assert(Const(2).eval == NumValue(2))
  }

  test("Const(0) evaluates to NumValue(0)") {
    assert(Const(0).eval == NumValue(0))
  }

  // ---- Const: negatives / larger values ----
  test("Const(-5) evaluates to NumValue(-5)") {
    assert(Const(-5).eval == NumValue(-5))
  }

  test("Const(100) evaluates to NumValue(100)") {
    assert(Const(100).eval == NumValue(100))
  }

  // ---- Result shape ----
  test("eval result is a NumValue") {
    assert(Const(7).eval.isInstanceOf[NumValue])
  }
}
