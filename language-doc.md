# DaRo

Daro is a weakly typed language. It has been designed to be executed using a interpreter. Files
containing DaRo source code should use the ending `.daro` to identify their type.

## Example

A simple "FizzBuzz"-program in the daro language might look like this:

```
fn fizzBuzz(n) {
    match 0 {
        n % 15: "FizzBuzz"
        n % 3: "Fizz"
        n % 5: "Buzz"
        default: n
    }
}

i = 1;
for i <= 100 {
    println(fizzBuzz(i))
    i += 1;
}
```

## General

The execution of a piece of code in DaRo goes general from top to bottom and from left to right.
When execution code, all statements on the root level (i.e. not inside function or class
definitions) will be executed. Statements can be separated using the `;` character. Statements that
are not separated by `;` will not cause an error.

Multiple statements can be collected into a single statement by using either parenthesis (e.g. `(x = 5; y = 3)`)
or braces (e.g. `{x = 5; y = 3}`).  When using braces a new scope will be created that will contain
all new variables. Variables created inside the braces will not be accessible  to other statements
outside them. Statements bundled using simple parenthesis, however, execute directly in the parent
scope leaving new variables accessible to other statements outside.

## Variables

Variables are identified by a name starting with a letter or underscore followed by zero or more
letters, numbers or underscores. A variable can store values of any type supported by DaRo. A
variable can also change the type it holds during execution. To assign a value to a variable use the
simple `=` operator with the variable name on the left hand side and the value, that should be stored
into the location, on the right side. E.g. `x = 42`

## Control structures

The DaRo language includes four kinds of control structures:
* The `if-else`-statement
* The `for`-statement
* The`for-in`-statement, and
* The `match`-statement

### `if` and `else`

This statement has the following forms:
* `if condition if_block else else_block`
* `if condition if_block`

The code inside the if_block will only be executed iff the condition evaluates to a truthy value.
Truthy values include the boolean `true`, non-zero integers and floating point numbers and any array
or class object. If a else branch is present the code inside the else_block is executed iff the
condition evaluates to a falsy value.
The `if-else`-statement will return the value of the block that was executed. This can be used as a
way to replace the tertiary operator which is not present in DaRo:
```
x = if y == 0 { 1 } else { 2 }
``'

### `for`

The for statement has the following 


