package mapexercise

// NOTE: Map Practice, implement each function using Scala's immutable Map.
// These mirror the methods that you many need for your Lettuce environment:
// looking up a variable, check existence, etc.
//
// TODO: Replace ??? with your implementation.

object MapExercise {

  // NOTE: Return the value for `key`, or -1 if the key is absent.
  // Hint: contains / apply, or getOrElse.
  
  def lookupOrDefault(m: Map[String, Int], key: String): Int = ???

  // NOTE: Add (or overwrite) the binding key -> value, returning the NEW map.
  // The original map must remain unchanged.
  
  def addBinding(m: Map[String, Int], key: String, value: Int): Map[String, Int] = ???

  // NOTE: Return true if `key` is bound in the map, false otherwise.
  
  def isBound(m: Map[String, Int], key: String): Boolean = ???

  // NOTE: Remove `key` from the map, returning the NEW map.
  // If the key isn't present, return the map unchanged.
  
  def removeBinding(m: Map[String, Int], key: String): Map[String, Int] = ???

  // NOTE: Given two maps, merge them so that bindings in `b` override `a`.
  // on any shared keys.
  def extend(a: Map[String, Int], b: Map[String, Int]): Map[String, Int] = ???

  // NOTE: Return how many keys the map contains.
  def size(m: Map[String, Int]): Int = ???

  // NOTE: Increment the value at `key` by 1. If the key is absent, set it to 1.
  def increment(m: Map[String, Int], key: String): Map[String, Int] = ???
}
