<div align="center">

# Lettuce -- the Variables & the Environment <img src="https://www.scala-lang.org/resources/img/scala-spiral.png" height="32" align="center" />

**A basic Interpreter built in Scala**

_An introduction to operational sematics: values vs. expressions, basic error handling,_
_the environment, variable binding (`let`), lookup (`id`), conditionals, and the short_
_circuting of `and`_

[![Scala](https://img.shields.io/badge/Scala-3.3.7_LTS-DC322F?logo=scala&logoColor=white)](https://www.scala-lang.org/)
[![sbt](https://img.shields.io/badge/sbt-1.12.11-blue)](https://www.scala-sbt.org/)
[![ScalaTest](https://img.shields.io/badge/ScalaTest-3.2.19-success)](https://www.scalatest.org/)
[![Tests](https://github.com/haos8244/vars-and-env-scala/actions/workflows/test.yml/badge.svg)](../../actions)

</div>

---

## What is Lettuce all about?

Lettuce is a small expression language, taken from the fundamentals of _OCaml_.
This repo implements **big-step operational semantics** in Scala using an `eval`
function that takes an expression and an environment and returns a value. The 
interpreter is built up **incrementally**, one additional language feature per
problem package, so each step is small, self-contained and fully tested.

The idea is that the **environment**, being a `Map[String, Value]` lets expressions
bind and look up variables. `let` writes to it, `id` reads from it, and immutability
gives inheret lexical scoping.

```scala
// let x = 2 in
//   let y = (let x = x + 1 in x + 1) in
//     x + y
//   ⇒ 6   (the inner x shadows; the outer x stays 2)
Let("x", Const(2),
  Let("y",
    Let("x", Plus(Ident("x"), Const(1)), Plus(Ident("x"), Const(1))),
    Plus(Ident("x"), Ident("y")))).eval(Map())   // = NumValue(6)
```

---

## The Language

### Abstract syntax 

| Expression            | Meaning                          |
| --------------------- | -------------------------------- |
| `Const(n)`            | integer literal                  |
| `Plus(e1, e2)`        | `e1 + e2`                        |
| `Gt(e1, e2)`          | `e1 > e2` (strict)               |
| `Ident(id)`           | use a variable                   |
| `Let(id, e1, e2)`     | `let id = e1 in e2`              |
| `IfThenElse(c, t, f)` | `if (c) t else f`                |
| `And(e1, e2)`         | `e1 && e2` (short-circuiting)    |

### Values 
| Value          | Meaning                                  |
| -------------- | ---------------------------------------- |
| `NumValue(n)`  | a number result                          |
| `BoolValue(b)` | a boolean result                         |
| `ErrorValue`   | a type mismatch or unbound variable      |

**The judgment** &rarr; `eval(e, env) = v` p&rarr; _"expression `e`, under environment `env`,_
_produces value `v`."_

---

## Project Layout

Each language feature exists in its own directory, essentially in Scala a package,
so that there are no collisions between names of `traits`, `classes`, `objects`,
etc. Hence, redefinition is independent and tested in isolation with corresponding
test cases

```
src/
├── main/scala/
│   ├── problem1/   Const
│   ├── problem2/   + Plus
│   ├── problem3/   + Gt
│   ├── problem4/   + errors (ErrorValue)
│   ├── problem5/   + the environment (eval gains env)
│   ├── problem6/   + Ident & Let
│   ├── problem7/   + IfThenElse
│   ├── problem8/   + And (short-circuit)
│   └── capstone/   everything, as a fill-in exercise / do it from spec
└── test/scala/
    └── …/          a matching test suite per package
```

---

## Quick Start

```bash
# Start up the JVM
sbt

# run just one problem (compiles diff, isolated)
> testOnly problem[1/2/3/4/5/6/7/8].*
```

> :bulb: **Tip:** keep one `sbt` shell open for the session &rarr; the JVM stays warm and
> incremental compilation makes the edit &rarr; test loop nearly instant.

---

## The Capstone

`capstone/` is a self-contained exercise, a full spec in the header comment, a
blank `eval` to implement, and a complete test suite that validates the specifications.

```bash
sbt
> testOnly capstone.*
```

---

## Instructor vs. Student Copies

Two tags mark two commits: **`student`** (blanks to fill in) and **`instructor`**
(filled-in solutions).
 
```bash
# start working from the blanks (on your own editable branch)
git switch -c mywork student
 
# fill in the ???s, then run the tests
sbt "testOnly problem6.*"
 
# peek at the answer key, then go back to your work
git switch --detach instructor   # look only (read-only)
git switch mywork                # back to editing
```
 
> :bulb: `git switch -c mywork student` makes a branch named `mywork` starting from the blank
> `student` commit, edit there. The `student` tag stays the same if you
> ever want to start over.

---

## Tips

- **Hunt for `???` and `FIXME:`**, these mark the spots you need to fill in.
  `???` is real Scala (it compiles but throws `NotImplementedError`), so any unfinished
  case fails its test until you replace it. In Neovim, `todo-comments.nvim` highlights
  `TODO` / `FIXME` / `HACK` and lets you jump between them.
```scala
  case Let(id, e1, e2) => ??? // FIXME:
```
- **Keep one `sbt` shell open.** Launching `sbt` every time makes JVM start up every time. 
  Start it once, then run commands at the `>` prompt.
- **Use watch mode.** Inside the shell, `~testOnly problem6.*` re-runs that problem's
  tests automatically on every save. Write code → save → glance at green/red.
- **You don't need to compile manually.** `testOnly` compiles first, and only recompiles
  what changed. Skip `clean` unless something seems genuinely stuck — it forces a slow
  full rebuild.
- **`???` keeps the build alive.** An _unimplemented_ case (with `???`) still compiles, so
  it won't block other problems. A _syntax/type error_ anywhere **will** block the whole
  build. If `testOnly problem6.*` suddenly won't start, check the compile error; it's
  often in a different package.
- **One package per problem = isolation.** `problem6.Const` and `problem7.Const` are
  different types, so each problem redefines `Expr` / `Value` / `eval` freely. Target one
  with `testOnly problem6.*`.
- **Run a single test case** by name with ScalaTest's `-z` filter:
```bash
  sbt "testOnly problem6.* -- -z \"shadowing\""
```

---

## Concepts Covered

- **Values vs. Expressions**: Why `Value` is decoupled from `Expr` (where `ErrorValue` comes in)
- **Error Handling**: Type mismatches produce `ErrorValue`, they propogate up
- **The Environment**: `Map[String, Value]`, having immutability for lexical scoping
- **Binding & Lookup**: `let` extends the environment, `id` reads it back
- **Shadowing**: Inner bindings override outer ones, only in their scope
- **Short-Circuiting**: `and` skips its right operand when the left is `false`

---

_Built for CSCI &middot; Principles of Programming Languages_

</div>
