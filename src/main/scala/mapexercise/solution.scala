package mapexercise

// Map practice -> implement each function using Scala's immutable Map.
// These mirror the operations you need for the Lettuce environment:
// look up a variable, add a binding, check existence, etc.
//
// Replace each ??? with your implementation.

object MapExercise {

  // 1. Return the value for `key`, or -1 if the key is absent.
  //    Hint: contains / apply, or getOrElse.
  def lookupOrDefault(m: Map[String, Int], key: String): Int = m.getOrElse(key, -1)

  // 2. Add (or overwrite) the binding key -> value, returning the NEW map.
  //    The original map must remain unchanged (immutability!).
  def addBinding(m: Map[String, Int], key: String, value: Int): Map[String, Int] = m + (key -> value)

  // 3. Return true if `key` is bound in the map, false otherwise.
  def isBound(m: Map[String, Int], key: String): Boolean = m.contains(key)

  // 4. Remove `key` from the map, returning the NEW map.
  //    If the key isn't present, return the map unchanged.
  def removeBinding(m: Map[String, Int], key: String): Map[String, Int] = m - key

  // 5. Given two maps, merge them so that bindings in `b` override `a`
  //    on any shared keys. (This is exactly environment extension!)
  def extend(a: Map[String, Int], b: Map[String, Int]): Map[String, Int] = a ++ b

  // 6. Return how many keys the map contains.
  def size(m: Map[String, Int]): Int = m.size

  // 7. Increment the value at `key` by 1. If the key is absent, set it to 1.
  def increment(m: Map[String, Int], key: String): Map[String, Int] = m + (key -> (m.getOrElse(key, 0) + 1))
}
