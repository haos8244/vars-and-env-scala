package mapexercise

import org.scalatest.funsuite.AnyFunSuite

class MapExerciseTest extends AnyFunSuite {
  import MapExercise._

  val sample: Map[String, Int] = Map("x" -> 1, "y" -> 2, "z" -> 3)

  // lookupOrDefault
  test("lookup existing key returns its value") {
    assert(lookupOrDefault(sample, "y") == 2)
  }

  test("lookup missing key returns -1") {
    assert(lookupOrDefault(sample, "w") == -1)
  }

  test("lookup in empty map returns -1") {
    assert(lookupOrDefault(Map(), "x") == -1)
  }

  // addBinding
  test("addBinding adds a new key") {
    assert(addBinding(sample, "w", 4) == Map("x" -> 1, "y" -> 2, "z" -> 3, "w" -> 4))
  }

  test("addBinding overwrites an existing key") {
    assert(addBinding(sample, "x", 99)("x") == 99)
  }

  test("addBinding does NOT mutate the original (immutability)") {
    val before = sample
    addBinding(sample, "w", 4)
    assert(sample == before)          // original unchanged
    assert(!sample.contains("w"))     // the new key didn't leak in
  }

  // isBound
  test("isBound true for present key") {
    assert(isBound(sample, "z"))
  }

  test("isBound false for absent key") {
    assert(!isBound(sample, "q"))
  }

  // removeBinding
  test("removeBinding removes a present key") {
    assert(removeBinding(sample, "y") == Map("x" -> 1, "z" -> 3))
  }

  test("removeBinding on absent key returns unchanged map") {
    assert(removeBinding(sample, "nope") == sample)
  }

  test("removeBinding does not mutate original") {
    val before = sample
    removeBinding(sample, "x")
    assert(sample == before)
  }

  // extend (environment extension)
  test("extend merges disjoint maps") {
    assert(extend(Map("a" -> 1), Map("b" -> 2)) == Map("a" -> 1, "b" -> 2))
  }

  test("extend: right side overrides on shared keys") {
    assert(extend(Map("x" -> 1), Map("x" -> 100)) == Map("x" -> 100))
  }

  test("extend with empty right side is unchanged") {
    assert(extend(sample, Map()) == sample)
  }

  // size
  test("size of sample is 3") {
    assert(size(sample) == 3)
  }

  test("size of empty map is 0") {
    assert(size(Map()) == 0)
  }

  // increment
  test("increment existing key adds 1") {
    assert(increment(sample, "x")("x") == 2)
  }

  test("increment absent key sets it to 1") {
    assert(increment(sample, "new")("new") == 1)
  }

  test("increment twice adds 2") {
    val once = increment(sample, "x")
    val twice = increment(once, "x")
    assert(twice("x") == 3)
  }
}
