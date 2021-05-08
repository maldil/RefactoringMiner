// $ANTLR 3.5.2 PyType.g 2021-03-04 12:38:42
package  refactoringminer.python.typeinference.antlr;
import org.antlr.runtime.*;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class PyTypeParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "CALLABLE", "CLOSE_SQ_BR", "COMMA", 
		"DICT", "DIGIT", "DOT", "EMPTY_TUPLE", "HYPHEN", "LETTER", "LIST", "NUMBER", 
		"OPEN_SQ_BR", "OPTIONAL", "SET", "SimpleType", "THREE_DOTS", "TUPLE", 
		"TYPE", "UNDERSCORE", "UNION", "WHITESPACE"
	};
	public static final int EOF=-1;
	public static final int CALLABLE=4;
	public static final int CLOSE_SQ_BR=5;
	public static final int COMMA=6;
	public static final int DICT=7;
	public static final int DIGIT=8;
	public static final int DOT=9;
	public static final int EMPTY_TUPLE=10;
	public static final int HYPHEN=11;
	public static final int LETTER=12;
	public static final int LIST=13;
	public static final int NUMBER=14;
	public static final int OPEN_SQ_BR=15;
	public static final int OPTIONAL=16;
	public static final int SET=17;
	public static final int SimpleType=18;
	public static final int THREE_DOTS=19;
	public static final int TUPLE=20;
	public static final int TYPE=21;
	public static final int UNDERSCORE=22;
	public static final int UNION=23;
	public static final int WHITESPACE=24;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public PyTypeParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public PyTypeParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return PyTypeParser.tokenNames; }
	@Override public String getGrammarFileName() { return "PyType.g"; }


	    public static void main(String[] args) throws Exception {
	        PyTypeLexer lex = new PyTypeLexer(new ANTLRFileStream(args[0]));
	        CommonTokenStream tokens = new CommonTokenStream(lex);

	        PyTypeParser parser = new PyTypeParser(tokens);

	        try {
	            parser.expr();
	        } catch (RecognitionException e)  {
	            e.printStackTrace();
	        }
	    }


	public static class parse_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "parse"
	// PyType.g:46:1: parse : expr ;
	public final parse_return parse() throws RecognitionException {
		parse_return retval = new parse_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope expr1 =null;


		try {
			// PyType.g:47:5: ( expr )
			// PyType.g:47:8: expr
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_expr_in_parse228);
			expr1=expr();
			state._fsp--;

			adaptor.addChild(root_0, expr1.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parse"


	public static class expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// PyType.g:50:1: expr : ( list_expr | EMPTY_TUPLE | THREE_DOTS | set_expr | SimpleType | union_expr | callable_expr | dict_expr | tuple_expr | optional_expr | type_expr | generic_expr );
	public final expr_return expr() throws RecognitionException {
		expr_return retval = new expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token EMPTY_TUPLE3=null;
		Token THREE_DOTS4=null;
		Token SimpleType6=null;
		ParserRuleReturnScope list_expr2 =null;
		ParserRuleReturnScope set_expr5 =null;
		ParserRuleReturnScope union_expr7 =null;
		ParserRuleReturnScope callable_expr8 =null;
		ParserRuleReturnScope dict_expr9 =null;
		ParserRuleReturnScope tuple_expr10 =null;
		ParserRuleReturnScope optional_expr11 =null;
		ParserRuleReturnScope type_expr12 =null;
		ParserRuleReturnScope generic_expr13 =null;

		CommonTree EMPTY_TUPLE3_tree=null;
		CommonTree THREE_DOTS4_tree=null;
		CommonTree SimpleType6_tree=null;

		try {
			// PyType.g:50:6: ( list_expr | EMPTY_TUPLE | THREE_DOTS | set_expr | SimpleType | union_expr | callable_expr | dict_expr | tuple_expr | optional_expr | type_expr | generic_expr )
			int alt1=12;
			switch ( input.LA(1) ) {
			case LIST:
				{
				alt1=1;
				}
				break;
			case EMPTY_TUPLE:
				{
				alt1=2;
				}
				break;
			case THREE_DOTS:
				{
				alt1=3;
				}
				break;
			case SET:
				{
				alt1=4;
				}
				break;
			case SimpleType:
				{
				int LA1_5 = input.LA(2);
				if ( (LA1_5==EOF||(LA1_5 >= CLOSE_SQ_BR && LA1_5 <= COMMA)) ) {
					alt1=5;
				}
				else if ( (LA1_5==OPEN_SQ_BR) ) {
					alt1=12;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 1, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case UNION:
				{
				alt1=6;
				}
				break;
			case CALLABLE:
				{
				alt1=7;
				}
				break;
			case DICT:
				{
				alt1=8;
				}
				break;
			case TUPLE:
				{
				alt1=9;
				}
				break;
			case OPTIONAL:
				{
				alt1=10;
				}
				break;
			case TYPE:
				{
				alt1=11;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}
			switch (alt1) {
				case 1 :
					// PyType.g:50:8: list_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_list_expr_in_expr241);
					list_expr2=list_expr();
					state._fsp--;

					adaptor.addChild(root_0, list_expr2.getTree());

					}
					break;
				case 2 :
					// PyType.g:51:7: EMPTY_TUPLE
					{
					root_0 = (CommonTree)adaptor.nil();


					EMPTY_TUPLE3=(Token)match(input,EMPTY_TUPLE,FOLLOW_EMPTY_TUPLE_in_expr249); 
					EMPTY_TUPLE3_tree = (CommonTree)adaptor.create(EMPTY_TUPLE3);
					adaptor.addChild(root_0, EMPTY_TUPLE3_tree);

					}
					break;
				case 3 :
					// PyType.g:52:7: THREE_DOTS
					{
					root_0 = (CommonTree)adaptor.nil();


					THREE_DOTS4=(Token)match(input,THREE_DOTS,FOLLOW_THREE_DOTS_in_expr257); 
					THREE_DOTS4_tree = (CommonTree)adaptor.create(THREE_DOTS4);
					adaptor.addChild(root_0, THREE_DOTS4_tree);

					}
					break;
				case 4 :
					// PyType.g:53:7: set_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_set_expr_in_expr265);
					set_expr5=set_expr();
					state._fsp--;

					adaptor.addChild(root_0, set_expr5.getTree());

					}
					break;
				case 5 :
					// PyType.g:54:7: SimpleType
					{
					root_0 = (CommonTree)adaptor.nil();


					SimpleType6=(Token)match(input,SimpleType,FOLLOW_SimpleType_in_expr273); 
					SimpleType6_tree = (CommonTree)adaptor.create(SimpleType6);
					adaptor.addChild(root_0, SimpleType6_tree);

					}
					break;
				case 6 :
					// PyType.g:55:7: union_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_union_expr_in_expr281);
					union_expr7=union_expr();
					state._fsp--;

					adaptor.addChild(root_0, union_expr7.getTree());

					}
					break;
				case 7 :
					// PyType.g:56:7: callable_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_callable_expr_in_expr289);
					callable_expr8=callable_expr();
					state._fsp--;

					adaptor.addChild(root_0, callable_expr8.getTree());

					}
					break;
				case 8 :
					// PyType.g:57:7: dict_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_dict_expr_in_expr297);
					dict_expr9=dict_expr();
					state._fsp--;

					adaptor.addChild(root_0, dict_expr9.getTree());

					}
					break;
				case 9 :
					// PyType.g:58:7: tuple_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_tuple_expr_in_expr305);
					tuple_expr10=tuple_expr();
					state._fsp--;

					adaptor.addChild(root_0, tuple_expr10.getTree());

					}
					break;
				case 10 :
					// PyType.g:59:7: optional_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_optional_expr_in_expr313);
					optional_expr11=optional_expr();
					state._fsp--;

					adaptor.addChild(root_0, optional_expr11.getTree());

					}
					break;
				case 11 :
					// PyType.g:60:7: type_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_type_expr_in_expr321);
					type_expr12=type_expr();
					state._fsp--;

					adaptor.addChild(root_0, type_expr12.getTree());

					}
					break;
				case 12 :
					// PyType.g:61:7: generic_expr
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_generic_expr_in_expr329);
					generic_expr13=generic_expr();
					state._fsp--;

					adaptor.addChild(root_0, generic_expr13.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expr"


	public static class list_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_element"
	// PyType.g:63:1: list_element : OPEN_SQ_BR expr CLOSE_SQ_BR -> expr ;
	public final list_element_return list_element() throws RecognitionException {
		list_element_return retval = new list_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR14=null;
		Token CLOSE_SQ_BR16=null;
		ParserRuleReturnScope expr15 =null;

		CommonTree OPEN_SQ_BR14_tree=null;
		CommonTree CLOSE_SQ_BR16_tree=null;
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:64:5: ( OPEN_SQ_BR expr CLOSE_SQ_BR -> expr )
			// PyType.g:64:7: OPEN_SQ_BR expr CLOSE_SQ_BR
			{
			OPEN_SQ_BR14=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_list_element341);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR14);

			pushFollow(FOLLOW_expr_in_list_element343);
			expr15=expr();
			state._fsp--;

			stream_expr.add(expr15.getTree());
			CLOSE_SQ_BR16=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_list_element345);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR16);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 64:35: -> expr
			{
				adaptor.addChild(root_0, stream_expr.nextTree());
			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_element"


	public static class union_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "union_element"
	// PyType.g:67:1: union_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final union_element_return union_element() throws RecognitionException {
		union_element_return retval = new union_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR17=null;
		Token COMMA19=null;
		Token CLOSE_SQ_BR21=null;
		ParserRuleReturnScope expr18 =null;
		ParserRuleReturnScope expr20 =null;

		CommonTree OPEN_SQ_BR17_tree=null;
		CommonTree COMMA19_tree=null;
		CommonTree CLOSE_SQ_BR21_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:68:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:68:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR17=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_union_element366);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR17);

			pushFollow(FOLLOW_expr_in_union_element368);
			expr18=expr();
			state._fsp--;

			stream_expr.add(expr18.getTree());
			// PyType.g:68:23: ( COMMA expr )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==COMMA) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// PyType.g:68:24: COMMA expr
					{
					COMMA19=(Token)match(input,COMMA,FOLLOW_COMMA_in_union_element371);  
					stream_COMMA.add(COMMA19);

					pushFollow(FOLLOW_expr_in_union_element373);
					expr20=expr();
					state._fsp--;

					stream_expr.add(expr20.getTree());
					}
					break;

				default :
					break loop2;
				}
			}

			CLOSE_SQ_BR21=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_union_element378);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR21);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 68:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "union_element"


	public static class callable_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "callable_element"
	// PyType.g:70:1: callable_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final callable_element_return callable_element() throws RecognitionException {
		callable_element_return retval = new callable_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR22=null;
		Token COMMA24=null;
		Token CLOSE_SQ_BR26=null;
		ParserRuleReturnScope expr23 =null;
		ParserRuleReturnScope expr25 =null;

		CommonTree OPEN_SQ_BR22_tree=null;
		CommonTree COMMA24_tree=null;
		CommonTree CLOSE_SQ_BR26_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:71:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:71:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR22=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_callable_element395);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR22);

			pushFollow(FOLLOW_expr_in_callable_element397);
			expr23=expr();
			state._fsp--;

			stream_expr.add(expr23.getTree());
			// PyType.g:71:23: ( COMMA expr )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==COMMA) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// PyType.g:71:24: COMMA expr
					{
					COMMA24=(Token)match(input,COMMA,FOLLOW_COMMA_in_callable_element400);  
					stream_COMMA.add(COMMA24);

					pushFollow(FOLLOW_expr_in_callable_element402);
					expr25=expr();
					state._fsp--;

					stream_expr.add(expr25.getTree());
					}
					break;

				default :
					break loop3;
				}
			}

			CLOSE_SQ_BR26=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_callable_element407);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR26);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 71:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "callable_element"


	public static class dict_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "dict_element"
	// PyType.g:73:1: dict_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final dict_element_return dict_element() throws RecognitionException {
		dict_element_return retval = new dict_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR27=null;
		Token COMMA29=null;
		Token CLOSE_SQ_BR31=null;
		ParserRuleReturnScope expr28 =null;
		ParserRuleReturnScope expr30 =null;

		CommonTree OPEN_SQ_BR27_tree=null;
		CommonTree COMMA29_tree=null;
		CommonTree CLOSE_SQ_BR31_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:74:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:74:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR27=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_dict_element424);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR27);

			pushFollow(FOLLOW_expr_in_dict_element426);
			expr28=expr();
			state._fsp--;

			stream_expr.add(expr28.getTree());
			// PyType.g:74:23: ( COMMA expr )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==COMMA) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// PyType.g:74:24: COMMA expr
					{
					COMMA29=(Token)match(input,COMMA,FOLLOW_COMMA_in_dict_element429);  
					stream_COMMA.add(COMMA29);

					pushFollow(FOLLOW_expr_in_dict_element431);
					expr30=expr();
					state._fsp--;

					stream_expr.add(expr30.getTree());
					}
					break;

				default :
					break loop4;
				}
			}

			CLOSE_SQ_BR31=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_dict_element436);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR31);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 74:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dict_element"


	public static class tuple_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "tuple_element"
	// PyType.g:76:1: tuple_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final tuple_element_return tuple_element() throws RecognitionException {
		tuple_element_return retval = new tuple_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR32=null;
		Token COMMA34=null;
		Token CLOSE_SQ_BR36=null;
		ParserRuleReturnScope expr33 =null;
		ParserRuleReturnScope expr35 =null;

		CommonTree OPEN_SQ_BR32_tree=null;
		CommonTree COMMA34_tree=null;
		CommonTree CLOSE_SQ_BR36_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:77:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:77:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR32=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_tuple_element453);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR32);

			pushFollow(FOLLOW_expr_in_tuple_element455);
			expr33=expr();
			state._fsp--;

			stream_expr.add(expr33.getTree());
			// PyType.g:77:23: ( COMMA expr )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==COMMA) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// PyType.g:77:24: COMMA expr
					{
					COMMA34=(Token)match(input,COMMA,FOLLOW_COMMA_in_tuple_element458);  
					stream_COMMA.add(COMMA34);

					pushFollow(FOLLOW_expr_in_tuple_element460);
					expr35=expr();
					state._fsp--;

					stream_expr.add(expr35.getTree());
					}
					break;

				default :
					break loop5;
				}
			}

			CLOSE_SQ_BR36=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_tuple_element465);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR36);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 77:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "tuple_element"


	public static class optional_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "optional_element"
	// PyType.g:79:1: optional_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final optional_element_return optional_element() throws RecognitionException {
		optional_element_return retval = new optional_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR37=null;
		Token COMMA39=null;
		Token CLOSE_SQ_BR41=null;
		ParserRuleReturnScope expr38 =null;
		ParserRuleReturnScope expr40 =null;

		CommonTree OPEN_SQ_BR37_tree=null;
		CommonTree COMMA39_tree=null;
		CommonTree CLOSE_SQ_BR41_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:80:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:80:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR37=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_optional_element482);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR37);

			pushFollow(FOLLOW_expr_in_optional_element484);
			expr38=expr();
			state._fsp--;

			stream_expr.add(expr38.getTree());
			// PyType.g:80:23: ( COMMA expr )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==COMMA) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// PyType.g:80:24: COMMA expr
					{
					COMMA39=(Token)match(input,COMMA,FOLLOW_COMMA_in_optional_element487);  
					stream_COMMA.add(COMMA39);

					pushFollow(FOLLOW_expr_in_optional_element489);
					expr40=expr();
					state._fsp--;

					stream_expr.add(expr40.getTree());
					}
					break;

				default :
					break loop6;
				}
			}

			CLOSE_SQ_BR41=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_optional_element494);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR41);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 80:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "optional_element"


	public static class type_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "type_element"
	// PyType.g:82:1: type_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final type_element_return type_element() throws RecognitionException {
		type_element_return retval = new type_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR42=null;
		Token COMMA44=null;
		Token CLOSE_SQ_BR46=null;
		ParserRuleReturnScope expr43 =null;
		ParserRuleReturnScope expr45 =null;

		CommonTree OPEN_SQ_BR42_tree=null;
		CommonTree COMMA44_tree=null;
		CommonTree CLOSE_SQ_BR46_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:83:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:83:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR42=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_type_element511);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR42);

			pushFollow(FOLLOW_expr_in_type_element513);
			expr43=expr();
			state._fsp--;

			stream_expr.add(expr43.getTree());
			// PyType.g:83:23: ( COMMA expr )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==COMMA) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// PyType.g:83:24: COMMA expr
					{
					COMMA44=(Token)match(input,COMMA,FOLLOW_COMMA_in_type_element516);  
					stream_COMMA.add(COMMA44);

					pushFollow(FOLLOW_expr_in_type_element518);
					expr45=expr();
					state._fsp--;

					stream_expr.add(expr45.getTree());
					}
					break;

				default :
					break loop7;
				}
			}

			CLOSE_SQ_BR46=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_type_element523);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR46);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 83:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type_element"


	public static class generic_element_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "generic_element"
	// PyType.g:85:1: generic_element : OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ ;
	public final generic_element_return generic_element() throws RecognitionException {
		generic_element_return retval = new generic_element_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPEN_SQ_BR47=null;
		Token COMMA49=null;
		Token CLOSE_SQ_BR51=null;
		ParserRuleReturnScope expr48 =null;
		ParserRuleReturnScope expr50 =null;

		CommonTree OPEN_SQ_BR47_tree=null;
		CommonTree COMMA49_tree=null;
		CommonTree CLOSE_SQ_BR51_tree=null;
		RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
		RewriteRuleTokenStream stream_CLOSE_SQ_BR=new RewriteRuleTokenStream(adaptor,"token CLOSE_SQ_BR");
		RewriteRuleTokenStream stream_OPEN_SQ_BR=new RewriteRuleTokenStream(adaptor,"token OPEN_SQ_BR");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// PyType.g:86:5: ( OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR -> ( expr )+ )
			// PyType.g:86:7: OPEN_SQ_BR expr ( COMMA expr )* CLOSE_SQ_BR
			{
			OPEN_SQ_BR47=(Token)match(input,OPEN_SQ_BR,FOLLOW_OPEN_SQ_BR_in_generic_element540);  
			stream_OPEN_SQ_BR.add(OPEN_SQ_BR47);

			pushFollow(FOLLOW_expr_in_generic_element542);
			expr48=expr();
			state._fsp--;

			stream_expr.add(expr48.getTree());
			// PyType.g:86:23: ( COMMA expr )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==COMMA) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// PyType.g:86:24: COMMA expr
					{
					COMMA49=(Token)match(input,COMMA,FOLLOW_COMMA_in_generic_element545);  
					stream_COMMA.add(COMMA49);

					pushFollow(FOLLOW_expr_in_generic_element547);
					expr50=expr();
					state._fsp--;

					stream_expr.add(expr50.getTree());
					}
					break;

				default :
					break loop8;
				}
			}

			CLOSE_SQ_BR51=(Token)match(input,CLOSE_SQ_BR,FOLLOW_CLOSE_SQ_BR_in_generic_element552);  
			stream_CLOSE_SQ_BR.add(CLOSE_SQ_BR51);

			// AST REWRITE
			// elements: expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 86:50: -> ( expr )+
			{
				if ( !(stream_expr.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_expr.hasNext() ) {
					adaptor.addChild(root_0, stream_expr.nextTree());
				}
				stream_expr.reset();

			}


			retval.tree = root_0;

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "generic_element"


	public static class list_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "list_expr"
	// PyType.g:88:1: list_expr : LIST ^ ( list_element )* ;
	public final list_expr_return list_expr() throws RecognitionException {
		list_expr_return retval = new list_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token LIST52=null;
		ParserRuleReturnScope list_element53 =null;

		CommonTree LIST52_tree=null;

		try {
			// PyType.g:89:5: ( LIST ^ ( list_element )* )
			// PyType.g:89:7: LIST ^ ( list_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			LIST52=(Token)match(input,LIST,FOLLOW_LIST_in_list_expr569); 
			LIST52_tree = (CommonTree)adaptor.create(LIST52);
			root_0 = (CommonTree)adaptor.becomeRoot(LIST52_tree, root_0);

			// PyType.g:89:13: ( list_element )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==OPEN_SQ_BR) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// PyType.g:89:13: list_element
					{
					pushFollow(FOLLOW_list_element_in_list_expr572);
					list_element53=list_element();
					state._fsp--;

					adaptor.addChild(root_0, list_element53.getTree());

					}
					break;

				default :
					break loop9;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "list_expr"


	public static class set_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "set_expr"
	// PyType.g:91:1: set_expr : SET ^ ( list_element )* ;
	public final set_expr_return set_expr() throws RecognitionException {
		set_expr_return retval = new set_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token SET54=null;
		ParserRuleReturnScope list_element55 =null;

		CommonTree SET54_tree=null;

		try {
			// PyType.g:92:5: ( SET ^ ( list_element )* )
			// PyType.g:92:7: SET ^ ( list_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			SET54=(Token)match(input,SET,FOLLOW_SET_in_set_expr585); 
			SET54_tree = (CommonTree)adaptor.create(SET54);
			root_0 = (CommonTree)adaptor.becomeRoot(SET54_tree, root_0);

			// PyType.g:92:12: ( list_element )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==OPEN_SQ_BR) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// PyType.g:92:12: list_element
					{
					pushFollow(FOLLOW_list_element_in_set_expr588);
					list_element55=list_element();
					state._fsp--;

					adaptor.addChild(root_0, list_element55.getTree());

					}
					break;

				default :
					break loop10;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "set_expr"


	public static class union_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "union_expr"
	// PyType.g:94:1: union_expr : UNION ^ ( union_element )* ;
	public final union_expr_return union_expr() throws RecognitionException {
		union_expr_return retval = new union_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token UNION56=null;
		ParserRuleReturnScope union_element57 =null;

		CommonTree UNION56_tree=null;

		try {
			// PyType.g:95:5: ( UNION ^ ( union_element )* )
			// PyType.g:95:7: UNION ^ ( union_element )*
			{
			root_0 = (CommonTree)adaptor.nil();


			UNION56=(Token)match(input,UNION,FOLLOW_UNION_in_union_expr601); 
			UNION56_tree = (CommonTree)adaptor.create(UNION56);
			root_0 = (CommonTree)adaptor.becomeRoot(UNION56_tree, root_0);

			// PyType.g:95:14: ( union_element )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==OPEN_SQ_BR) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// PyType.g:95:14: union_element
					{
					pushFollow(FOLLOW_union_element_in_union_expr604);
					union_element57=union_element();
					state._fsp--;

					adaptor.addChild(root_0, union_element57.getTree());

					}
					break;

				default :
					break loop11;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "union_expr"


	public static class callable_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "callable_expr"
	// PyType.g:97:1: callable_expr : CALLABLE ^ callable_element ;
	public final callable_expr_return callable_expr() throws RecognitionException {
		callable_expr_return retval = new callable_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token CALLABLE58=null;
		ParserRuleReturnScope callable_element59 =null;

		CommonTree CALLABLE58_tree=null;

		try {
			// PyType.g:98:5: ( CALLABLE ^ callable_element )
			// PyType.g:98:7: CALLABLE ^ callable_element
			{
			root_0 = (CommonTree)adaptor.nil();


			CALLABLE58=(Token)match(input,CALLABLE,FOLLOW_CALLABLE_in_callable_expr617); 
			CALLABLE58_tree = (CommonTree)adaptor.create(CALLABLE58);
			root_0 = (CommonTree)adaptor.becomeRoot(CALLABLE58_tree, root_0);

			pushFollow(FOLLOW_callable_element_in_callable_expr620);
			callable_element59=callable_element();
			state._fsp--;

			adaptor.addChild(root_0, callable_element59.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "callable_expr"


	public static class dict_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "dict_expr"
	// PyType.g:100:1: dict_expr : DICT ^ dict_element ;
	public final dict_expr_return dict_expr() throws RecognitionException {
		dict_expr_return retval = new dict_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token DICT60=null;
		ParserRuleReturnScope dict_element61 =null;

		CommonTree DICT60_tree=null;

		try {
			// PyType.g:101:5: ( DICT ^ dict_element )
			// PyType.g:101:7: DICT ^ dict_element
			{
			root_0 = (CommonTree)adaptor.nil();


			DICT60=(Token)match(input,DICT,FOLLOW_DICT_in_dict_expr632); 
			DICT60_tree = (CommonTree)adaptor.create(DICT60);
			root_0 = (CommonTree)adaptor.becomeRoot(DICT60_tree, root_0);

			pushFollow(FOLLOW_dict_element_in_dict_expr635);
			dict_element61=dict_element();
			state._fsp--;

			adaptor.addChild(root_0, dict_element61.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "dict_expr"


	public static class tuple_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "tuple_expr"
	// PyType.g:103:1: tuple_expr : TUPLE ^ tuple_element ;
	public final tuple_expr_return tuple_expr() throws RecognitionException {
		tuple_expr_return retval = new tuple_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token TUPLE62=null;
		ParserRuleReturnScope tuple_element63 =null;

		CommonTree TUPLE62_tree=null;

		try {
			// PyType.g:104:5: ( TUPLE ^ tuple_element )
			// PyType.g:104:7: TUPLE ^ tuple_element
			{
			root_0 = (CommonTree)adaptor.nil();


			TUPLE62=(Token)match(input,TUPLE,FOLLOW_TUPLE_in_tuple_expr647); 
			TUPLE62_tree = (CommonTree)adaptor.create(TUPLE62);
			root_0 = (CommonTree)adaptor.becomeRoot(TUPLE62_tree, root_0);

			pushFollow(FOLLOW_tuple_element_in_tuple_expr650);
			tuple_element63=tuple_element();
			state._fsp--;

			adaptor.addChild(root_0, tuple_element63.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "tuple_expr"


	public static class optional_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "optional_expr"
	// PyType.g:106:1: optional_expr : OPTIONAL ^ optional_element ;
	public final optional_expr_return optional_expr() throws RecognitionException {
		optional_expr_return retval = new optional_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPTIONAL64=null;
		ParserRuleReturnScope optional_element65 =null;

		CommonTree OPTIONAL64_tree=null;

		try {
			// PyType.g:107:5: ( OPTIONAL ^ optional_element )
			// PyType.g:107:7: OPTIONAL ^ optional_element
			{
			root_0 = (CommonTree)adaptor.nil();


			OPTIONAL64=(Token)match(input,OPTIONAL,FOLLOW_OPTIONAL_in_optional_expr664); 
			OPTIONAL64_tree = (CommonTree)adaptor.create(OPTIONAL64);
			root_0 = (CommonTree)adaptor.becomeRoot(OPTIONAL64_tree, root_0);

			pushFollow(FOLLOW_optional_element_in_optional_expr667);
			optional_element65=optional_element();
			state._fsp--;

			adaptor.addChild(root_0, optional_element65.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "optional_expr"


	public static class type_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "type_expr"
	// PyType.g:109:1: type_expr : TYPE ^ type_element ;
	public final type_expr_return type_expr() throws RecognitionException {
		type_expr_return retval = new type_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token TYPE66=null;
		ParserRuleReturnScope type_element67 =null;

		CommonTree TYPE66_tree=null;

		try {
			// PyType.g:110:5: ( TYPE ^ type_element )
			// PyType.g:110:7: TYPE ^ type_element
			{
			root_0 = (CommonTree)adaptor.nil();


			TYPE66=(Token)match(input,TYPE,FOLLOW_TYPE_in_type_expr679); 
			TYPE66_tree = (CommonTree)adaptor.create(TYPE66);
			root_0 = (CommonTree)adaptor.becomeRoot(TYPE66_tree, root_0);

			pushFollow(FOLLOW_type_element_in_type_expr682);
			type_element67=type_element();
			state._fsp--;

			adaptor.addChild(root_0, type_element67.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type_expr"


	public static class generic_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "generic_expr"
	// PyType.g:112:1: generic_expr : SimpleType ^ generic_element ;
	public final generic_expr_return generic_expr() throws RecognitionException {
		generic_expr_return retval = new generic_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token SimpleType68=null;
		ParserRuleReturnScope generic_element69 =null;

		CommonTree SimpleType68_tree=null;

		try {
			// PyType.g:113:5: ( SimpleType ^ generic_element )
			// PyType.g:113:7: SimpleType ^ generic_element
			{
			root_0 = (CommonTree)adaptor.nil();


			SimpleType68=(Token)match(input,SimpleType,FOLLOW_SimpleType_in_generic_expr694); 
			SimpleType68_tree = (CommonTree)adaptor.create(SimpleType68);
			root_0 = (CommonTree)adaptor.becomeRoot(SimpleType68_tree, root_0);

			pushFollow(FOLLOW_generic_element_in_generic_expr697);
			generic_element69=generic_element();
			state._fsp--;

			adaptor.addChild(root_0, generic_element69.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "generic_expr"

	// Delegated rules



	public static final BitSet FOLLOW_expr_in_parse228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_list_expr_in_expr241 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_EMPTY_TUPLE_in_expr249 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_THREE_DOTS_in_expr257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_expr_in_expr265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SimpleType_in_expr273 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_union_expr_in_expr281 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_callable_expr_in_expr289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dict_expr_in_expr297 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_tuple_expr_in_expr305 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_optional_expr_in_expr313 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_expr_in_expr321 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_generic_expr_in_expr329 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_list_element341 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_list_element343 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_list_element345 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_union_element366 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_union_element368 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_union_element371 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_union_element373 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_union_element378 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_callable_element395 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_callable_element397 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_callable_element400 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_callable_element402 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_callable_element407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_dict_element424 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_dict_element426 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_dict_element429 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_dict_element431 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_dict_element436 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_tuple_element453 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_tuple_element455 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_tuple_element458 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_tuple_element460 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_tuple_element465 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_optional_element482 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_optional_element484 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_optional_element487 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_optional_element489 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_optional_element494 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_type_element511 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_type_element513 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_type_element516 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_type_element518 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_type_element523 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPEN_SQ_BR_in_generic_element540 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_generic_element542 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_COMMA_in_generic_element545 = new BitSet(new long[]{0x0000000000BF2490L});
	public static final BitSet FOLLOW_expr_in_generic_element547 = new BitSet(new long[]{0x0000000000000060L});
	public static final BitSet FOLLOW_CLOSE_SQ_BR_in_generic_element552 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LIST_in_list_expr569 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_list_element_in_list_expr572 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_SET_in_set_expr585 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_list_element_in_set_expr588 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_UNION_in_union_expr601 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_union_element_in_union_expr604 = new BitSet(new long[]{0x0000000000008002L});
	public static final BitSet FOLLOW_CALLABLE_in_callable_expr617 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_callable_element_in_callable_expr620 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DICT_in_dict_expr632 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_dict_element_in_dict_expr635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TUPLE_in_tuple_expr647 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_tuple_element_in_tuple_expr650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPTIONAL_in_optional_expr664 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_optional_element_in_optional_expr667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TYPE_in_type_expr679 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_type_element_in_type_expr682 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_SimpleType_in_generic_expr694 = new BitSet(new long[]{0x0000000000008000L});
	public static final BitSet FOLLOW_generic_element_in_generic_expr697 = new BitSet(new long[]{0x0000000000000002L});
}
