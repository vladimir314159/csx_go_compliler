
import java.io.*;

// abstract superclass; only subclasses are actually created
abstract class ASTNode {

	int 	linenum;
	int	colnum;

	static PrintStream afile;	// File to generate JVM code into

	static int typeErrors =  0;     // Total number of type errors found 
	static int cgErrors =  0;       // Total number of code gen errors 

	static int numberOfLocals =  0; // Total number of local CSX-lite vars

	static int labelCnt = 0;	// counter used to gen unique labels

	static void genIndent(int indent){
		for (int i=1;i<=indent;i++)
			System.out.print("\t");
	}
	static void myAssert(boolean assertion){
		if (! assertion)
			 throw new RuntimeException();
	}

	static void typeMustBe(int testType,int requiredType,String errorMsg) {
		 if ((testType != Types.Error) && (testType != requiredType)) {
                        System.out.println(errorMsg);
                        typeErrors++;
                }
        }
	static void typesMustBeEqual(int type1,int type2,String errorMsg) {
		 if ((type1 != Types.Error) && (type2 != Types.Error) &&
                     (type1 != type2)) {
                        System.out.println(errorMsg);
                        typeErrors++;
                }
        }

	String error() {
		return "Error (line " + linenum + "): ";
        }

	// generate an instruction w/ 0 operands
	static void    gen(String opcode){
        	afile.println("\t"+opcode);
	}

        // generate an instruction w/ 1 operand
	static void  gen(String opcode, String operand){
        	afile.println("\t"+opcode+"\t"+operand);
	}

        // generate an instruction w/ 1 operand
	static void  gen(String opcode, int operand){
        	afile.println("\t"+opcode+"\t"+operand);
	}


	//  generate an instruction w/ 2 operands
	static void  gen(String opcode, String operand1, String operand2){
        	afile.println("\t"+opcode+"\t"+ operand1+"  "+ operand2);
	}

	//  generate an instruction w/ 2 operands
	static void  gen(String opcode, String operand1, int operand2){
        	afile.println("\t"+opcode+"\t"+ operand1+"  "+operand2);
	}

	//      build label of form labeln
	String   buildlabel(int suffix){
                return "label"+suffix;
	}

	//      generate a label for an instruction
	void    genlab(String label){
        	afile.println(label+":");
	}




  	public static SymbolTable st = new SymbolTable();

	ASTNode(){linenum=-1;colnum=-1;}
	ASTNode(int l,int c){linenum=l;colnum=c;}
	boolean   isNull(){return false;}; // Is this node null?
	void Unparse(int indent){}; // This will normally need to be redefined
				    //  in a subclass
	void checkTypes(){}; // This will normally need to be redefined
				    //  in a subclass

	//   codegen translates the AST rooted by this node 
  	//      into JVM code which is written in asmFile.
	//   If no errors occur during code generation,
	//    TRUE is returned, and asmFile should contain a
        //    complete and correct JVM program. 
	//   Otherwise, FALSE is returned and asmFile need not
	//    contain a valid program.

       boolean codegen(PrintStream asmfile){
		throw new Error();};//This version of codegen
                                    // should never be called

        //      cg translates its AST node into JVM code
	//      The code which is written in the shared PrintStream  variable
	//      afile (set by codegen)

         void cg(){}; // This member is normally overridden in subclasses

};


// This class def probably doesn't need to be changed
class nullNode extends ASTNode {
	nullNode(){super();};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
};


// This node is used to root only CSX lite programs 
class csxLiteNode extends ASTNode {
	
	csxLiteNode(fieldDeclsNode decls, stmtsNode stmts, int line, int col){
		super(line,col);
                fields=decls;
		progStmts=stmts;
	}; 

	void Unparse(int indent) {
		System.out.println(linenum + ":" + " {");
		fields.Unparse(1);
		progStmts.Unparse(1);
		System.out.println(linenum + ":" + " } EOF");
	};

