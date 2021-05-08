grammar PyType;

options {
  output=AST;
  ASTLabelType=CommonTree;
}

tokens {
    OPEN_SQ_BR = '[';
    CLOSE_SQ_BR = ']';
    LIST = 'List';
    SET = 'Set';
    UNION = 'Union';
    DICT = 'Dict';
    TUPLE = 'Tuple';
    TYPE = 'Type';
    COMMA = ',';
    CALLABLE = 'Callable';
 /*   Nothing = 'nothing'; */
    OPTIONAL = 'Optional';
    HYPHEN = '-' ;
    UNDERSCORE = '_' ;
    DOT = '\.';
    EMPTY_TUPLE = '()';
    THREE_DOTS = '...';
}

@members {
    public static void main(String[] args) throws Exception {
        python3.typeinference.antlr.PyTypeLexer lex = new python3.typeinference.antlr.PyTypeLexer(new ANTLRFileStream(args[0]));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        python3.typeinference.antlr.PyTypeParser parser = new python3.typeinference.antlr.PyTypeParser(tokens);

        try {
            parser.expr();
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }
    }
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/
parse
    :  expr
    ;

expr : list_expr
    | EMPTY_TUPLE
    | THREE_DOTS
    | set_expr
    | SimpleType
    | union_expr
    | callable_expr
    | dict_expr
    | tuple_expr
    | optional_expr
    | type_expr
    | generic_expr;

list_element
    : OPEN_SQ_BR expr CLOSE_SQ_BR -> expr
    ;



union_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

callable_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

dict_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

tuple_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

optional_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

type_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

generic_element
    : OPEN_SQ_BR expr (COMMA expr)*  CLOSE_SQ_BR -> expr+;

list_expr
    : LIST^ list_element*;

set_expr
    : SET^ list_element*;

union_expr
    : UNION^ union_element*;

callable_expr
    : CALLABLE^ callable_element;

dict_expr
    : DICT^ dict_element;

tuple_expr
    : TUPLE^ tuple_element  ;

optional_expr
    : OPTIONAL^ optional_element;

type_expr
    : TYPE^ type_element;

generic_expr
    : SimpleType^ generic_element;

DIGIT  : '0'..'9' ;

LETTER : 'a'..'z' |'A'..'Z'|'0'..'9'|'_' ;

NUMBER : DIGIT+ ;

SimpleType : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'.')*('a'..'z'|'A'..'Z'|'_'|'0'..'9')
           ;




/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+    { $channel = HIDDEN; } ;

