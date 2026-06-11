package problem5

import org.scalatest.funsuite.AnyFunSuite

class Problem5Test extends AnyFunSuite {

  // An empty environment to evaluate under (nothing uses it yet,
  // but eval now requires it as an argument).
  val emptyEnv: Environment = Map()

  //  Const (now takes an env) 
  test("Const(1) = NumValue(1) under empty env") {
    assert(Const(1).eval(emptyEnv) == NumValue(1))
  }

  //  Plus still works with the env threaded through 
  test("1 + 2 = 3") {
    assert(Plus(Const(1), Const(2)).eval(emptyEnv) == NumValue(3))
  }

  test("(1 + 2) + (3 + 4) = 10  (env passed down recursively)") {
    assert(
      Plus(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval(emptyEnv) == NumValue(10)
    )
  }

  //  Gt still works with the env threaded through 
  test("4 > 3 = true") {
    assert(Gt(Const(4), Const(3)).eval(emptyEnv) == BoolValue(true))
  }

  test("1 > 2 = false") {
    assert(Gt(Const(1), Const(2)).eval(emptyEnv) == BoolValue(false))
  }

  test("(1 + 2) > (3 + 4) = false") {
    assert(
      Gt(Plus(Const(1), Const(2)), Plus(Const(3), Const(4))).eval(emptyEnv) == BoolValue(false)
    )
  }

  test("100 > (3 + 4) = true") {
    assert(
      Gt(Const(100), Plus(Const(3), Const(4))).eval(emptyEnv) == BoolValue(true)
    )
  }

  //  Errors still propagate 
  test("1 + (3 > 2) = ERROR") {
    assert(Plus(Const(1), Gt(Const(3), Const(2))).eval(emptyEnv) == ErrorValue)
  }

  test("(2 > 3) + 4 = ERROR") {
    assert(Plus(Gt(Const(2), Const(3)), Const(4)).eval(emptyEnv) == ErrorValue)
  }

  //  The env is accepted but doesn't change Const/Plus/Gt results 
  // (A non-empty env should give identical answers, since nothing reads it yet.)
  test("a populated env doesn't affect arithmetic (no Ident/Let yet)") {
    val populated: Environment = Map("x" -> NumValue(99), "y" -> BoolValue(true))
    assert(Plus(Const(1), Const(2)).eval(populated) == NumValue(3))
    assert(Gt(Const(5), Const(1)).eval(populated) == BoolValue(true))
  }

  //  Result shape 
  test("Plus yields NumValue, Gt yields BoolValue") {
    assert(Plus(Const(1), Const(2)).eval(emptyEnv).isInstanceOf[NumValue])
    assert(Gt(Const(1), Const(2)).eval(emptyEnv).isInstanceOf[BoolValue])
  }
}