	void checkTypes(){
		fields.checkTypes();
        	progStmts.checkTypes();
	}

	boolean isTypeCorrect() {
        	checkTypes();
        	return (typeErrors == 0);
	};

	boolean codegen(PrintStream asmfile) {
        	afile = asmfile;
        	cg();
        	return (cgErrors == 0);
 	}


	void cg() {
        	gen(".class","public","test");
        	gen(".super","java/lang/Object");
        	gen(".method"," public static","main([Ljava/lang/String;)V");
        	fields. cg();
		if (numberOfLocals >0)
        		gen(".limit","locals",numberOfLocals);
        	progStmts. cg();
        	gen("return");
        	gen(".limit","stack",10);
        	gen(".end","method");
	}


   	private stmtsNode 	progStmts;
   	private fieldDeclsNode 	fields;
};


// Root of all ASTs for CSX
class classNode extends ASTNode {
	classNode(identNode id, memberDeclsNode m, int line, int col){
		super(line,col);
		className=id;
		members=m;
	}
	boolean isTypeCorrect() {return true;}; // You need to refine this one
	private identNode	className;
	private memberDeclsNode	members;
};


class memberDeclsNode extends ASTNode {
	memberDeclsNode(fieldDeclsNode f, methodDeclsNode m,
			int line, int col){
		super(line,col);
	 	fields=f;
		methods=m;
	}
	fieldDeclsNode 	fields;
	private methodDeclsNode	methods;
};


class fieldDeclsNode extends ASTNode {
	fieldDeclsNode(){super();}
	fieldDeclsNode(declNode d, fieldDeclsNode f, int line, int col){
		super(line,col);
		thisField = d;
		moreFields = f;
	}
	void Unparse(int indent) {
		thisField.Unparse(indent);
		moreFields.Unparse(indent);
	}; 
	void checkTypes() {
        	thisField.checkTypes();
        	moreFields.checkTypes();
	}; 

	void cg() {
        	thisField.cg();
        	moreFields.cg();
	}; 
	static nullFieldDeclsNode NULL = new nullFieldDeclsNode();
	private declNode	thisField;
	private fieldDeclsNode 	moreFields;
};

class nullFieldDeclsNode extends fieldDeclsNode {
	nullFieldDeclsNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};

// abstract superclass; only subclasses are actually created
abstract class declNode extends ASTNode {
	declNode(){super();};
	declNode(int l,int c){super(l,c);};
};


class varDeclNode extends declNode {
	varDeclNode(identNode id, typeNode t, exprNode e,
			int line, int col){
		super(line,col);
		varName = id;
		varType = t;
		initValue = e;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		varType.Unparse(0);
		System.out.print("  ");
		varName.Unparse(0);
		System.out.println(";");
	};

	void checkTypes() {
        	SymbolInfo     id;
        	id = (SymbolInfo) st.localLookup(varName.idname);
        	if (id != null) {
               		 System.out.println(error() + id.name()+
                                     " is already declared.");
                	typeErrors++;
                	varName.type = new Types(Types.Error);

        	} else {
                	id = new SymbolInfo(varName.idname,
                                         new Kinds(Kinds.Var),varType.type);
                	varName.type = varType.type;
			try {
                		st.insert(id);
			} catch (DuplicateException d) 
                              { /* can't happen */ }
			  catch (EmptySTException e) 
                              { /* can't happen */ }
                	varName.idinfo=id;
        	}
	};


	void cg() {

	//   Give this variable an index equal to numberOfLocals
	//     and remember index in ST

        	varName.idinfo.varIndex = numberOfLocals;
	//   Increment numberOfLocals used in this prog
        	numberOfLocals++;
	};


	private	identNode	varName;
	private	typeNode 	varType;
	private	exprNode 	initValue;
};

class constDeclNode extends declNode {
	constDeclNode(identNode id,  exprNode e,
			int line, int col){
		super(line,col);
		constName=id;
		constValue=e;
	}

