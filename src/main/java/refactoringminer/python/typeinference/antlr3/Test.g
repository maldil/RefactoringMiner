grammar Test;

options {
  output=AST;
  ASTLabelType=CommonTree;
}

parse
  :  orExp
  ;

orExp
  :  andExp ('||'^ andExp)* // Make `||` root
  ;

andExp
  :  atom ('&&'^ atom)* // Make `&&` root
  ;

atom
  :  'true'
  |  'false'
  |  '(' orExp ')' -> orExp // Just a single token, no need to do `^(...)`,
                            // we're removing the parenthesis. Note that
                            // `'('! orExp ')'!` will do exactly the same.
  ;

// ignore white space characters
Space
  :  (' ' | '\t' | '\r' | '\n') {$channel=HIDDEN;}
  ;

