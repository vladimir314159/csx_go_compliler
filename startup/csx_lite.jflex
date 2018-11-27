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
	String stringLiteralText; // Full text of string literal,
                          //  including quotes & escapes thi
	CSXStringLitToken(String text,int line,int col) {
		super(line,col);
		stringLiteralText = text;
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
	String charLiteralText; //this has the \n actually printed
	CSXCharLitToken(String val,int line,int col) {
		super(line,col);
		charLiteralText = val;
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
			charLiteralText = val.substring(0,2)+"\'";
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
%%

DIGIT=[0-9]
ALFABET=[A-Za-z]
QUOTEANYWHERE=[^]* [\"] [^]*
TICANYWHERE=[^]* [\'] [^]*
BACKSLASHANYWHERE=[^]* [\\] [^]*
NEWLINEANYWHERE=[^]* [\n\r$] [^]*
ESBACK = \\\"
ESTIC = \\\'
ESNEW = \\n
ESTAB = \\t
ESES = \\\\
NOTENDLINE = [^\n\r$]
%type Symbol
%column
%line
%caseless
%eofval{
  return new Symbol(sym.EOF, new CSXToken(0,0));
%eofval}

%%



//One line comment
"//" .* { 				//I found out about the dot from stackoverflow.com
	// this is a one line comment 
}

//Multiline comment note that this matchs the next ## in other words
//## ## comment? ## ## comment will NOT be a comment, rather it will be scanned.

"##" (#? ([^#][\n\r$]?)+)* "##" {
	/* comment over arbitrary lines comment */
}


//Next are all of the reserved words or charictures case insensitive so var can also be Var or vaR
//besides that this part is all standered.
"var" {
	return new Symbol(sym.rw_VAR,
		new CSXToken(yyline,yycolumn));
}

"package" {
	return new Symbol(sym.rw_PACKAGE,
		new CSXToken(yyline,yycolumn));
}

"func"  {
	return new Symbol(sym.rw_FUNC,
		new CSXToken(yyline,yycolumn));
}

"for" {
	return new Symbol(sym.rw_FOR,
		new CSXToken(yyline,yycolumn));
}

"class" {
	return new Symbol(sym.rw_CLASS,
		new CSXToken(yyline,yycolumn));
}


"const"	{
	return new Symbol(sym.rw_CONST,
		new CSXToken(yyline,yycolumn));
}

"while" {
	return new Symbol(sym.rw_WHILE,
		new CSXToken(yyline,yycolumn));
}

"char" {
	return new Symbol(sym.rw_CHAR,
		new CSXToken(yyline,yycolumn));
}


"return" {
	return new Symbol(sym.rw_RETURN,
		new CSXToken(yyline,yycolumn));
}

"read" {
	return new Symbol(sym.rw_READ,
		new CSXToken(yyline,yycolumn));
}


"int" {
	return new Symbol(sym.rw_INT,
		new CSXToken(yyline,yycolumn));		
}

"false" {
	return new Symbol(sym.rw_FALSE,
		new CSXToken(yyline,yycolumn));		
}

"bool" {
	return new Symbol(sym.rw_BOOL,
		new CSXToken(yyline,yycolumn));		
}

"true" {
	return new Symbol(sym.rw_TRUE,
		new CSXToken(yyline,yycolumn));		
}

"if" {
	return new Symbol(sym.rw_IF,
		new CSXToken(yyline,yycolumn));		
}

"break" {
	return new Symbol(sym.rw_BREAK,
		new CSXToken(yyline,yycolumn));			
}

"continue" {
	return new Symbol(sym.rw_CONTINUE,
		new CSXToken(yyline,yycolumn));		
}

"void" {
	return new Symbol(sym.rw_VOID,
		new CSXToken(yyline,yycolumn));		
}

"else" {
	return new Symbol(sym.rw_ELSE,
		new CSXToken(yyline,yycolumn));
}

"print" {
	return new Symbol(sym.rw_PRINT,
		new CSXToken(yyline,yycolumn));
}

">=" {
	return new Symbol(sym.GEQ,
		new CSXToken(yyline,yycolumn));
}

"<=" {
	return new Symbol(sym.LEQ,
		new CSXToken(yyline,yycolumn));
}

">" {
	return new Symbol(sym.GT,
		new CSXToken(yyline,yycolumn));
}

"<" {
	return new Symbol(sym.LT,
		new CSXToken(yyline,yycolumn));
}

"!="	{
	return new Symbol(sym.NOTEQ,
		new CSXToken(yyline,yycolumn));
}

"==" {
	return new Symbol(sym.EQ,
		new CSXToken(yyline,yycolumn));
}

";"	{
	return new Symbol(sym.SEMI,
		new CSXToken(yyline,yycolumn));
}

"=" {
	return new Symbol(sym.ASG,
		new CSXToken(yyline,yycolumn));
}


"(" {
	return new Symbol(sym.LPAREN, 
		new CSXToken(yyline,yycolumn));
}

"&&" {
	return new Symbol(sym.CAND,
		new CSXToken(yyline,yycolumn));
}

"||" {
	return new Symbol(sym.COR,
		new CSXToken(yyline,yycolumn));
}

")" {
	return new Symbol(sym.RPAREN, 
		new CSXToken(yyline,yycolumn));
}

"/" {
	return new Symbol(sym.SLASH,
		new CSXToken(yyline,yycolumn));
}

"*" {
	return new Symbol(sym.TIMES,
		new CSXToken(yyline,yycolumn));	
}

"," {
	return new Symbol(sym.COMMA,
		new CSXToken(yyline,yycolumn));	
}



"!" {
	return new Symbol(sym.NOT,
		new CSXToken(yyline,yycolumn));
}

"["	{
	return new Symbol(sym.LBRACKET,
		new CSXToken(yyline,yycolumn));
}

"]"	{
	return new Symbol(sym.RBRACKET,
		new CSXToken(yyline,yycolumn));
}

"{" {
	return new Symbol(sym.LBRACE,
		new CSXToken(yyline,yycolumn));
}
"}" {
	return new Symbol(sym.RBRACE,
		new CSXToken(yyline,yycolumn));
}

":" {
	return new Symbol(sym.COLON,
		new CSXToken(yyline,yycolumn));
}

//finds that end of file so when we know we are done scanning 

<<EOF>> {
	return new Symbol(sym.EOF,
		new CSXToken(yyline,yycolumn));			
}

// newlines and tabs mean nothing (may python be burned)
(\n | \t) {
	/* */
}

//blank spaces are not significant however the separate things so 
// a b is different then ab
" "	{
	/* */
}

//Identifier Token they consist of a letter followed by optional letters and numbers
{ALFABET}+({ALFABET}*{DIGIT}*)* {
	return new Symbol(sym.IDENTIFIER,
		new CSXIdentifierToken(yytext(),yyline,yycolumn));
}

//Calculates a quote note that if the string has invald tokens in side it is not scanned as a string
//
[\"] (!({QUOTEANYWHERE}|{BACKSLASHANYWHERE}|{NEWLINEANYWHERE})|{ESBACK}|{ESES}|{ESNEW}|{ESTAB})* [\"]  {
	//System.out.println(yytext());
	return new Symbol(sym.STRLIT,
		new CSXStringLitToken(yytext(),yyline,yycolumn));
}

// Calculates a char
[\'] (!({BACKSLASHANYWHERE}|{TICANYWHERE})|{ESTIC}|{ESES}|{ESNEW}|{ESTAB}) [\'] {
	return new Symbol(sym.CHARLIT,
		new CSXCharLitToken(yytext(),yyline,yycolumn));
}
//Add positive or negative integer
[~+]?{DIGIT}+ {
	return new Symbol(sym.INTLIT,
		new CSXIntLitToken(yytext(),
			yyline,yycolumn));
}

"~" {
	return new Symbol(sym.MINUS,
		new CSXToken(yyline,yycolumn));
}

"-" {
	return new Symbol(sym.MINUS,
		new CSXToken(yyline,yycolumn));
}

"+"	{
	return new Symbol(sym.PLUS,
		new CSXToken(yyline,yycolumn));
}

//error token
[^] {
	return new Symbol(sym.error, 
		new CSXErrorToken(yytext(),yyline,yycolumn));
}