	private	identNode	constName;
	private	exprNode 	constValue;
};

class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit,
			int line, int col){
		super(line,col);
		arrayName=id;
		elementType=t;
		arraySize=lit;
	}

	private identNode	arrayName;
	private typeNode 	elementType;
	private intLitNode 	arraySize;
};

abstract class typeNode extends ASTNode {
// abstract superclass; only subclasses are actually created
	typeNode(){super();};
	typeNode(int l,int c, Types t){super(l,c);type = t;};
	static nullTypeNode NULL = new nullTypeNode();

        Types   type; // Used for typechecking -- the type of this typeNode

};


class nullTypeNode extends typeNode {
	nullTypeNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};

class intTypeNode extends typeNode {
	intTypeNode(int line, int col){
		super(line,col, new Types(Types.Integer));
	}
	void Unparse(int indent) {
		System.out.print("int");
	}
	void checkTypes() {
	//      No type checking needed
	}
        // Use default cg() member (from ASTNode)
};

class boolTypeNode extends typeNode {
	boolTypeNode(int line, int col){
		super(line,col, new Types(Types.Boolean));
	}
	void Unparse(int indent) {
		System.out.print("bool");
	}
	void checkTypes() {
	//      No type checking needed
	}
        // Use default cg() member (from ASTNode)
};


class charTypeNode extends typeNode {
	charTypeNode(int line, int col){
		super(line,col, new Types(Types.Character));
	}
	void checkTypes() {
	//      No type checking needed
	}
        // Use default cg() member (from ASTNode)
};

class voidTypeNode extends typeNode {
	voidTypeNode(int line, int col){
		super(line,col, new Types(Types.Void));
	}
	void checkTypes() {
	//      No type checking needed
	}
        // Use default cg() member (from ASTNode)
};

class methodDeclsNode extends ASTNode {
	methodDeclsNode(){super();}
	methodDeclsNode(methodDeclNode m, methodDeclsNode ms,
			int line, int col){
		super(line,col);
		thisDecl=m;
	 	moreDecls=ms;
	}
	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode		thisDecl;
	private methodDeclsNode 	moreDecls;
};


class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
};


class methodDeclNode extends ASTNode {
	methodDeclNode(identNode id, argDeclsNode a, typeNode t,
			fieldDeclsNode f, stmtsNode s, int line, int col){
		super(line,col);
		name=id;
		args=a;
		returnType=t;
		decls=f;
		stmts=s;
	}

	private identNode	name;
	private argDeclsNode	args;
	private typeNode 	returnType;
	private fieldDeclsNode 	decls;
	private stmtsNode 	stmts;
};


// abstract superclass; only subclasses are actually created
abstract class argDeclNode extends ASTNode {
	argDeclNode(){super();};
	argDeclNode(int l,int c){super(l,c);};
};


class argDeclsNode extends ASTNode {
	argDeclsNode(){};
	argDeclsNode(argDeclNode arg, argDeclsNode args,
			int line, int col){
		super(line,col);
		thisDecl=arg;
		moreDecls=args;
	}
	static nullArgDeclsNode NULL = new nullArgDeclsNode();

	private argDeclNode	thisDecl;
	private argDeclsNode 	moreDecls;
};

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};

class arrayArgDeclNode extends argDeclNode {
	arrayArgDeclNode(identNode id, typeNode t, int line, int col){
		super(line,col);
		argName=id;
		elementType=t;
	}

	private identNode	argName;
	private typeNode 	elementType;
};


class valArgDeclNode extends argDeclNode {
	valArgDeclNode(identNode id, typeNode t, int line, int col){
		super(line,col);
		argName=id;
		argType=t;
	}

	private identNode	argName;
	private typeNode 	argType;
};

// abstract superclass; only subclasses are actually created
abstract class stmtNode extends ASTNode {
	stmtNode(){super();};
	stmtNode(int l,int c){super(l,c);};
	static nullStmtNode NULL = new nullStmtNode();
};

