/* The following code was generated by JFlex 1.6.1 */

/*  Expand this file into your solution for project 2 */
import java_cup.runtime.*;

class CSXToken {
	int linenum;
	int colnum;
	CSXToken(int line,int col) {
		linenum=line;colnum=col;
	}
}

class CSXErrorToken extends CSXToken {
	String stringError;
	CSXErrorToken(String error, int line, int col) {
		super(line,col);
		stringError = error;
	}
}

//integer literal class
//This function catches overflow both in the positive and negative side.
//Also, it converts ~ to a negative sign and just removes the + since it isredundant
class CSXIntLitToken extends CSXToken {
	int intValue;
	String stringValue;
	boolean queryNegative = false;
	CSXIntLitToken(String stringval,int line,int col) {
		super(line,col);
		//System.out.println(stringval);
		if (stringval.charAt(0) == '+'){	//if there is a + we just remove it
			stringValue = stringval.substring(1);
		}
		if (stringval.charAt(0) == '~'){ //this is a negative sign in this language
			stringValue = stringval.substring(1);
			queryNegative = true;
		}
		else {
			stringValue = stringval;
		}
		try {
			intValue = Integer.parseInt(stringValue);
			if(queryNegative){
				intValue = -intValue;
			}
		} 
		catch (Exception e) {

			if (queryNegative){
				System.out.println("Overflow integer to small!");
				intValue = Integer.MIN_VALUE;
			}
			else {
				System.out.println("Overflow integer to large!");
				intValue = Integer.MAX_VALUE;
			}
		}
	};
}

//Identifier class the identifiers must start with a letter then after that can be a letter or a number.
//This class stores the stiring form of the identifier in the variable identifierText
class CSXIdentifierToken extends CSXToken {
	String identifierText;
	CSXIdentifierToken(String text,int line,int col) {
		super(line,col);identifierText=text;
	}
}

//Creates a string from the yytext().  Goes through a string in order and turns \n into 
class CSXStringLitToken extends CSXToken {
	String stringText; // \n is a real newline this is the real value of the string
	String stringLitteralText; // Full text of string literal,
                          //  including quotes & escapes thi
	CSXStringLitToken(String text,int line,int col) {
		super(line,col);
		stringLitteralText = text;
		StringBuilder stringBuilder = new StringBuilder(text.length());
		for(int i = 0; i < text.length(); ++i){
			if(text.charAt(i) != '\\'){
				stringBuilder.append(text.charAt(i));
			}
			else {
				char charValue = '\0';
				i++;  						//skip over the escape character.
				switch(text.charAt(i)){
					case 'n':
						stringBuilder.append('\n');
						break;
					case 't':
						charValue = '\t';
						break;
					case '\'':
						charValue = '\'';
						break;
					case '\\':
						charValue = '\\';
						break;
					case '\"':
						charValue = '\"';
						break;
					default:
						System.out.println("unrecognized token type: " + text.charAt(i));
						charValue = ' '; 
				}
				stringBuilder.append(charValue);
			}
		}
		stringText = stringBuilder.toString();
	}
}

class CSXCharLitToken extends CSXToken {
	char charValue; // this has the \n is an actual newline
	String charLitteralText; //this has the \n actually printed
	CSXCharLitToken(String val,int line,int col) {
		super(line,col);
		charLitteralText = val;
		if(val.charAt(1)=='\\'){
			switch(val.charAt(2)){
				case 'n':
					charValue = '\n';
					break;
				case 't':
					charValue = '\t';
					break;
				case '\'':
					charValue = '\'';
					break;
				case '\\':
					charValue = '\\';
					break;
				default:
					System.out.println("Error token char: "+val);
					charValue = '\0'; // if there is a error token we get null

			}
		}
		else{
			charLitteralText = val.substring(0,2)+"\'";
			charValue=val.charAt(1);
		}
	}
}

