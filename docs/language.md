# DaRo Language

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

## Operators

The DaRo language includes the following operators. In the table operators at the bottom have a
higher precedence.

| Operators | Note |
| --------- | ---- |
| `=` `+=` `-=` `*=` `/=` `%=` `<<=` `>>=` `\|=` `&=` `^=` | Assignments |
| `\|\|` | Lazy  Or |
| `&&` | Lazy And |
| `==` `!=` `>` `>=` `<` `<=` | Comparisons |
| `\|` | Bitwise Or |
| `^` | Bitwise Xor |
| `&` | Bitwise And |
| `>>` `<<` | Shift |
| `+` `-` | |
| `*` `/` `%` | |
| `**` | Power |
| `+` `-` `[]` `~` `!` | Unary prefix |
| `()` `[]` `.` | Unary suffix |


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
way to replace the tertiary operator which is not present in DaRo.

###### Examples

```
if y == 0 {
    x = 1;
} else {
    x = 2;
}

// This can also be done using the if as an expression
x = if (y == 0) 1 else 2
```

### `for`

The `for`-statement has the following form:
* `for condition block`

The code inside block will be executed for as long as the condition evaluates to true. The condition
is executed before every execution of block and if it resolves to a falsy value the for statement
exists.

###### Examples

```
// Computing the sum of all integers between 1 and 100
sum = 0;
i = 1;
for i <= 100 {
    sum += i;
    i++;
}
```

### `for` and `in`

The `for-in`-statement has the following form:
* `for variable in array block`

The code inside block will be executed for every element inside the array. For every run the
variable is set to a different element of the array.

###### Examples

```
// Compute the sum of all elements in arr
sum = 0;
arr = new array { 1, 2, 3, 4, 5, 6, 7, 8, 10 }
for i in arr {
    sum += i;
}
```

### `match`

The `match`-statement has the following form:
* `match value { value1: block1   value2, value3: block2   default: block3 }`

The `match` statement will evaluate first the value and than compare the resulting object with all
of the possible conditions from start to end. If one of the cases (e.g. `value2`) matches the given
value the corresponding block is executed and the match statement breaks. A value of `default` will
match all possible values.  If non of the cases match the statement will not execute any of the
blocks.

###### Examples

```
match x {
    1: println("one")
    2: println("two")
    3: println("three")
    default: println(x)
}

// The match can also be used as a expression
println(match x { 1: "one" 2: "two" 3: "three" default: x });
```

## Functions

In DaRo functions can be defined using the following form:
* `fn name(argument1, argument2) block`

This statement will create a value of the function type and assign it to a variable with the given
name. Functions can also be anonymous if they are not given a name. The return value of the function
definition is the created function object.

A function call has the following form:
* `name(parameter1, parameter2)`

When a function is called it must be called with the same number of parameters it was defined for.
The call statement will return the value that was returned by the function after execution.
Functions can return values using the `return` statement.  If no return statement is used the
function will return the value of its block. Before execution of the function block the arguments
will be assigned the given parameters the function was called with.

###### Examples

```
fn add(x, y) {
    return x + y;
}

// The return statement can be removed
fn add(x, y) {
    x + y
}

// The function can be anonymous
add = fn (x, y) {
    x + y
}

// Calling works the same for all of these
a = add(20, 22);
```

## Classes

Class definition in DaRo have the following form:
* `class name { body }`

A class has its own collection of variables that is not directly accessible from outside a class
instance. A class can be instantiated using the `new` statement. When instantiating the class the
body of the class will be executed in the objects internal scope. To access the variables inside of a
class instance from the outside the `.` operator can be used. All functions inside the class will be
executed inside the class scope. In addition to the user defined variables all class objects also
contain the variable `this` referring to the instance of the class.

###### Examples

```
class Test {
    x = 5;
    y = 5;

    fn z (x) {
        return x + this.x + y
    }
}

x = new Test
x.x = 6
y = x.z(7) // y is now 5 + 6 + 7 = 18

// Classes can also be anonymous
Test = class { x = 5; y = 5 }
```

## Modules

The DaRo language has some module support. Modules can be loaded using the `from` statement. If the
same file has already been loaded once the same module object will be returned.
If the variables inside of a module should be accessible directly the `use` statement can be used to
make all of the variables inside the module accessible directly in the surrounding scope.

###### Examples

```
// This will load and execute the code inside the file test.daro in a module and return the module
// after successful execution
mod = from "test.daro";
mod.x = 5;

// Because this file has been loaded before, the same module will be returned
use from "test.daro";
x // x is now accessible like this
```

## Java integration

DaRo has the ability to use Java Objects and execute Java methods on those objects. When using the
default configuration all loaded java packages can be accessed directly from DaRo. For example the
variable `java` is defined by default inside DaRo and contains as variable all packages and classes
whose name starts with `java.`.

###### Examples

```
// This will return an Object containing all the objects and packages inside java.util
x = java.util;

// This will create a new instance of the Java HashMap class
y = new x.HashMap;

// This will execute the Java method of the HashMap object
y.put("test", 5)

// The use statement can also be used with Java classes or packages
use java.lang;

// Static fields can also be accessed
System.out.println("Hello world");

// Static methods on Java classes can be executed
z = Math.sqrt(2);
```