class nullStmtNode extends stmtNode {
	nullStmtNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};

class stmtsNode extends ASTNode {
	stmtsNode(stmtNode stmt, stmtsNode stmts, int line, int col){
		super(line,col);
		thisStmt=stmt;
		moreStmts=stmts;
	};
	stmtsNode(){}

	void Unparse(int indent) {
		thisStmt.Unparse(indent);
		moreStmts.Unparse(indent);
	}; 
	void checkTypes() {
        	thisStmt.checkTypes();
        	moreStmts.checkTypes();
	}; 

	void cg() {
        	thisStmt.cg();
        	moreStmts.cg();
	}; 


	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode	thisStmt;
	private stmtsNode 	moreStmts;
};

class nullStmtsNode extends stmtsNode {
	nullStmtsNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};


class asgNode extends stmtNode {
	asgNode(nameNode n, exprNode e, int line, int col){
		super(line,col);
		target=n;
		source=e;
	};

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		target.Unparse(0);
		System.out.print(" = ");
		source.Unparse(0);
		System.out.println(";");
	};

	void checkTypes() {
        	target.checkTypes();
        	source.checkTypes();
        	myAssert(target.kind.val == Kinds.Var); //In CSX-lite all IDs should be vars! 

		typesMustBeEqual(source.type.val, target.type.val,
                        error() + "Both the left and right"
                          	+ " hand sides of an assignment must "
                            	+ "have the same type.");
	}


	void cg() {

        // Translate RHS (an expression)
        	source.cg();

        // Value to be stored is now on the stack
        // Save it into target variable, using the variable's index
        	gen("istore", target.varName.idinfo.varIndex);
	};



	private nameNode	target;
	private exprNode 	source;
};



class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, stmtNode s2, int line, int col){
		super(line,col);
		condition=e;
		thenPart=s1;
		elsePart=s2;
	};

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if (");
		condition.Unparse(0);
		System.out.println(")");
		thenPart.Unparse(indent+1);
		// No else parts in CSX Lite
	};

	void checkTypes() {
        	condition.checkTypes();
        	typeMustBe(condition.type.val, Types.Boolean,
                	error() + "The control expression of an" +
                          	" if must be a bool.");
        	thenPart.checkTypes();
        	// No else parts in CSX Lite
	};

	void cg() {
        	String    out;        // label that will mark end of if stmt

        // translate boolean condition, pushing it onto the stack
        	condition.cg();

        out = buildlabel(labelCnt++);

        // gen conditional branch around then stmt
        	gen("ifeq",out);

        // translate then part
        	thenPart.cg();

        // generate label marking end of if stmt
        	genlab(out);
	};


	private exprNode 	condition;
	private stmtNode 	thenPart;
	private stmtNode 	elsePart;
};


class whileNode extends stmtNode {
	whileNode(identNode i, exprNode e, stmtNode s, int line, int col){
		super(line,col);
	 	label=i;
	 	condition=e;
	 	loopBody=s;
	}

	private exprNode 	label;
	private exprNode 	condition;
	private stmtNode 	loopBody;
};

class readNode extends stmtNode {
	readNode(){}
	readNode(nameNode n, readNode rn, int line, int col){
		super(line,col);
		targetVar=n;
		moreReads=rn;
	}

	static nullReadNode NULL = new nullReadNode();
	private nameNode 	targetVar;
	private readNode 	moreReads;
};


class nullReadNode extends readNode {
	nullReadNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};

class displayNode extends stmtNode {
	displayNode(){}
	displayNode(exprNode val, displayNode pn, int line, int col){
		super(line,col);
		outputValue = val;
		moreDisplays = pn;
	}
	static nullDisplayNode NULL = new nullDisplayNode();
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("print(");
		outputValue.Unparse(0);
		System.out.println(");");
	};

	void checkTypes() {
        	outputValue.checkTypes();
        	typeMustBe(outputValue.type.val, Types.Integer,
                	error() + "Only int values may be printed.");
	};

	void cg() {

        	outputValue.cg();

        // value to be printed is now on the stack
        // Call CSX library routine "printInt(int i)"
        	gen("invokestatic"," CSXLib/printInt(I)V");
	};


	private exprNode 	outputValue;
	private displayNode 	moreDisplays;
};


