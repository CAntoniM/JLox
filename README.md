# JLox

This is a implementation of the jlox compiler and runtime from
[crafting interpreters](https://craftinginterpreters.com/) written in java.
Used as a personal development project.

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
assignment = IDENIFIER , "=" , assignment | eqaulity;
equality = comparision, { ("!=" | "==" ) , comparison };
comparison = term, { ( ">" | ">=" | "<" | "<=" ) term };
term = factor, { ( "-" | "+" ) factor } ;
factor = unary { ( "/" | "*" ) unary };
unary = ("!" | "-" ) unary | primary ;
primary = number | string | "true" | "false" | "nil" | "(", expression ,")" ;

grouping = "(" , expression , ")" ;
binary = expression , operator , expression ;

expression_statement = expression , ';'

print_statement = "print", expression, ";";

block_statement = "{", {declaration,} "}"

statement = expression_statement | print_statement | block_statement;

variable_declaration = "var", identifier, [ "=" , expression, ] ":" ;

declaration = variable_declaraion | statement;

program = {declaration} , EOF;

```