// This class is used to track line and column numbers
// Feel free to change to extend it
/*
class Pos {
	static int  linenum = 1; // maintain this as line number current
                                 // token was scanned on 
	static int  colnum = 1; // maintain this as column number current
                                 // token began at 
	static int  line = 1; // maintain this as line number after
							// scanning current token  
	static int  col = 1; // maintain this as column number after
							// scanning current token  
	static void setpos() { // set starting position for current token
		linenum = line;
		colnum = col;
	}
}
*/
/*
class Symbol { 
	public int sym;
	public CSXToken value;
	public Symbol(int tokenType, CSXToken theToken) {
		sym = tokenType;
		value = theToken;
	}
}
*/

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>csx_lite.jflex</tt>
 */
class Yylex {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\65\1\12\1\14\1\14\1\13\22\0\1\65\1\50\1\3"+
    "\1\15\1\6\1\0\1\53\1\4\1\52\1\55\1\56\1\66\1\57"+
    "\1\70\1\0\1\11\12\1\1\64\1\51\1\47\1\46\1\45\2\0"+
    "\1\17\1\44\1\22\1\43\1\26\1\27\1\25\1\40\1\42\1\2"+
    "\1\24\1\33\1\2\1\31\1\32\1\21\1\2\1\20\1\35\1\36"+
    "\1\30\1\16\1\37\3\2\1\60\1\5\1\61\3\0\1\17\1\44"+
    "\1\22\1\43\1\26\1\27\1\25\1\40\1\42\1\2\1\24\1\33"+
    "\1\2\1\7\1\32\1\21\1\2\1\20\1\35\1\10\1\30\1\16"+
    "\1\37\3\2\1\62\1\54\1\63\1\67\6\0\1\14\252\0\2\41"+
    "\115\0\1\34\u1ea8\0\1\14\1\14\u0100\0\1\23\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\udee5\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\2\1\1\3\1\4\1\5"+
    "\1\1\7\3\1\1\2\3\1\6\1\7\1\10\1\11"+
    "\1\12\1\13\2\1\1\14\1\15\1\16\1\17\1\20"+
    "\1\21\1\22\1\23\1\24\2\25\1\0\1\26\2\0"+
    "\1\27\1\0\1\3\1\30\1\0\15\3\1\0\1\31"+
    "\1\3\1\31\2\3\1\32\1\33\1\34\1\35\1\36"+
    "\1\37\1\0\1\3\1\0\1\40\1\0\4\3\1\0"+
    "\4\3\1\0\3\3\1\41\1\0\1\3\2\42\2\3"+
    "\1\43\1\44\2\45\1\3\1\46\1\0\1\3\1\0"+
    "\2\3\1\0\1\3\1\0\1\3\1\47\2\50\1\0"+
    "\1\3\1\51\1\0\2\3\1\52\1\3\1\0\1\3"+
    "\2\53\1\0\1\3\2\54\2\55\2\56\2\57\2\60"+
    "\1\61\1\0\1\3\1\0\1\3\2\62\1\0\1\3"+
    "\2\63";

