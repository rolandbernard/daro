# DaRo

## About

### Group Members

* Roland Bernard, rolbernard@unibz.it
* Daniel Planötscher, dplanoetscher@unibz.it

### Project Idea

We plan to make a game that teaches people the basics of coding using a simple, self-designed
programming language. The idea is to create simple puzzles with increasing difficulty that ask the
player to solve programming-related problems, such as finding the minimum in an array, creating sums
etc.
In addition to the game, the language can also be used stand-alone using the cli interface of the
interpreter.

## Getting Started

### How to Test

To run all the tests for this application, execute the maven command `mvn test`. This will
automatically run all the defined test using jUnit.

### How to Build

The project can be build using the maven command `mvn package`. This will create three runnable jars
in the `target` directory:
* `target/daro-1.0.0-game.jar` when executed will launch our game
* `target/daro-1.0.0-ide.jar` when executed will launch a small IDE for programming in the DaRo
* `target/daro-1.0.0-cli.jar` when executed will launch a small REPL (Read-Eval-Print-Loop) program

### How to Run

You can run the generated jar files using the Java VM by executing for example `java -jar
target/daro-1.0.0-ide.jar` after having executed `mvn package`. If you just want to execute the
application without first packaging it, you can execute `mvn javafx:run` to execute the game and
`mvn exec:java` to execute the REPL program.

### How to Use

We split the documentation on how to use the software into four parts:
* [Language documentation](docs/language.md)
* [CLI documentation](docs/cli.md)
* [IDE documentation](docs/ide.md)
* [Game documentation](docs/game.md)

## Implementation

### General

The implementation is split into three packages that roughly correspond to the jars generated when
executing the package goal:
* `daro.game` Includes the code for the game
* `daro.ide` Includes the code for the IDE
* `daro.lang` Includes the code for the interpreter and simple CLI program

### Libraries

For the implementation of this project we used the following third-party libraries:
* [JUnit](https://junit.org/junit5/) for the test that execute when running `mvn test`
* [JavaFX](https://openjfx.io/) to implement the graphical user interface
* [RichTextFX](https://github.com/FXMisc/RichTextFX) to as a base implementation for the code editor
* [json-simple](https://code.google.com/archive/p/json-simple/) to serialize and deserialize JSON data

### Programming techniques

#### Interfaces

#### Abstract classes

#### Generics

#### Collections

#### Custom exceptions

#### Exception handling

#### Method overriding

#### Method overloading

#### Lambdas

#### Streams

#### Optionals

#### File I/O

#### Serialization

#### Deserialization

#### Regular expressions

#### Thread signaling

## Experience

### Organization
<!-- TODO -->

### Git usage
<!-- TODO -->

### Main challenges
<!-- TODO -->

