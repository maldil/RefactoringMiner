// $ANTLR 3.5.2 PyType.g 2021-03-04 12:38:42
package  refactoringminer.python.typeinference.antlr;
import org.antlr.runtime.*;

@SuppressWarnings("all")
public class PyTypeLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public PyTypeLexer() {} 
	public PyTypeLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public PyTypeLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "PyType.g"; }

	// $ANTLR start "CALLABLE"
	public final void mCALLABLE() throws RecognitionException {
		try {
			int _type = CALLABLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:2:10: ( 'Callable' )
			// PyType.g:2:12: 'Callable'
			{
			match("Callable"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CALLABLE"

	// $ANTLR start "CLOSE_SQ_BR"
	public final void mCLOSE_SQ_BR() throws RecognitionException {
		try {
			int _type = CLOSE_SQ_BR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:3:13: ( ']' )
			// PyType.g:3:15: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLOSE_SQ_BR"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:4:7: ( ',' )
			// PyType.g:4:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "DICT"
	public final void mDICT() throws RecognitionException {
		try {
			int _type = DICT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:5:6: ( 'Dict' )
			// PyType.g:5:8: 'Dict'
			{
			match("Dict"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DICT"

	// $ANTLR start "DOT"
	public final void mDOT() throws RecognitionException {
		try {
			int _type = DOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:6:5: ( '\\.' )
			// PyType.g:6:7: '\\.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOT"

	// $ANTLR start "EMPTY_TUPLE"
	public final void mEMPTY_TUPLE() throws RecognitionException {
		try {
			int _type = EMPTY_TUPLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:7:13: ( '()' )
			// PyType.g:7:15: '()'
			{
			match("()"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EMPTY_TUPLE"

	// $ANTLR start "HYPHEN"
	public final void mHYPHEN() throws RecognitionException {
		try {
			int _type = HYPHEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:8:8: ( '-' )
			// PyType.g:8:10: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HYPHEN"

	// $ANTLR start "LIST"
	public final void mLIST() throws RecognitionException {
		try {
			int _type = LIST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:9:6: ( 'List' )
			// PyType.g:9:8: 'List'
			{
			match("List"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LIST"

	// $ANTLR start "OPEN_SQ_BR"
	public final void mOPEN_SQ_BR() throws RecognitionException {
		try {
			int _type = OPEN_SQ_BR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:10:12: ( '[' )
			// PyType.g:10:14: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPEN_SQ_BR"

	// $ANTLR start "OPTIONAL"
	public final void mOPTIONAL() throws RecognitionException {
		try {
			int _type = OPTIONAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:11:10: ( 'Optional' )
			// PyType.g:11:12: 'Optional'
			{
			match("Optional"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPTIONAL"

	// $ANTLR start "SET"
	public final void mSET() throws RecognitionException {
		try {
			int _type = SET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:12:5: ( 'Set' )
			// PyType.g:12:7: 'Set'
			{
			match("Set"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SET"

	// $ANTLR start "THREE_DOTS"
	public final void mTHREE_DOTS() throws RecognitionException {
		try {
			int _type = THREE_DOTS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:13:12: ( '...' )
			// PyType.g:13:14: '...'
			{
			match("..."); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "THREE_DOTS"

	// $ANTLR start "TUPLE"
	public final void mTUPLE() throws RecognitionException {
		try {
			int _type = TUPLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:14:7: ( 'Tuple' )
			// PyType.g:14:9: 'Tuple'
			{
			match("Tuple"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TUPLE"

	// $ANTLR start "TYPE"
	public final void mTYPE() throws RecognitionException {
		try {
			int _type = TYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:15:6: ( 'Type' )
			// PyType.g:15:8: 'Type'
			{
			match("Type"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TYPE"

	// $ANTLR start "UNDERSCORE"
	public final void mUNDERSCORE() throws RecognitionException {
		try {
			int _type = UNDERSCORE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:16:12: ( '_' )
			// PyType.g:16:14: '_'
			{
			match('_'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNDERSCORE"

	// $ANTLR start "UNION"
	public final void mUNION() throws RecognitionException {
		try {
			int _type = UNION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:17:7: ( 'Union' )
			// PyType.g:17:9: 'Union'
			{
			match("Union"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNION"

	// $ANTLR start "DIGIT"
	public final void mDIGIT() throws RecognitionException {
		try {
			int _type = DIGIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:115:8: ( '0' .. '9' )
			// PyType.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGIT"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			int _type = LETTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:117:8: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )
			// PyType.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETTER"

	// $ANTLR start "NUMBER"
	public final void mNUMBER() throws RecognitionException {
		try {
			int _type = NUMBER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:119:8: ( ( DIGIT )+ )
			// PyType.g:119:10: ( DIGIT )+
			{
			// PyType.g:119:10: ( DIGIT )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// PyType.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NUMBER"

	// $ANTLR start "SimpleType"
	public final void mSimpleType() throws RecognitionException {
		try {
			int _type = SimpleType;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:121:12: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.' )* ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' ) )
			// PyType.g:121:14: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.' )* ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// PyType.g:121:37: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '.' )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					int LA2_1 = input.LA(2);
					if ( (LA2_1=='.'||(LA2_1 >= '0' && LA2_1 <= '9')||(LA2_1 >= 'A' && LA2_1 <= 'Z')||LA2_1=='_'||(LA2_1 >= 'a' && LA2_1 <= 'z')) ) {
						alt2=1;
					}

				}
				else if ( (LA2_0=='.') ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// PyType.g:
					{
					if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop2;
				}
			}

			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SimpleType"

	// $ANTLR start "WHITESPACE"
	public final void mWHITESPACE() throws RecognitionException {
		try {
			int _type = WHITESPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// PyType.g:131:12: ( ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+ )
			// PyType.g:131:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			{
			// PyType.g:131:14: ( '\\t' | ' ' | '\\r' | '\\n' | '\\u000C' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||(LA3_0 >= '\f' && LA3_0 <= '\r')||LA3_0==' ') ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// PyType.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			 _channel = HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHITESPACE"

	@Override
	public void mTokens() throws RecognitionException {
		// PyType.g:1:8: ( CALLABLE | CLOSE_SQ_BR | COMMA | DICT | DOT | EMPTY_TUPLE | HYPHEN | LIST | OPEN_SQ_BR | OPTIONAL | SET | THREE_DOTS | TUPLE | TYPE | UNDERSCORE | UNION | DIGIT | LETTER | NUMBER | SimpleType | WHITESPACE )
		int alt4=21;
		alt4 = dfa4.predict(input);
		switch (alt4) {
			case 1 :
				// PyType.g:1:10: CALLABLE
				{
				mCALLABLE(); 

				}
				break;
			case 2 :
				// PyType.g:1:19: CLOSE_SQ_BR
				{
				mCLOSE_SQ_BR(); 

				}
				break;
			case 3 :
				// PyType.g:1:31: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 4 :
				// PyType.g:1:37: DICT
				{
				mDICT(); 

				}
				break;
			case 5 :
				// PyType.g:1:42: DOT
				{
				mDOT(); 

				}
				break;
			case 6 :
				// PyType.g:1:46: EMPTY_TUPLE
				{
				mEMPTY_TUPLE(); 

				}
				break;
			case 7 :
				// PyType.g:1:58: HYPHEN
				{
				mHYPHEN(); 

				}
				break;
			case 8 :
				// PyType.g:1:65: LIST
				{
				mLIST(); 

				}
				break;
			case 9 :
				// PyType.g:1:70: OPEN_SQ_BR
				{
				mOPEN_SQ_BR(); 

				}
				break;
			case 10 :
				// PyType.g:1:81: OPTIONAL
				{
				mOPTIONAL(); 

				}
				break;
			case 11 :
				// PyType.g:1:90: SET
				{
				mSET(); 

				}
				break;
			case 12 :
				// PyType.g:1:94: THREE_DOTS
				{
				mTHREE_DOTS(); 

				}
				break;
			case 13 :
				// PyType.g:1:105: TUPLE
				{
				mTUPLE(); 

				}
				break;
			case 14 :
				// PyType.g:1:111: TYPE
				{
				mTYPE(); 

				}
				break;
			case 15 :
				// PyType.g:1:116: UNDERSCORE
				{
				mUNDERSCORE(); 

				}
				break;
			case 16 :
				// PyType.g:1:127: UNION
				{
				mUNION(); 

				}
				break;
			case 17 :
				// PyType.g:1:133: DIGIT
				{
				mDIGIT(); 

				}
				break;
			case 18 :
				// PyType.g:1:139: LETTER
				{
				mLETTER(); 

				}
				break;
			case 19 :
				// PyType.g:1:146: NUMBER
				{
				mNUMBER(); 

				}
				break;
			case 20 :
				// PyType.g:1:153: SimpleType
				{
				mSimpleType(); 

				}
				break;
			case 21 :
				// PyType.g:1:164: WHITESPACE
				{
				mWHITESPACE(); 

				}
				break;

		}
	}


	protected DFA4 dfa4 = new DFA4(this);
	static final String DFA4_eotS =
		"\1\uffff\1\23\2\uffff\1\23\1\27\2\uffff\1\23\1\uffff\3\23\1\35\1\23\1"+
		"\37\1\23\1\uffff\1\24\2\uffff\1\24\2\uffff\5\24\1\uffff\1\24\2\uffff\4"+
		"\24\1\55\4\24\1\62\1\63\1\24\1\uffff\1\24\1\66\2\24\2\uffff\1\24\1\72"+
		"\1\uffff\1\73\2\24\2\uffff\2\24\1\100\1\101\2\uffff";
	static final String DFA4_eofS =
		"\102\uffff";
	static final String DFA4_minS =
		"\1\11\1\56\2\uffff\2\56\2\uffff\1\56\1\uffff\5\56\1\60\1\56\1\uffff\1"+
		"\154\2\uffff\1\143\2\uffff\1\163\2\164\2\160\1\uffff\1\151\2\uffff\1\154"+
		"\2\164\1\151\1\56\1\154\1\145\1\157\1\141\2\56\1\157\1\uffff\1\145\1\56"+
		"\1\156\1\142\2\uffff\1\156\1\56\1\uffff\1\56\1\154\1\141\2\uffff\1\145"+
		"\1\154\2\56\2\uffff";
	static final String DFA4_maxS =
		"\2\172\2\uffff\1\172\1\56\2\uffff\1\172\1\uffff\5\172\1\71\1\172\1\uffff"+
		"\1\154\2\uffff\1\143\2\uffff\1\163\2\164\2\160\1\uffff\1\151\2\uffff\1"+
		"\154\2\164\1\151\1\172\1\154\1\145\1\157\1\141\2\172\1\157\1\uffff\1\145"+
		"\1\172\1\156\1\142\2\uffff\1\156\1\172\1\uffff\1\172\1\154\1\141\2\uffff"+
		"\1\145\1\154\2\172\2\uffff";
	static final String DFA4_acceptS =
		"\2\uffff\1\2\1\3\2\uffff\1\6\1\7\1\uffff\1\11\7\uffff\1\25\1\uffff\1\22"+
		"\1\24\1\uffff\1\14\1\5\5\uffff\1\17\1\uffff\1\21\1\23\14\uffff\1\13\4"+
		"\uffff\1\4\1\10\2\uffff\1\16\3\uffff\1\15\1\20\4\uffff\1\1\1\12";
	static final String DFA4_specialS =
		"\102\uffff}>";
	static final String[] DFA4_transitionS = {
			"\2\21\1\uffff\2\21\22\uffff\1\21\7\uffff\1\6\3\uffff\1\3\1\7\1\5\1\uffff"+
			"\12\17\7\uffff\2\20\1\1\1\4\7\20\1\10\2\20\1\12\3\20\1\13\1\14\1\16\5"+
			"\20\1\11\1\uffff\1\2\1\uffff\1\15\1\uffff\32\20",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\1\22\31\24",
			"",
			"",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\10\24\1\25\21"+
			"\24",
			"\1\26",
			"",
			"",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\10\24\1\30\21"+
			"\24",
			"",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\17\24\1\31\12"+
			"\24",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\4\24\1\32\25"+
			"\24",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\24\24\1\33\3"+
			"\24\1\34\1\24",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\15\24\1\36\14"+
			"\24",
			"\12\40",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"\1\41",
			"",
			"",
			"\1\42",
			"",
			"",
			"\1\43",
			"\1\44",
			"\1\45",
			"\1\46",
			"\1\47",
			"",
			"\1\50",
			"",
			"",
			"\1\51",
			"\1\52",
			"\1\53",
			"\1\54",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\56",
			"\1\57",
			"\1\60",
			"\1\61",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\64",
			"",
			"\1\65",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\67",
			"\1\70",
			"",
			"",
			"\1\71",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\74",
			"\1\75",
			"",
			"",
			"\1\76",
			"\1\77",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			""
	};

	static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
	static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
	static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
	static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
	static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
	static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
	static final short[][] DFA4_transition;

	static {
		int numStates = DFA4_transitionS.length;
		DFA4_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
		}
	}

	protected class DFA4 extends DFA {

		public DFA4(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 4;
			this.eot = DFA4_eot;
			this.eof = DFA4_eof;
			this.min = DFA4_min;
			this.max = DFA4_max;
			this.accept = DFA4_accept;
			this.special = DFA4_special;
			this.transition = DFA4_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( CALLABLE | CLOSE_SQ_BR | COMMA | DICT | DOT | EMPTY_TUPLE | HYPHEN | LIST | OPEN_SQ_BR | OPTIONAL | SET | THREE_DOTS | TUPLE | TYPE | UNDERSCORE | UNION | DIGIT | LETTER | NUMBER | SimpleType | WHITESPACE );";
		}
	}

}
