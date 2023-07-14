# JLox

This is a implementation of the jlox compiler and runtime from
[crafting interpreters](https://craftinginterpreters.com/) written in java.
Used as a personal development project. With a goal of being able to understand
compilers, how they are designed and implemented.

## Usage

the jlox interpeter runs in one of two modes the first of this is a REPL mode.
where you simply run the application and it will provide a interactable prompt

```bash
java -jar jlox.jar
```

or you can run a lox script by running the following command:

```bash
java -jar jlox.jar script.lox
```

## Project structure

This project uses a maven as its build tool, maven while not the most popular
build tool (see src/main/java/me/cantonim/jlox/) it does massivly simplify the
building and adding dependancies to the project and that is worth its weight in
gold as far as i am concerened. to run the build run the command

```bash
mvn package
```

This will build, test and package the jlox jar in
target/jlox-<Version>-jar-with-dependencies.jar. We use the with dependances jar
as we want to ship as a stand alone executable without worrying about java
class paths or even class names (see the fact we ship a runnable jar).

### tools/Generator.java

this is a small standalone java application used for generating out the
Statement.java and Expression.java files. we do this as they use alot of
repeatitive code that needs to be added onto as we develop the language
add more features.

As such if you need to add a new statment or expression type to the syntax tree
of the application please modify the relavent defineAST methods called inside
of the src and then run.

```bash
java Generate ../src/main/me/cantonim/jlox/
```

## Feature Status

as we developing an intrepterter for a already defiend language there is a fixed
set of lanauge functonality that we need to set ourselfs too. below is a list of
the language features defined at
[Crafting Interpeters](https://craftinginterpreters.com/the-lox-language.html)

- [x] arithmetic expression
- [x] boolean logic expression
- [x] variable statements
- [x] variable scope
- [x] print statements
- [x] assignment expressions
- [x] block statements
- [x] if statements
- [ ] while statments
- [ ] function statements
- [ ] classes
- [ ] closures

## Language theory

Below is a ebnf representation of the grammer of the lanaguage this will be a
translation of lox's own context free grammer abstraction as ebnf is way more
common to see in other languages not just lox and therefore is way more useful
to me

```ebnf
digit ="1"|"2"|"3"|"4"|"5"|"6"|"7"|"8"|"9"|"0";

alpha ="a"|"b"|"c"|"d"|"e"|"f"|"g"|"h"|"i"|"j"|"k"|"l"|"m"|"n"|"o"|"p"|"q"|"r"|
"s"|"t"|"u"|"p"|"w"|"x"|"y"|"z"|"A"|"B"|"C"|"D"|"E"|"F"|"G"|"H"|"I"|"J"|"K"|"L"|
"M"|"N"|"O"|"P"|"Q"|"R"|"S"|"T"|"U"|"P"|"W"|"X"|"Y"|"Z"|"_";

symbol ="`"|"~"|"!"|"@"|"#"|"$"|"%"|"^"|"&"|"*"|"("|")"|"-"|"+"|"="|"{"|"}"|
"["|"]"|"|"|"\\"|";"|":"|"'"|"<"|">"|","|"."|"/"|"?";

operator ="=="|"!="|"<"|"<="|">"|">="|"+"|"="|"*"|"/";

number = digit , [ "." , digit , { digit } ];
string = "\"" , { digit | alpha | symbol } , "\"";
identifier = alpha, [{digit | alpha}] ;
litteral = number | string | identifier | "true" | "false" | "nil" ;

expression = assignment;
assignment = IDENIFIER , "=" , assignment | logical_or;
logical_or = logical_and {, "or", logical_and};
logical_and = eqaulaity {, "and", logical_and};
equality = comparision, { ("!=" | "==" ) , comparison };
comparison = term, { ( ">" | ">=" | "<" | "<=" ) term };
term = factor, { ( "-" | "+" ) factor } ;
factor = unary { ( "/" | "*" ) unary };
unary = ("!" | "-" ) unary | primary ;
primary = number | string | "true" | "false" | "nil" | "(", expression ,")" ;

grouping = "(" , expression , ")" ;
binary = expression , operator , expression ;

expression_statement = expression , ';';

print_statement = "print", expression, ";";

block_statement = "{", {declaration,} "}";

variable_declaration = "var", identifier, [ "=" , expression, ] ":" ;

declaration = variable_declaraion | statement;

if_statement = "if" , "(" , expression , ")" , statement [, "else", statement] ;
while_statement = "while", "(", expression ,")" , statement;
for_statement = "for", "(", (expression_statement|variable_declaration), ";" , [expression,] ";", [expression,] ")", statement;

statement = expression_statement |
            print_statement      |
            block_statement      |
            if_statement         |
            while_statement      |
            for_statement        |;

program = {declaration} , EOF;

```