  private static int [] zzUnpackAction() {
    int [] result = new int[151];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\71\0\162\0\253\0\344\0\u011d\0\u0156\0\u018f"+
    "\0\71\0\u01c8\0\u0201\0\u023a\0\u0273\0\u02ac\0\u02e5\0\u031e"+
    "\0\u0357\0\u0390\0\u03c9\0\u0402\0\u043b\0\u0474\0\u04ad\0\u04e6"+
    "\0\71\0\71\0\u051f\0\u0558\0\71\0\71\0\71\0\71"+
    "\0\71\0\71\0\71\0\71\0\162\0\162\0\71\0\344"+
    "\0\71\0\u0591\0\u05ca\0\71\0\u0603\0\u063c\0\u0675\0\u06ae"+
    "\0\u06e7\0\u0720\0\u0759\0\u0792\0\u07cb\0\u0804\0\u083d\0\u0876"+
    "\0\u08af\0\u08e8\0\u0921\0\u095a\0\u0993\0\u09cc\0\71\0\u0a05"+
    "\0\253\0\u0a3e\0\u0a77\0\71\0\71\0\71\0\71\0\71"+
    "\0\71\0\u0ab0\0\u0ae9\0\u0b22\0\253\0\u0b5b\0\u0b94\0\u0bcd"+
    "\0\u0c06\0\u0c3f\0\u0c78\0\u0cb1\0\u0cea\0\u0d23\0\u0d5c\0\u0d95"+
    "\0\u0dce\0\u0e07\0\u0e40\0\253\0\u0e79\0\u0eb2\0\71\0\253"+
    "\0\u0eeb\0\u0f24\0\253\0\71\0\71\0\253\0\u0f5d\0\253"+
    "\0\u0f96\0\u0fcf\0\u1008\0\u1041\0\u107a\0\u10b3\0\u10ec\0\u1125"+
    "\0\u115e\0\253\0\71\0\253\0\u1197\0\u11d0\0\253\0\u1209"+
    "\0\u1242\0\u127b\0\253\0\u12b4\0\u12ed\0\u1326\0\71\0\253"+
    "\0\u135f\0\u1398\0\71\0\253\0\71\0\253\0\71\0\253"+
    "\0\71\0\253\0\71\0\253\0\253\0\u13d1\0\u140a\0\u1443"+
    "\0\u147c\0\71\0\253\0\u14b5\0\u14ee\0\71\0\253";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[151];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\5\1\6\2\2\1\4\1\7"+
    "\1\10\1\11\2\2\1\12\1\13\1\4\1\14\1\15"+
    "\1\16\1\2\2\4\1\17\1\20\4\4\1\2\1\4"+
    "\1\7\1\21\1\4\1\22\1\23\1\4\1\24\1\25"+
    "\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35"+
    "\1\36\1\37\1\40\1\41\1\42\1\43\1\44\1\11"+
    "\1\45\1\46\1\47\72\0\1\3\70\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\10\4\1\0\4\4\1\0"+
    "\3\4\24\0\3\50\1\51\1\50\1\52\1\0\3\50"+
    "\2\0\55\50\4\53\1\54\1\55\63\53\1\0\2\4"+
    "\4\0\2\4\5\0\2\4\1\56\2\4\1\0\10\4"+
    "\1\0\4\4\1\0\3\4\35\0\1\57\74\0\1\60"+
    "\54\0\2\4\4\0\2\4\5\0\1\4\1\61\3\4"+
    "\1\0\6\4\1\62\1\4\1\0\4\4\1\0\3\4"+
    "\25\0\2\4\4\0\2\4\5\0\5\4\1\0\2\4"+
    "\1\63\5\4\1\0\4\4\1\0\3\4\25\0\2\4"+
    "\4\0\2\4\5\0\1\4\1\64\1\65\2\4\1\0"+
    "\10\4\1\0\4\4\1\0\3\4\25\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\6\4\1\66\1\67\1\0"+
    "\3\4\1\70\1\0\3\4\25\0\2\4\4\0\2\4"+
    "\5\0\5\4\1\0\7\4\1\71\1\0\4\4\1\0"+
    "\3\4\25\0\2\4\4\0\2\4\5\0\1\4\1\72"+
    "\3\4\1\0\4\4\1\73\1\4\1\74\1\4\1\0"+
    "\4\4\1\0\3\4\25\0\2\4\4\0\2\4\5\0"+
    "\5\4\1\0\10\4\1\0\3\4\1\75\1\0\3\4"+
    "\33\0\1\76\17\0\1\77\1\0\1\76\40\0\2\4"+
    "\4\0\1\100\1\4\5\0\5\4\1\0\3\4\1\101"+
    "\1\4\1\100\2\4\1\0\4\4\1\0\3\4\25\0"+
    "\2\4\4\0\2\4\5\0\2\4\1\102\2\4\1\0"+
    "\6\4\1\103\1\4\1\0\4\4\1\0\3\4\72\0"+
    "\1\104\70\0\1\105\70\0\1\106\70\0\1\107\75\0"+
    "\1\110\71\0\1\111\17\0\1\50\1\0\1\50\1\0"+
    "\2\50\60\0\4\53\1\54\1\0\63\53\4\0\2\112"+
    "\1\0\2\112\61\0\2\4\4\0\2\4\5\0\5\4"+
    "\1\0\4\4\1\113\3\4\1\0\4\4\1\0\3\4"+
    "\24\0\12\57\3\0\54\57\15\60\1\114\53\60\1\0"+
    "\2\4\4\0\2\4\5\0\2\4\1\115\2\4\1\0"+
    "\10\4\1\0\4\4\1\0\3\4\25\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\10\4\1\0\4\4\1\116"+
    "\1\117\2\4\25\0\2\4\4\0\1\4\1\120\5\0"+
    "\1\4\1\121\3\4\1\0\10\4\1\0\1\4\1\120"+
    "\2\4\1\0\3\4\25\0\2\4\4\0\2\4\5\0"+
    "\4\4\1\122\1\0\10\4\1\0\4\4\1\0\3\4"+
    "\25\0\2\4\4\0\2\4\5\0\5\4\1\0\10\4"+
    "\1\0\4\4\1\123\1\124\2\4\25\0\2\4\4\0"+
    "\1\125\1\4\5\0\5\4\1\0\5\4\1\125\2\4"+
    "\1\0\4\4\1\0\3\4\25\0\2\4\4\0\2\4"+
    "\5\0\1\4\1\126\3\4\1\0\10\4\1\0\4\4"+
    "\1\0\3\4\25\0\2\4\4\0\2\4\5\0\1\4"+
    "\1\127\3\4\1\0\10\4\1\0\4\4\1\0\3\4"+
    "\25\0\2\4\4\0\2\4\5\0\5\4\1\0\10\4"+
    "\1\130\1\131\3\4\1\0\3\4\25\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\7\4\1\132\1\0\4\4"+
    "\1\0\3\4\25\0\2\4\4\0\1\133\1\4\5\0"+
    "\5\4\1\0\5\4\1\133\2\4\1\0\4\4\1\0"+
    "\3\4\25\0\2\4\4\0\2\4\5\0\2\4\1\134"+
    "\2\4\1\0\10\4\1\0\4\4\1\0\3\4\25\0"+
    "\2\4\4\0\2\4\5\0\5\4\1\0\10\4\1\0"+
    "\4\4\1\135\1\136\2\4\34\0\1\137\25\0\1\137"+
    "\33\0\2\4\4\0\1\4\1\140\5\0\5\4\1\0"+
    "\10\4\1\0\1\4\1\140\2\4\1\0\3\4\25\0"+
    "\2\4\4\0\2\4\5\0\5\4\1\0\2\4\1\141"+
    "\5\4\1\0\4\4\1\0\3\4\25\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\6\4\1\142\1\4\1\0"+
    "\4\4\1\0\3\4\30\0\1\54\65\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\2\4\1\143\5\4\1\0"+
    "\4\4\1\0\3\4\24\0\15\60\1\144\53\60\43\0"+
    "\1\145\26\0\2\4\4\0\2\4\5\0\5\4\1\0"+
    "\10\4\1\0\4\4\1\0\1\4\1\146\1\4\25\0"+
    "\2\4\4\0\2\4\5\0\5\4\1\0\4\4\1\147"+
    "\3\4\1\0\4\4\1\0\3\4\25\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\10\4\1\0\4\4\1\0"+
    "\1\4\1\150\1\4\25\0\2\4\4\0\2\4\5\0"+
    "\5\4\1\151\1\152\7\4\1\0\4\4\1\0\3\4"+
    "\33\0\1\153\21\0\1\153\40\0\2\4\4\0\1\154"+
    "\1\4\5\0\5\4\1\0\5\4\1\154\2\4\1\0"+
    "\4\4\1\0\3\4\25\0\2\4\4\0\1\4\1\155"+
    "\5\0\5\4\1\0\10\4\1\156\1\157\1\155\2\4"+
    "\1\0\3\4\25\0\2\4\4\0\2\4\5\0\5\4"+
    "\1\0\10\4\1\160\1\161\3\4\1\0\3\4\25\0"+
    "\2\4\4\0\2\4\5\0\2\4\1\162\2\4\1\0"+
    "\10\4\1\0\4\4\1\0\3\4\52\0\1\163\43\0"+
    "\2\4\4\0\2\4\5\0\5\4\1\0\2\4\1\164"+
    "\5\4\1\0\4\4\1\0\3\4\25\0\2\4\4\0"+
    "\2\4\5\0\5\4\1\0\10\4\1\165\1\166\3\4"+
    "\1\0\3\4\25\0\2\4\4\0\2\4\5\0\4\4"+
    "\1\167\1\0\10\4\1\0\4\4\1\0\3\4\57\0"+
    "\1\170\36\0\2\4\4\0\2\4\5\0\5\4\1\0"+
    "\7\4\1\171\1\0\4\4\1\0\3\4\25\0\2\4"+
    "\4\0\2\4\5\0\1\4\1\172\3\4\1\0\10\4"+
    "\1\0\4\4\1\0\3\4\25\0\2\4\4\0\2\4"+
    "\5\0\5\4\1\0\7\4\1\173\1\0\4\4\1\0"+
    "\3\4\25\0\2\4\4\0\2\4\5\0\2\4\1\174"+
    "\2\4\1\0\10\4\1\0\4\4\1\0\3\4\43\0"+
    "\1\175\52\0\2\4\4\0\2\4\5\0\1\4\1\176"+
    "\3\4\1\0\10\4\1\0\4\4\1\0\3\4\34\0"+
    "\1\177\25\0\1\177\33\0\2\4\4\0\1\4\1\200"+
    "\5\0\5\4\1\0\10\4\1\0\1\4\1\200\2\4"+
    "\1\0\3\4\25\0\2\4\4\0\2\4\5\0\5\4"+
    "\1\0\10\4\1\0\4\4\1\201\1\202\2\4\34\0"+
    "\1\203\25\0\1\203\33\0\2\4\4\0\1\4\1\204"+
    "\5\0\5\4\1\0\10\4\1\0\1\4\1\204\2\4"+
    "\1\0\3\4\60\0\2\205\34\0\2\4\4\0\2\4"+
    "\5\0\5\4\1\0\10\4\1\205\1\206\3\4\1\0"+
    "\3\4\52\0\1\207\43\0\2\4\4\0\2\4\5\0"+
    "\5\4\1\0\2\4\1\210\5\4\1\0\4\4\1\0"+
    "\3\4\52\0\1\211\43\0\2\4\4\0\2\4\5\0"+
    "\5\4\1\0\2\4\1\212\5\4\1\0\4\4\1\0"+
    "\3\4\25\0\2\4\4\0\2\4\5\0\5\4\1\213"+
    "\1\214\7\4\1\0\4\4\1\0\3\4\25\0\2\4"+
    "\4\0\1\215\1\4\5\0\5\4\1\0\5\4\1\215"+
    "\2\4\1\0\4\4\1\0\3\4\51\0\1\216\44\0"+
    "\2\4\4\0\2\4\5\0\5\4\1\0\1\4\1\217"+
    "\6\4\1\0\4\4\1\0\3\4\33\0\1\220\21\0"+
    "\1\220\40\0\2\4\4\0\1\221\1\4\5\0\5\4"+
    "\1\0\5\4\1\221\2\4\1\0\4\4\1\0\3\4"+
    "\52\0\1\222\43\0\2\4\4\0\2\4\5\0\5\4"+
    "\1\0\2\4\1\223\5\4\1\0\4\4\1\0\3\4"+
    "\54\0\1\224\41\0\2\4\4\0\2\4\5\0\5\4"+
    "\1\0\4\4\1\225\3\4\1\0\4\4\1\0\3\4"+
    "\52\0\1\226\43\0\2\4\4\0\2\4\5\0\5\4"+
    "\1\0\2\4\1\227\5\4\1\0\4\4\1\0\3\4"+
    "\24\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5415];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\6\1\1\11\17\1\2\11\2\1\10\11"+
    "\2\1\1\11\1\0\1\11\2\0\1\11\1\0\2\1"+
    "\1\0\15\1\1\0\1\11\4\1\6\11\1\0\1\1"+
    "\1\0\1\1\1\0\4\1\1\0\4\1\1\0\4\1"+
    "\1\0\1\1\1\11\4\1\2\11\3\1\1\0\1\1"+
    "\1\0\2\1\1\0\1\1\1\0\2\1\1\11\1\1"+
    "\1\0\2\1\1\0\4\1\1\0\1\1\1\11\1\1"+
    "\1\0\1\1\1\11\1\1\1\11\1\1\1\11\1\1"+
    "\1\11\1\1\1\11\2\1\1\0\1\1\1\0\1\1"+
    "\1\11\1\1\1\0\1\1\1\11\1\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[151];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Yylex(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 228) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Symbol yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
              {
                return new Symbol(sym.EOF,
		new CSXToken(yyline,yycolumn));
              }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return new Symbol(sym.error, 
		new CSXErrorToken(yytext(),yyline,yycolumn));
            }
          case 52: break;
          case 2: 
            { return new Symbol(sym.INTLIT,
		new CSXIntLitToken(yytext(),
			yyline,yycolumn));
            }
          case 53: break;
          case 3: 
            { return new Symbol(sym.IDENTIFIER,
		new CSXIdentifierToken(yytext(),yyline,yycolumn));
            }
          case 54: break;
          case 4: 
            { return new Symbol(sym.SLASH,
		new CSXToken(yyline,yycolumn));
            }
          case 55: break;
          case 5: 
            { /* */
            }
          case 56: break;
          case 6: 
            { return new Symbol(sym.GT,
		new CSXToken(yyline,yycolumn));
            }
          case 57: break;
          case 7: 
            { return new Symbol(sym.ASG,
		new CSXToken(yyline,yycolumn));
            }
          case 58: break;
          case 8: 
            { return new Symbol(sym.LT,
		new CSXToken(yyline,yycolumn));
            }
          case 59: break;
          case 9: 
            { return new Symbol(sym.NOT,
		new CSXToken(yyline,yycolumn));
            }
          case 60: break;
          case 10: 
            { return new Symbol(sym.SEMI,
		new CSXToken(yyline,yycolumn));
            }
          case 61: break;
          case 11: 
            { return new Symbol(sym.LPAREN, 
		new CSXToken(yyline,yycolumn));
            }
          case 62: break;
          case 12: 
            { return new Symbol(sym.RPAREN, 
		new CSXToken(yyline,yycolumn));
            }
          case 63: break;
          case 13: 
            { return new Symbol(sym.TIMES,
		new CSXToken(yyline,yycolumn));
            }
          case 64: break;
          case 14: 
            { return new Symbol(sym.COMMA,
		new CSXToken(yyline,yycolumn));
            }
          case 65: break;
          case 15: 
            { return new Symbol(sym.LBRACKET,
		new CSXToken(yyline,yycolumn));
            }
          case 66: break;
          case 16: 
            { return new Symbol(sym.RBRACKET,
		new CSXToken(yyline,yycolumn));
            }
          case 67: break;
          case 17: 
            { return new Symbol(sym.LBRACE,
		new CSXToken(yyline,yycolumn));
            }
          case 68: break;
          case 18: 
            { return new Symbol(sym.RBRACE,
		new CSXToken(yyline,yycolumn));
            }
          case 69: break;
          case 19: 
            { return new Symbol(sym.COLON,
		new CSXToken(yyline,yycolumn));
            }
          case 70: break;
          case 20: 
            { return new Symbol(sym.PLUS,
		new CSXToken(yyline,yycolumn));
            }
          case 71: break;
          case 21: 
            { return new Symbol(sym.MINUS,
		new CSXToken(yyline,yycolumn));
            }
          case 72: break;
          case 22: 
            { //System.out.println(yytext());
	return new Symbol(sym.STRLIT,
		new CSXStringLitToken(yytext(),yyline,yycolumn));
            }
          case 73: break;
          case 23: 
            { return new Symbol(sym.CHARLIT,
		new CSXCharLitToken(yytext(),yyline,yycolumn));
            }
          case 74: break;
          case 24: 
            { //I found out about the dot from stackoverflow.com
	// this is a one line comment
            }
          case 75: break;
          case 25: 
            { return new Symbol(sym.rw_IF,
		new CSXToken(yyline,yycolumn));
            }
          case 76: break;
          case 26: 
            { return new Symbol(sym.GEQ,
		new CSXToken(yyline,yycolumn));
            }
          case 77: break;
          case 27: 
            { return new Symbol(sym.EQ,
		new CSXToken(yyline,yycolumn));
            }
          case 78: break;
          case 28: 
            { return new Symbol(sym.LEQ,
		new CSXToken(yyline,yycolumn));
            }
          case 79: break;
          case 29: 
            { return new Symbol(sym.NOTEQ,
		new CSXToken(yyline,yycolumn));
            }
          case 80: break;
          case 30: 
            { return new Symbol(sym.CAND,
		new CSXToken(yyline,yycolumn));
            }
          case 81: break;
          case 31: 
            { return new Symbol(sym.COR,
		new CSXToken(yyline,yycolumn));
            }
          case 82: break;
          case 32: 
            { return new Symbol(sym.rw_VAR,
		new CSXToken(yyline,yycolumn));
            }
          case 83: break;
          case 33: 
            { return new Symbol(sym.rw_FOR,
		new CSXToken(yyline,yycolumn));
            }
          case 84: break;
          case 34: 
            { return new Symbol(sym.rw_INT,
		new CSXToken(yyline,yycolumn));
            }
          case 85: break;
          case 35: 
            { return new Symbol(sym.rw_TRUE,
		new CSXToken(yyline,yycolumn));
            }
          case 86: break;
          case 36: 
            { /* comment over arbitrary lines comment */
            }
          case 87: break;
          case 37: 
            { return new Symbol(sym.rw_VOID,
		new CSXToken(yyline,yycolumn));
            }
          case 88: break;
          case 38: 
            { return new Symbol(sym.rw_READ,
		new CSXToken(yyline,yycolumn));
            }
          case 89: break;
          case 39: 
            { return new Symbol(sym.rw_CHAR,
		new CSXToken(yyline,yycolumn));
            }
          case 90: break;
          case 40: 
            { return new Symbol(sym.rw_ELSE,
		new CSXToken(yyline,yycolumn));
            }
          case 91: break;
          case 41: 
            { return new Symbol(sym.rw_FUNC,
		new CSXToken(yyline,yycolumn));
            }
          case 92: break;
          case 42: 
            { return new Symbol(sym.rw_BOOL,
		new CSXToken(yyline,yycolumn));
            }
          case 93: break;
          case 43: 
            { return new Symbol(sym.rw_PRINT,
		new CSXToken(yyline,yycolumn));
            }
          case 94: break;
          case 44: 
            { return new Symbol(sym.rw_CONST,
		new CSXToken(yyline,yycolumn));
            }
          case 95: break;
          case 45: 
            { return new Symbol(sym.rw_CLASS,
		new CSXToken(yyline,yycolumn));
            }
          case 96: break;
          case 46: 
            { return new Symbol(sym.rw_FALSE,
		new CSXToken(yyline,yycolumn));
            }
          case 97: break;
          case 47: 
            { return new Symbol(sym.rw_WHILE,
		new CSXToken(yyline,yycolumn));
            }
          case 98: break;
          case 48: 
            { return new Symbol(sym.rw_BREAK,
		new CSXToken(yyline,yycolumn));
            }
          case 99: break;
          case 49: 
            { return new Symbol(sym.rw_RETURN,
		new CSXToken(yyline,yycolumn));
            }
          case 100: break;
          case 50: 
            { return new Symbol(sym.rw_PACKAGE,
		new CSXToken(yyline,yycolumn));
            }
          case 101: break;
          case 51: 
            { return new Symbol(sym.rw_CONTINUE,
		new CSXToken(yyline,yycolumn));
            }
          case 102: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