class nullDisplayNode extends displayNode {
	nullDisplayNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};


class callNode extends stmtNode {
	callNode(identNode id, argsNode a, int line, int col){
		super(line,col);
		methodName=id;
		args=a;
	}

	private identNode	methodName;
	private argsNode 	args;
};



class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col){
		super(line,col);
		returnVal=e;
	}

	private exprNode 	returnVal;
};


class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col){
		super(line,col);
		label=i;
	}

	private identNode 	label;
};



class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col){
		super(line,col);
		label=i;
	}

	private identNode 	label;
};


class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, int line, int col){
		super(line,col);
		decls=f;
		stmts=s;
	}

	private fieldDeclsNode 	decls;
	private stmtsNode 	stmts;
};



class argsNode extends ASTNode {
	argsNode(){}
	argsNode(exprNode e, argsNode a, int line, int col){
		super(line,col);
		argVal=e;
		moreArgs=a;
	}

	static nullArgsNode NULL = new nullArgsNode();
	private exprNode 	argVal;
	private argsNode 	moreArgs;
};


class nullArgsNode extends argsNode {
	nullArgsNode(){};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};


class strLitNode extends exprNode {
	strLitNode(String stringval, int line, int col){
		super(line,col);
                strval=stringval;
	}

	private String 	strval;
};



// abstract superclass; only subclasses are actually created
abstract class exprNode extends ASTNode {
	exprNode(){super();};
	exprNode(int l,int c){
                super(l,c);
		type=new Types();
		kind=new Kinds();
        };
	exprNode(int l,int c,Types t,Kinds k) {
		super(l,c);
                type = t; kind = k;
        };
	static nullExprNode NULL = new nullExprNode();
        protected Types   type; // Used for typechecking: the type of this node
        protected Kinds   kind; // Used for typechecking: the kind of this node
};



class nullExprNode extends exprNode {
	nullExprNode(){super();};
	boolean   isNull(){return true;};
	void Unparse(int indent){};
	void checkTypes(){}; 
        void cg(){};
};


class binaryOpNode extends exprNode {
	binaryOpNode(exprNode e1, int op, exprNode e2, int line, int col,
			Types resultType){
		super(line,col,  resultType, new Kinds(Kinds.Value));
		operatorCode=op;
		leftOperand=e1;
		rightOperand=e2;
	};

	static void printOp(int op) {
		switch (op) {
			case sym.PLUS:
				System.out.print(" + ");
				break;
			case sym.MINUS:
				System.out.print(" - ");
				break;
			default:
				myAssert(false);
		}
	}

	static String toString(int op) {
		switch (op) {
			case sym.PLUS:
				return(" + ");
			case sym.MINUS:
				return(" - ");
			default:
				myAssert(false);
				return "";
		}
	}

	void Unparse(int indent) {
		System.out.print("(");
		leftOperand.Unparse(0);
		printOp(operatorCode);
		rightOperand.Unparse(0);
		System.out.print(")");
	};

	void checkTypes() {
        	myAssert(operatorCode== sym.PLUS
			||operatorCode==sym.MINUS);//Only two bin ops in CSX-lite
        	leftOperand.checkTypes();
        	rightOperand.checkTypes();
        	type = new Types(Types.Integer);
        	typeMustBe(leftOperand.type.val, Types.Integer,
                	error() + "Left operand of" + toString(operatorCode) 
                         	+  "must be an int.");
        	typeMustBe(rightOperand.type.val, Types.Integer,
                	error() + "Right operand of" + toString(operatorCode) 
                         	+  "must be an int.");
	};

	void cg() {
        // First translate the left and right operands
        	leftOperand. cg();
        	rightOperand. cg();
        // Now the values of the operands are on the stack

        	if (operatorCode == sym.PLUS)
               		gen("iadd");
        	else if (operatorCode == sym.MINUS)
                	gen("isub");
        	else    throw new Error(); // Only + and - in CSX-lite
	};


	private exprNode 	leftOperand;
	private exprNode 	rightOperand;
	private int	 	operatorCode; // Token code of the operator
};


class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col){
		super(line,col);
		operand=e;
		operatorCode=op;
	}

	private exprNode 	operand;
	private int	 	operatorCode; // Token code of the operator
};


class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col){
		super(line,col);
		operand=e;
		resultType=t;
	}

	private exprNode 	operand;
	private typeNode 	resultType;
};

class fctCallNode extends exprNode {
	fctCallNode(identNode id, argsNode a, int line, int col){
		super(line,col);
		methodName=id;
		methodArgs=a;
	}

	private identNode 	methodName;
	private argsNode 	methodArgs;
};



class identNode extends exprNode {

	identNode(String identname, int line, int col){
                super(line,col,new Types(Types.Unknown), new Kinds(Kinds.Var));
                idname   = identname;
                nullFlag = false;
        };

        identNode(boolean flag){
                super(0,0,new Types(Types.Unknown), new Kinds(Kinds.Var));
                idname   = "";
                nullFlag = flag;
        };


	boolean   isNull(){return nullFlag;}; // Is this node null?

	static identNode NULL = new identNode(true);

	void Unparse(int indent) {
		System.out.print(idname);
	}


	void checkTypes() {
        	SymbolInfo    id;
        	myAssert(kind.val == Kinds.Var); //In CSX-lite all IDs should be vars! 
        	id = (SymbolInfo) st.localLookup(idname);
        	if (id == null) {
               	 	System.out.println(error() +  idname +
                             " is not declared.");
                typeErrors++;
                type = new Types(Types.Error);
        } else {
                type = id.type; 
                idinfo = id; // Save ptr to correct symbol table entry
        	}
	}


	// In CSX-lite, we don't code gen identNode directly;
        //  Instead, we do translation in parent nodes where
        //   context of identNode is known
        // Hence no cg() member is defined (though you may want
	//	to define one in full CSX)

	public	String 	idname;
	public  SymbolInfo  	idinfo; // symbol table entry for this ident
	private boolean		nullFlag;
};


class intLitNode extends exprNode {
	intLitNode(int val, int line, int col){
		super(line,col,new Types(Types.Integer),
				new Kinds(Kinds.Value));
		intval   = val;
	}
	void Unparse(int indent) {
		System.out.print(intval);
	}
	void checkTypes() {
	//      All intLits are automatically type-correct
	}

	void cg() {
        // Load value of this literal onto stack
        	gen("ldc",intval);
	};


	private int 	intval;
};


class nameNode extends exprNode {
	nameNode(identNode id, exprNode expr, int line, int col){
		super(line,col,new Types(Types.Unknown), new Kinds(Kinds.Var));
		varName=id;
		subscriptVal=expr;
	};

	void Unparse(int indent) {
		varName.Unparse(0); // Subscripts not allowed in CSX Lite
	};

	void checkTypes() {
        	varName.checkTypes(); // Subscripts not allowed in CSX Lite
        	type=varName.type;
	};

	void cg() {
        // Load value of this variable onto stack using its index
       		gen("iload",varName.idinfo.varIndex);
	};



	public identNode 	varName;
	private exprNode 	subscriptVal;
};



class charLitNode extends exprNode {
	charLitNode(char val, int line, int col){
		super(line,col);
		 charval=val;
	}

	private char 	charval;
};


class trueNode extends exprNode {
	trueNode(int line, int col){
		super(line,col);
	}
};


class falseNode extends exprNode {
	falseNode(int line, int col){
		super(line,col);
	}
};

