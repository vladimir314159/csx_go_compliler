import java.util.LinkedList;

import java.io.*;

abstract class ASTNode {
// abstract superclass; only subclasses are actually created

	int linenum;
	int colnum;
	static PrintStream afile;	// File to generate JVM code into

	static int cgErrors =  0;       // Total number of code gen errors 

	static int numberOfLocals =  0; // Total number of local CSX-lite vars

	static int labelCnt = 0;	// counter used to gen unique labels
	static int typeErrors = 0; // Total number of type errors found 
	static void genIndent(int indent) {
		for (int i = 1; i <= indent; i += 1) {
			System.out.print("\t");
		}
	} // genIndent
	static void arraySizeEq(int length1, int length2, String errorMsg){ //note why do people have size of array it should always be lenght.
		if (length1!=length2){
			System.out.println(errorMsg);
			typeErrors++;
		}
	}
	static void asgTypeCheck(Types L, Types R, String errorMsg){
		//System.out.println(L+" "+R);
		if (L.val == Types.Unknown){
			System.out.println(errorMsg+
				"lefthand side of assignment is a undeclaired variable.");
			typeErrors++;
			return;
		}
		if (R.val == Types.Unknown){
			System.out.println(errorMsg+
				"righthand side of assignment is a undeclaired variable.");
			typeErrors++;
			return;
		}
		typeMustBe(L.val, R.val, errorMsg+
		"Type "+L+" must match what is being assigned into "+R+".");
	}
	static void mustBe(boolean assertion) {
		if (! assertion) {
			throw new RuntimeException();
		}
	} // mustBe
	static void typeMustBe(int testType,int requiredType,String errorMsg) {
		if ((testType != Types.Error) && (testType != requiredType)) {
			System.out.println(errorMsg);
			typeErrors++;
		}	
	} // typeMustBe
	static void typesMustBe(int testType,int requiredType0, int requiredType1,String errorMsg) {
		if((testType != Types.Error) && (testType != requiredType0)&&(testType != requiredType1)){
			System.out.println(errorMsg);
			typeErrors++;
		}
	}
	static void type3MustBe(int testType,int requiredType0, int requiredType1, int requiredType2, String errorMsg) {
		if((testType != Types.Error) && (testType != requiredType0)&&(testType != requiredType1)&&(testType != requiredType2)){
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
	} // typesMustBeEqual
	static void MustBeGE0(int val,String errorMsg){
		if (val <= 0){
			System.out.println(errorMsg);
			typeErrors++;
		}
	}
	static void insertNoCatch(SymbolInfo id){
		try {
			st.insert(id);
			//System.out.println("Symbol insert:"+id);
		} catch (DuplicateException d) {
			/* can't happen */
		} catch (EmptySTException e) {
			/* can't happen */
		}
	}
	static private int ParKindCheck(int kind1, int kind2, String errorMsg){
		if(kind1 == Kinds.ScalarParam){
			if( kind2 == Kinds.Const || kind2 == Kinds.Var || kind2 == Kinds.Value || kind2 == Kinds.Func || kind2 == Kinds.ScalarParam || kind2 == Kinds.Other){
				return 0;
			}
			else {
				System.out.println(errorMsg);
				typeErrors++;
				return -1;
			}
		}
		/*
		if(kind1 == Kinds.ArrayParam){
			if( kind2 == Kinds.Array ){
				return 0;
			}
			else{
				System.out.println("2:"+errorMsg);
				typeErrors++;
				return -1;
			}
		}
		*/
		return 0;
	}
	static private int ParTypeCheck(int type1,int type2, String errorMsg){
		int[] types = {Types.Integer,Types.Character, Types.Boolean};
		//System.out.println(type1);
		//System.out.println(type2);
		for( int i = 0; i < types.length; i++){
			//System.out.println("i:"+i);
			if(type1 == types[i]){
				if (type2 == types[i]){
					/* great */
					//System.out.println("HUGE");
				}
				else {
					System.out.println(errorMsg);
					typeErrors++;
					return -1;
				}
				//System.out.println("SAD");
			}
		}
		return 0;
		//System.out.println("foo");
	}
	static void checkParams(LinkedList<SymbolInfo> called, LinkedList<SymbolInfo> caller, String errorMsg){
		if (called.isEmpty() && caller.isEmpty()){
			/* both are void so call matchs */
			return;
		}
		if (called.isEmpty() && !caller.isEmpty()){
			System.out.println(errorMsg);
			typeErrors++;
			return;
		}
		if (!called.isEmpty() && caller.isEmpty()){
			System.out.println(errorMsg);
			typeErrors++;
			return;
		}
		if(called.size() != caller.size()){
			System.out.println(called.size()+caller.size()+errorMsg);
			typeErrors++;
			return;
		}
		// call sequence 
		for (SymbolInfo arg: called){
			if ( -1 == ParKindCheck(arg.kind.val, caller.getFirst().kind.val, errorMsg) ) break;
			if ( -1 == ParTypeCheck(arg.type.val, caller.getFirst().type.val, errorMsg) ) break;
			caller.remove(0);
		}																																														
	}     
	String error() {
		return "Error (line " + linenum + "): ";
	} // error
	ASTNode() {
		linenum = -1;
		colnum = -1;
	} // ASTNode()

	ASTNode(int line, int col) {
		linenum = line+1;
		colnum = col;
	} // ASTNode(line, col)
	public static SymbolTable st = new SymbolTable();
	boolean isNull() {
		return false; // often redefined in a subclass
	} // isNull()

	void Unparse(int indent) {
		// This routine is normally redefined in a subclass
	} // Unparse()
	void checkTypes() {}
	// This will normally need to be redefined in a subclass
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
	boolean codegen(PrintStream asmfile){
		throw new Error();};//This version of codegen
                                    // should never be called

        //      cg translates its AST node into JVM code
	//      The code which is written in the shared PrintStream  variable
	//      afile (set by codegen)

         void cg(){}; // This member is normally overridden in subclasses
} // class ASTNode

class nullNode extends ASTNode {
// This class definition probably doesn't need to be changed
	nullNode() {
		super();
	}

	boolean isNull() {
		return true;
	}

	void Unparse(int indent) {
		// no action
	}
} // class nullNode

class csxLiteNode extends ASTNode {
// This node is used to root CSX lite programs 
	
	csxLiteNode(stmtsNode stmts, int line, int col) {
		super(line, col);
		progStmts = stmts;
	} // csxLiteNode() 

	void Unparse(int indent) {
		System.out.println(linenum + ":" + " {");
		progStmts.Unparse(1);
		System.out.println(linenum + ":" + " }");
	} // Unparse()
	boolean codegen(PrintStream asmfile) {
		afile = asmfile;
		cg();
		return (cgErrors == 0);
 }
 /*
	void cg() {
		gen(".class","public","test");
		gen(".super","java/lang/Object");
		gen(".method"," public static","main([Ljava/lang/String;)V");
		progStmts.cg();
		if (numberOfLocals >0)
			gen(".limit","locals",numberOfLocals);
		//progStmts. cg();
		//gen("return");
		//gen(".limit","stack",9);
		//gen(".end","method");
	}
	*/
	private final stmtsNode progStmts;
} // class csxLiteNode
/*
class ProgramNode extends ASTNode {
	ProgramNode(identNode id, LinkedList<varDeclNode> D, LinkedList<funcDeclNode> F, int line, int col){
		super(line, col);
		packageName = id;
		varList = D;
		funcList = F;
	}
	//void Unparse(int indent){
	//}
	private final identNode packageName;
	private final LinkedList<varDeclNode> varList;
	private final LinkedList<funcDeclNode> funcList;
}
*/

class ProgramNode extends ASTNode {
	ProgramNode(identNode id, memberDeclsNode m,int line, int col){
		super(line, col);
		 packageName = id;
		 members = m;
	}
	void Unparse(int indent) { 
		System.out.print(linenum+": "+"package ");
		packageName.Unparse(0);
		System.out.print("\n");
		members.Unparse(0);
	}
	void checkTypes() {
		SymbolInfo id;
		id = new SymbolInfo(packageName.idname,
				new Kinds(Kinds.Value),packageName.type);
		try {
			st.insert(id);
			//System.out.println("Symbol insert:"+id);
		} catch (DuplicateException d) {
			/* can't happen */
		} catch (EmptySTException e) {
			/* can't happen */
		}
		packageName.idinfo = id;
		packageName.checkTypes();
		members.checkTypes();
	} // checkTypes
	boolean isTypeCorrect() {
		checkTypes();
		return (typeErrors == 0);
	} // isTypeCorrect
	boolean codegen(PrintStream asmfile) {
		afile = asmfile;
		cg();
		return (cgErrors == 0);
 	}
	void cg() {
		members.cg();
	}
	private identNode packageName;
	private memberDeclsNode members;
};
/*
class funcDeclNode extends ASTNode {
	funcDeclNode(identNode i, LinkedList<argDeclNode> D, typeNode R, blockNode B, int line, int col){
		super(line, col);
		name = i;
		args = D;
		returnType = R;
		blockNode = B;
	}
	//Unparse(int indent){}
	
	private final identNode name;
	private final LinkedList<argDeclNode> args;
	private final typeNode returnType;
	private final blockNode blockNode; 

}
*/

class classNode extends ASTNode {

	classNode(identNode id, memberDeclsNode memb, int line, int col) {
		super(line, col);
		className = id;
		members = memb;
	} // classNode

	private final identNode className;
	private final memberDeclsNode members;
} // class classNode

class memberDeclsNode extends ASTNode {
	memberDeclsNode(fieldDeclsNode f, methodDeclsNode m, int line, int col) {
		super(line, col);
		fields = f;
		methods = m;
	}
	void Unparse(int indent) {
		genIndent(indent);
		fields.Unparse(1);
		methods.Unparse(1);
	}
	void checkTypes() {
		fields.checkTypes();
		methods.checkTypes();
	}

	void cg() {
		gen(".class","public","test");
		gen(".super","java/lang/Object");
		fields.cg();
		gen(".method"," public static","main([Ljava/lang/String;)V");
		gen("invokestatic","test/main()V");
		gen("return");
		if (numberOfLocals >=0)
			gen(".limit","locals",numberOfLocals+1);
		gen(".limit","stack",20);
		gen(".end","method");
		methods.cg();
	}
	private final fieldDeclsNode fields;
	private final methodDeclsNode methods;
} // class memberDeclsNode

class fieldDeclsNode extends ASTNode {
	fieldDeclsNode() {
		super();
	}
	fieldDeclsNode(declNode d, fieldDeclsNode f, int line, int col) {
		super(line, col);
		thisField = d;
		moreFields = f;
	}
	void Unparse(int indent) {
		thisField.Unparse(indent);
		moreFields.Unparse(indent);
	}
	void checkTypes() {
		if (thisField!=null){ 
			thisField.checkTypes();
		}
		moreFields.checkTypes();
	}
	void cg() {
		System.out.println("thisField.cg()"+thisField+moreFields);
		if(thisField!=null){
			thisField.cg();
		}
		if(moreFields!=null){
			moreFields.cg();
		}
	}
	static nullFieldDeclsNode NULL = new nullFieldDeclsNode();
	private declNode thisField;
	private fieldDeclsNode moreFields;
} // class fieldDeclsNode

class nullFieldDeclsNode extends fieldDeclsNode {
	nullFieldDeclsNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
	void checkTypes() {}
} // class nullFieldDeclsNode

// abstract superclass; only subclasses are actually created
class declNode extends ASTNode {
	declNode() {
		super();
	}
	declNode(int l, int c) {
		super(l, c);
	}
} // class declNode

class varDeclNode extends declNode {
	varDeclNode(identNode id, typeNode t, exprNode e, int line, int col) {
		super(line, col);
		varName = id;
		varType = t;
		initValue = e;
	}
	void Unparse(int indent) {
		System.out.print(linenum+": ");
		genIndent(indent);
		System.out.print("var ");
		varName.Unparse(0);
		//System.out.print(varType);
		varType.Unparse(0);
		System.out.print(" = ");
		initValue.Unparse(0);
		System.out.println(" ;");
	}
	void checkTypes() {
		if (!initValue.isNull()){
			initValue.checkTypes();
			asgTypeCheck(varType.type,initValue.type,error());
		}
		SymbolInfo id;
		id = (SymbolInfo) st.localLookup(varName.idname);
		if (id == null) {
			id = new SymbolInfo(varName.idname,
				new Kinds(Kinds.Var),varType.type);
			varName.type = varType.type;
			try {
				st.insert(id);
				//System.out.println("Symbol insert:"+id);
			} catch (DuplicateException d) {
				/* can't happen */
			} catch (EmptySTException e) {
				/* can't happen */
			}
			varName.idinfo = id;
		} else {
			System.out.println(error() + id.name() + " is already declared in the current scope.");
			typeErrors++;
			varName.type = new Types(Types.Error);
		} // id != null
		varName.checkTypes();
		varType.checkTypes();
		//System.out.println(error()+varType.type+"\t"+initValue.type);


	}
	void cg() {
		//   Give this variable an index equal to numberOfLocals
		//     and remember index in ST
		System.out.println("var decls");
		//varName.cg();
		varType.cg();
		initValue.cg();
		if(initValue.isNull()){
			gen("iconst_m1");		//this is fine since we already checked that local variables will be used.
		}
		varName.idinfo.varIndex = numberOfLocals;
		gen("istore", varName.idinfo.varIndex);

		//   Increment numberOfLocals used in this prog
		numberOfLocals++;
	};
	private final identNode varName;
	private final typeNode varType;
	private final exprNode initValue;
} // class varDeclNode

class constDeclNode extends declNode {
	constDeclNode(identNode id,  exprNode e, int line, int col) {
		super(line, col);
		constName = id;
		constValue = e;
	}
	void Unparse(int indent) {
		System.out.print(linenum+": ");
		genIndent(indent);
		System.out.print("const ");
		constName.Unparse(0);
		//System.out.print(varType);
		System.out.print(" = ");
		constValue.Unparse(0);
		System.out.println(" ;");
	}
	void checkTypes() {
		SymbolInfo id;
		id = (SymbolInfo) st.localLookup(constName.idname);
		if (id == null) {
			//System.out.println("foo");
			id = new SymbolInfo(constName.idname,
				new Kinds(Kinds.Const),constValue.type);
			constName.type = constValue.type;
			//constName.kind = new Kinds(Kinds.Const);
			//System.out.println("constValue.type:\t"+constName.type);
			try {
				st.insert(id);
				//System.out.println("Symbol insert:"+id);
			} catch (DuplicateException d) {
				/* can't happen */
			} catch (EmptySTException e) {
				/* can't happen */
			}
			constName.idinfo = id;
		} else {
			System.out.println(error() + id.name() + " is already declared.");
			typeErrors++;
			constName.type = new Types(Types.Error);
		} // id != null
		constName.checkTypes();
		constValue.checkTypes();
	}
	void cg(){
		//   Give this variable an index equal to numberOfLocals
		//     and remember index in ST
		System.out.println("const decls");
		//constName.cg();
		constValue.cg();
		constName.idinfo.varIndex = numberOfLocals;
		gen("istore", constName.idinfo.varIndex);

		//   Increment numberOfLocals used in this prog
		numberOfLocals++;
	};
	private final identNode constName;
	private final exprNode constValue;
} // class constDeclNode

class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit, int line, int col) {
		super(line, col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
	}
	void Unparse(int indent) {
		System.out.print(linenum+": ");
		genIndent(indent);
		System.out.print("var ");
		arrayName.Unparse(0);
		//System.out.print(varType);
		elementType.Unparse(0);
		System.out.print("[");
		arraySize.Unparse(0);
		System.out.println("] ;");
	}
	void checkTypes() {
		elementType.checkTypes();
		arraySize.checkTypes();
		//System.out.println("фылвоадж:"+elementType.type);
		typesMustBe(arraySize.type.val, Types.Integer, Types.Character, error()+
			"Elements of array may only contain type int or char.");
		MustBeGE0(arraySize.intval, error()+
			"Array size must be greater then 0.");
		SymbolInfo id;
		id = (SymbolInfo) st.localLookup(arrayName.idname);
		if (id == null) {
			arrayName.type = elementType.type;
			id = new SymbolInfo(arrayName.idname,
				new Kinds(Kinds.Array,arraySize.intval),arrayName.type,arraySize.intval);
			try {
				st.insert(id);
				//System.out.println("Symbol insert:"+id);
			} catch (DuplicateException d) {
				/* can't happen */
			} catch (EmptySTException e) {
				/* can't happen */
			}
			arrayName.idinfo = id;
		} else {
			System.out.println(error() + id.name() + " is already declared.");
			typeErrors++;
			arrayName.type = new Types(Types.Error);
		} // id != null
		arrayName.checkTypes();

	}
	void cg(){
		//arrayName.cg();
		//elementType.cg();
		arraySize.cg();
		if(elementType.type.val == Types.Integer){
			gen("newarray","int");
			//gen("dup");
		}
		if(elementType.type.val == Types.Boolean){
			gen("newarray","int");
		}
		arrayName.idinfo.varIndex = numberOfLocals;
		gen("astore",arrayName.idinfo.varIndex);
		numberOfLocals++;
	}
	private final identNode arrayName;
	private final typeNode elementType;
	private final intLitNode arraySize;
} // class arrayDeclNode

abstract class typeNode extends ASTNode {
// abstract superclass; only subclasses are actually created
	typeNode() {
		super();
	}
	typeNode(Types t){
		super();
		type = t;
	}
	typeNode(int l, int c,Types t) {
		super(l, c);
		type = t;
	}
	Types type;
	static nullTypeNode NULL = new nullTypeNode();
} // class typeNode

class nullTypeNode extends typeNode {
	nullTypeNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
} // class nullTypeNode

class intTypeNode extends typeNode {
	intTypeNode(int line, int col) {
		super(line, col,new Types(Types.Integer));
	}
	void Unparse(int indent) {
		System.out.print(" int");
	}
} // class intTypeNode

class boolTypeNode extends typeNode {
	boolTypeNode(int line, int col) {
		super(line, col, new Types(Types.Boolean));
	}
	void Unparse(int indent) {
		System.out.print(" bool");
	}
} // class boolTypeNode

class charTypeNode extends typeNode {
	charTypeNode(int line, int col) {
		super(line, col,new Types(Types.Character));
	}
	void Unparse(int indent) {
		System.out.print(" char");
	}
} // class charTypeNode

class voidTypeNode extends typeNode {
	voidTypeNode(int line, int col) {
		super(line, col, new Types(Types.Void));
	}
	voidTypeNode(){
		super(new Types(Types.Void));
	}
} // class voidTypeNode

class methodDeclsNode extends ASTNode {
	methodDeclsNode() {
		super();
	}
	methodDeclsNode(methodDeclNode m, methodDeclsNode ms,
			int line, int col) {
		super(line, col);
		thisDecl = m;
		 moreDecls = ms;
	}
	
	void Unparse(int indent) {
		thisDecl.Unparse(indent);
		moreDecls.Unparse(indent);
	}
	void checkTypes() {
		if(thisDecl != null){
			thisDecl.checkTypes();
		}
		if(moreDecls != null){
			moreDecls.checkTypes();
		}
	}
	void cg() {
		if(thisDecl != null){
			thisDecl.cg();
		}
		if(moreDecls != null){
			moreDecls.cg();
		}
	}
	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode thisDecl;
	private methodDeclsNode moreDecls;
} // class methodDeclsNode 

class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullMethodDeclsNode 

class methodDeclNode extends ASTNode {
	methodDeclNode(identNode id, argDeclsNode a, typeNode t,
			fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line, col);
		name = id;
		args = a;
		returnType = t;
		decls = f;
		stmts = s;

	}

	void Unparse(int indent) {
		System.out.print(linenum+": ");
		genIndent(indent);
		System.out.print("func ");
		name.Unparse(indent);
		System.out.print(" ( ");
		args.Unparse(indent);
		System.out.print(" ) ");
		returnType.Unparse(indent);
		System.out.println(" { ");
		decls.Unparse(indent+1);
		stmts.Unparse(indent+1);
		System.out.print(linenum+": ");
		genIndent(indent);
		System.out.print("}\n");
	}
	void checkTypes() {
		//System.out.println(name.idname);
		nameOfFunc = name.idname;
		returnType.checkTypes();
		SymbolInfo mainLookup;
		mainLookup = (SymbolInfo)st.globalLookup("main");
		if (mainLookup != null){
			System.out.println(error()+"Cannot declares functions after main");
			typeErrors++;
		}
		if (returnType.type == null){
			returnType = new voidTypeNode();
		}
		SymbolInfo id;
		//System.out.println("returnType:\t"+returnType.type);
		args.checkTypes();
		for (String arg: argDeclsNode.params){
			SymbolInfo argument = (SymbolInfo)st.localLookup(arg);
			paramTypes.add(argument);
			//System.out.println(argument.name());
		}
		argDeclsNode.params.clear();
		//System.out.println(returnType.type);
		id = new SymbolInfo(name.idname,
				new Kinds(Kinds.Func),returnType.type,paramTypes);
				
		try {
			st.insert(id);
			//System.out.println("Symbol insert:"+id);
		} catch (DuplicateException d) {
			/* can't happen */
		} catch (EmptySTException e) {
			/* can't happen */
		}
		name.checkTypes();
		st.openScope();

		insertNoCatch(new SymbolInfo("func", new Kinds(Kinds.Func), returnType.type));
		//insertNoCatch(new SymbolInfo(nameOfFunc, new Kinds(Kinds.Func), returnType.type));
		decls.checkTypes();
		stmts.checkTypes();
		if ( returnType.type.val != Types.Void){ // a non void function must return.
			SymbolInfo func =  (SymbolInfo)st.globalLookup("func");//lookup function 
			if (!func.returned){ //func has not ben returned.
				System.out.println(error()+"no return value on a function returning "+returnType.type);
				typeErrors++;
			}
		}
		try{
			st.closeScope();
		} catch(EmptySTException e){
			// can't happen
		}
	}

	void cg() {
		//name.cg();
		gen(".method"," public static",name.idname+"()V");
		if (numberOfLocals >=0)
			gen(".limit","locals",numberOfLocals+2);
		args.cg();
		returnType.cg();
		decls.cg();
		stmts.cg();
		gen("return");
	
		gen(".limit","stack",20);
		gen(".end","method");

	}
	public String nameOfFunc;
	public LinkedList<SymbolInfo> paramTypes = new LinkedList<SymbolInfo>(); 
	private final identNode name;
	private final argDeclsNode args;
	private typeNode returnType;
	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class methodDeclNode 

// abstract superclass; only subclasses are actually created
abstract class argDeclNode extends ASTNode {
	argDeclNode() {
		super();
	}
	argDeclNode(int l, int c) {
		super(l, c);
		
	}
	public String name;
	protected Types type; // Used for typechecking: the type of this node
	protected Kinds kind; // Used for typechecking: the kind of this node
}

class argDeclsNode extends ASTNode {
	argDeclsNode() {}
	argDeclsNode(argDeclNode arg, argDeclsNode args,
			int line, int col) {
		super(line, col);
		thisDecl = arg;
		moreDecls = args;
	}
	void Unparse(int indent) {
		thisDecl.Unparse(indent);
		moreDecls.Unparse(indent);
	}
	void checkTypes() {
		if(thisDecl!=null){
			thisDecl.checkTypes();
			//params.add(thisDecl.name);
		}
		if(moreDecls!=null){
			moreDecls.checkTypes();
		}	
	}
	public static LinkedList<String> params = new LinkedList<String>();
	static nullArgDeclsNode NULL = new nullArgDeclsNode();
	private argDeclNode thisDecl;
	private argDeclsNode moreDecls;
} // class argDeclsNode 

class oneDeclNode extends argDeclsNode {
	oneDeclNode(argDeclNode f, int line, int col){
		super();
		arg = f;
	}
	void Unparse(int indent) {
		arg.Unparse(indent);
	}
	void checkTypes() {
		arg.checkTypes();
		params.add(arg.name);
	}
	private argDeclNode arg;
}
class comaDeclsNode extends argDeclsNode {
	comaDeclsNode(argDeclNode fD,argDeclsNode cD, int line, int col){
		super();
		formalDecl = fD;
		someFormals = cD;
	}
	void Unparse(int indent) {
		formalDecl.Unparse(indent);
		System.out.print(" , ");
		someFormals.Unparse(indent);
	}
	void checkTypes() {
		formalDecl.checkTypes();
		params.add(formalDecl.name);
		someFormals.checkTypes();

	}
	private argDeclNode formalDecl;
	private argDeclsNode someFormals;
}

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgDeclsNode 

class arrayArgDeclNode extends argDeclNode {
	arrayArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		elementType = t;
	}
	void Unparse(int indent) {
		argName.Unparse(indent);
		System.out.print(" [ ] ");
		elementType.Unparse(indent);
	}
	void checkTypes() {
		//System.out.println("asdf:"+argName.idname);
		elementType.checkTypes();
		type = elementType.type;
		name = argName.idname;
		SymbolInfo id = new SymbolInfo(argName.idname, new Kinds(Kinds.ArrayParam), type);
		//System.out.println("inserted: "+id);
		insertNoCatch(id);
		argName.checkTypes();
	}
	private final identNode argName;
	private final typeNode elementType;
} // class arrayArgDeclNode 

class valArgDeclNode extends argDeclNode {
	valArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		argType = t;
	}
	void Unparse(int indent) {
		argName.Unparse(indent);
		argType.Unparse(indent);
	}
	void checkTypes() {
		//System.out.println("asdf:"+argName.idname);
		argType.checkTypes();
		type = argType.type;
		name = argName.idname;
		//System.out.println(type);
		SymbolInfo id = new SymbolInfo(argName.idname, new Kinds(Kinds.ScalarParam), type);
		//System.out.println("inserted: "+id);
		//SymbolInfo l = (SymbolInfo)st.globalLookup(argName.idname);
		//System.out.println("looked up type:"+l.type);
		insertNoCatch(id);
		argName.checkTypes();
		//params.add(argName.idname);
		
	}

	private final identNode argName;
	private final typeNode argType;
} // class valArgDeclNode 

// abstract superclass; only subclasses are actually created
abstract class stmtNode extends ASTNode {
	stmtNode() {
		super();
	}
	stmtNode(int l, int c) {
		super(l, c);
	}
	void checkTypes() {}
	//protected Types type; // Used for typechecking: the type of this node
	//protected Kinds kind; // Used for typechecking: the kind of this node
	static nullStmtNode NULL = new nullStmtNode();
}

class nullStmtNode extends stmtNode {
	nullStmtNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {	}
} // class nullStmtNode 

class stmtsNode extends ASTNode {
	stmtsNode(stmtNode stmt, stmtsNode stmts, int line, int col) {
		super(line, col);
		thisStmt = stmt;
		moreStmts = stmts;
	}
	stmtsNode() {}

	void Unparse(int indent) {
		thisStmt.Unparse(indent);
		moreStmts.Unparse(indent);
	} 
	void checkTypes() {
		thisStmt.checkTypes();
		moreStmts.checkTypes();
		/*
		if (thisStmt == null){
			thisStmt.checkTypes();
		}
		if(!moreStmts.isNull()){
			moreStmts.checkTypes();
		}
		*/
	}
	void cg(){
		if(thisStmt!=null){
			thisStmt.cg();
		}
		if(moreStmts!=null){
			moreStmts.cg();
		}
	}
	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode thisStmt;
	private stmtsNode moreStmts;
} // class stmtsNode 

class nullStmtsNode extends stmtsNode {
	nullStmtsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes(){}

} // class nullStmtsNode 

class asgNode extends stmtNode {
	asgNode(exprNode n, exprNode e, int line, int col) {
		super(line, col);
		target = n;
		source = e;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		target.Unparse(0);
		System.out.print(" = ");
		source.Unparse(0);
		System.out.println(";");
	}

	void checkTypes() {
		target.checkTypes();
		source.checkTypes();
		//asgTypeCheck(target.type,source.type,error());
		/*
		target.Unparse(0);
		System.out.println(":"+target.type);
		source.Unparse(0);
		System.out.println(":"+source.type);
		*/
		//id = (SymbolInfo)st.globalLookup(target.idname);
		if (target.kind.val == Kinds.Array){
			//System.out.println(target.kind.length);
			if(source.kind.val == Kinds.Array){
				arraySizeEq(target.kind.length, source.kind.length, error()+
					"array of size "+source.kind.length+" cannot be assigned to array of size "+
					target.kind.length+".");
				return;
			}
			if(source.type.val == Types.String){
				int strlen = ((strLitNode)source).length;
				if(strlen != target.kind.length ){
					System.out.println(error()+"string of size "+strlen+" cannot be assigned to array of size "+
					target.kind.length+".");
					typeErrors++;
				}
				return;
			}

		}		
		if (target.kind.val == Kinds.Const){
			System.out.println(error()+"cannot assign into const variable.");
			typeErrors++;
		}
		else {
			//System.out.println("ASG:\t"+target.type+"\t"+source.type);
			typesMustBeEqual(target.type.val, source.type.val, error()+
				target.type+" cannot be assigned to type "+source.type+".");
		}
	}
	void cg() {

        // Translate RHS (an expression)

			System.out.println("ad:\t"+target.getClass().getName());

        // Value to be stored is now on the stack
		// Save it into target variable, using the variable's index
			if(target.getClass().getName() == "identNode"){
				source.cg();
				gen("istore", ((identNode)target).idinfo.varIndex);
			}
			else if(target.getClass().getName() == "nameSubNode"){

				((nameSubNode)target).cgForAsg();
				source.cg();
				gen("iastore");//, ((nameSubNode)target).idinfo.varIndex);


			}

	};
	private final exprNode target;
	private final exprNode source;
} // class asgNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode vD, stmtsNode st, int line, int col){
		super(line,col);
		vardecl = vD;
		stmts = st;
	}
	void Unparse(int indent) {
		System.out.print(linenum+": ");
		genIndent(indent);
		System.out.println("{");
		vardecl.Unparse(indent+1);
		stmts.Unparse(indent+1);
		System.out.print(linenum+": ");
		genIndent(indent);		//never will be out of bounds error since we indented a if so we can go back.
		System.out.println("}");
	}
	void checkTypes(){
		st.openScope();
		vardecl.checkTypes();
		stmts.checkTypes();
		try {
			st.closeScope();
		} catch(EmptySTException e){
			/* can't happen */
		}
	}

	void cg(){
		vardecl.cg();
		stmts.cg();
	};
	private final fieldDeclsNode vardecl;
	private final stmtsNode	stmts;
}
class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, stmtNode s2, int line, int col) {
		super(line, col);
		condition = e;
		thenPart = s1;
		elsePart = s2;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if (");
		condition.Unparse(0);
		System.out.println(") ");
		thenPart.Unparse(indent);
		//System.out.print(linenum + ":");
		//genIndent(indent);
		if (!elsePart.isNull()){
			//System.out.println("Not null");
			elsePart.Unparse(indent+1);
		}
		// No else parts in CSX-lite
	}
	void checkTypes() {
		condition.checkTypes();
		typeMustBe(condition.type.val, Types.Boolean, error()+
			"Condition in if statement must be Boolean.");

		st.openScope();
		thenPart.checkTypes();
		try {
			st.closeScope();
		} catch(EmptySTException e){
			/* Can't happen */ 
		}
		st.openScope();
		elsePart.checkTypes();
		try {
			st.closeScope();
		} catch(EmptySTException e){
			/* Can't happen */ 
		}
	}
	void cg() {
		//System.out.println("ada\n");
		String    out;        // label that will mark end of if stmt
		String    gotoend;
	// translate boolean condition, pushing it onto the stack
		condition.cg();

		out = buildlabel(labelCnt++);
		gotoend = buildlabel(labelCnt++);
	// gen conditional branch around then stmt
		gen("ifeq",out);
	// translate then part
		thenPart.cg();
		gen("goto",gotoend);
	// generate label marking end of if stmt
		genlab(out);
		if (!elsePart.isNull()){
			elsePart.cg();
		}
		genlab(gotoend);
	};
	private final exprNode condition;
	private final stmtNode thenPart;
	private final stmtNode elsePart;
} // class ifThenNode 
class forIdentNode extends stmtNode {
	forIdentNode(identNode i, exprNode e, stmtNode b, int line, int col){
		super(line,col);
		exp = e;
		block = b;
		ident = i;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		ident.Unparse(indent);
		System.out.print(" : ");
		System.out.print("for");
		System.out.print("(");
		exp.Unparse(0);
		System.out.println(")");
		block.Unparse(indent);
	}

	void checkTypes() {
		
	}
	private identNode ident;
	private exprNode exp;
	private stmtNode block;
}
class forNode extends stmtNode {
	forNode(exprNode e, stmtNode b, int line, int col){
		super(line,col);
		exp = e;
		block = b;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("for");
		System.out.print("(");
		exp.Unparse(0);
		System.out.println(")");
		block.Unparse(indent);
	}
	void checkTypes() {
		typeMustBe(exp.type.val, Types.Boolean, error()+
			"Condition in for statement must be Boolean.");
		st.openScope();
		exp.checkTypes();
		block.checkTypes();
		try{
			st.closeScope();
		} catch(EmptySTException e){
			/* */ 
		}
	}
	void cg() {
		String beginLoop, dontLoop;
		beginLoop = buildlabel(labelCnt++);
		dontLoop = buildlabel(labelCnt++);
		genlab(beginLoop);
		exp.cg();
		gen("ifeq",dontLoop);
		block.cg();
		gen("goto",beginLoop);
		genlab(dontLoop);
	}
	private exprNode exp;
	private stmtNode block;
}
class whileNode extends stmtNode {
	whileNode(exprNode i, exprNode e, stmtNode s, int line, int col) {
		super(line, col);
	 label = i;
	 condition = e;
	 loopBody = s;
	}

	private final exprNode label;
	private final exprNode condition;
	private final stmtNode loopBody;
} // class whileNode 
class stmtReadNode extends stmtNode {
	stmtReadNode(stmtNode r, int line, int col){
		super(line, col);
		read = r;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("read ");
		read.Unparse(indent);
		System.out.println(";");
	}
	void checkTypes(){
		read.checkTypes();
	}
	void cg(){
		read.cg();
	}
	private stmtNode read;
}
class readNode extends stmtNode {
	readNode() {}
	readNode(exprNode n, stmtNode rn, int line, int col) {
		super(line, col);
		 targetVar = n;
		 moreReads = rn;
	}
	void Unparse(int indent) {
		targetVar.Unparse(indent);
		moreReads.Unparse(indent);
	}
	void checkTypes() {
		if(targetVar !=null ){//&& targetVar.kind.val == Kinds.Var){
			targetVar.checkTypes();
			if(targetVar.kind.val != Kinds.Var){
				System.out.println(error()+"can only read in a variable of type char or int");
				typeErrors++;
			}
			typesMustBe(targetVar.type.val, Types.Character,Types.Integer, error()+
				"Can only read char or int");
			moreReads.checkTypes();
		}
	}
	void cg(){
		if(targetVar.type.val == Types.Integer){
			gen("invokestatic"," CSXLib/readInt()I");
			gen("istore",((identNode)targetVar).idinfo.varIndex);
		}
		else if(targetVar.type.val == Types.Character){
			gen("invokestatic"," CSXLib/readChar()C");
			gen("istore",((identNode)targetVar).idinfo.varIndex);
		}
	}

	static nullReadNode NULL = new nullReadNode();
	private exprNode targetVar;
	private stmtNode moreReads;
} // class readNode 

class nullReadNode extends readNode {
	nullReadNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullReadNode 

class printNode extends stmtNode {
	printNode(stmtNode s, int line, int col){
		super(line,col);
		prints = s;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("print ");
		prints.Unparse(indent);
		System.out.println(";");
	}
	void checkTypes(){
		prints.checkTypes();
	}
	void cg() {
		prints.cg();
		System.out.println("foo\n\n");
	// value to be printed is now on the stack
	// Call CSX library routine "printInt(int i)"

};
	private stmtNode prints;
}
class displayNode extends stmtNode {
	displayNode() {}
	displayNode(exprNode val, displayNode pn, int line, int col) {
		super(line, col);
		outputValue = val;
		moreDisplays = pn;
	}
	void Unparse(int indent) {
		outputValue.Unparse(indent);
		moreDisplays.Unparse(indent);
	}

	void checkTypes() {
		if ( outputValue != null){
			outputValue.checkTypes();
		}
		if (moreDisplays != null){
			moreDisplays.checkTypes();
		}
	}
	void cg(){
		if ( outputValue != null){
			outputValue.cg();
			System.out.println("kinds:\t"+outputValue.kind);
			/*if(outputValue.kind.val == Kinds.Array){
				gen("iaload");//,((nameSubNode)outputValue).idinfo.varIndex);
			}*/
			switch(outputValue.type.val){
				case Types.Integer:
					gen("invokestatic"," CSXLib/printInt(I)V");
					break;
				case Types.Character:
					gen("invokestatic"," CSXLib/printChar(C)V");
					break;
				case Types.String:
					gen("invokestatic"," CSXLib/printString(Ljava/lang/String;)V");
					break;
				case Types.Boolean:
					gen("invokestatic"," CSXLib/printBool(Z)V");
					break;
				default:
					throw new Error("Can't happen.");
			}
		}
		//gen("pop");
		if (moreDisplays != null){
			moreDisplays.cg();
		}
	}
	static nullDisplayNode NULL = new nullDisplayNode();

	private exprNode outputValue;
	private displayNode moreDisplays;
} // class displayNode 

class nullDisplayNode extends displayNode {
	nullDisplayNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullDisplayNode 

class callNode extends stmtNode {
	callNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		args = a;
	}

	private final identNode methodName;
	private final argsNode args;
} // class callNode 

class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col) {
		super(line, col);
		returnVal = e;
	}
	returnNode(int line, int col){
		super(line, col);
		returnVal = null;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("return ");
		if (returnVal != null){
			returnVal.Unparse(indent);
		}	
		System.out.println();
	}
	void checkTypes() {
		SymbolInfo id;
		id = (SymbolInfo)st.globalLookup("func");
		if ( id != null ){
			id.returned = true;
		}
		if ( id == null ){
			System.out.println(error()+"can only return from a function.");
			typeErrors++;
			return;
		}
		else if ( id.type.val == Types.Void && returnVal != null){
			System.out.println(error()+"can only return a value from a non void function.");
			returnVal.checkTypes();
		}
		else if ( id.type.val != Types.Void && returnVal != null){
			returnVal.checkTypes();
			if ( id.type.val != returnVal.type.val){
				System.out.println(error()+"return value must match the return type "+returnVal.type);
				typeErrors++;
			}
		}
		else {
			/* null return Value */
		}
	}
	private final exprNode returnVal;
} // class returnNode 
/*
class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line, col);
		decls = f;
		stmts = s;
	}

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class blockNode 
*/
class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("break ");
		label.Unparse(indent);
		System.out.println(";");
	}

	private final identNode label;
} // class breakNode 

class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("continue ");
		label.Unparse(indent);
		System.out.println(";");
	}
	private final identNode label;
} // class continueNode 

class argsNode extends ASTNode {
	argsNode() {}
	argsNode(exprNode e, argsNode a, int line, int col) {
		super(line, col);
		argVal = e;
		moreArgs = a;
	}
	public LinkedList<SymbolInfo> params = new LinkedList<SymbolInfo>();
	static nullArgsNode NULL = new nullArgsNode();
	private exprNode argVal;
	private argsNode moreArgs;
} // class argsNode 

class nullArgsNode extends argsNode {
	nullArgsNode() {
		// empty constructor
	}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgsNode 

class strLitNode extends exprNode {
	strLitNode(String stringval, String string_not_print, int line, int col) {
		super(line, col, new Types(Types.String),new Kinds(Kinds.Value));
		//strval = stringval;
		strval = string_not_print;
		length = string_not_print.length()-2;
		//System.out.println(string_not_print);
	}
	void Unparse(int indent) {
		System.out.print(strval);
	}
	void checkTypes(){
		
	}
	void cg() {
		//System.out.println(strval);
		gen("ldc",strval);
		//System.out.println("creme");
	}
	public final String strval;
	public final int length;
} // class strLitNode 

// abstract superclass; only subclasses are actually created
abstract class exprNode extends ASTNode {
	exprNode() {
		super();
	}
	exprNode(int l, int c) {
		super(l, c);
		type = new Types();
		kind = new Kinds();
	}
	exprNode(int l,int c,Types t,Kinds k) {
		super(l,c);
		type = t;
		kind = k;
	} // exprNode
	static nullExprNode NULL = new nullExprNode();
	protected Types type; // Used for typechecking: the type of this node
	protected Kinds kind; // Used for typechecking: the kind of this node
}
class nullExprNode extends exprNode {
	nullExprNode() {super();}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullExprNode 


class andExprNode extends exprNode {
	andExprNode(exprNode and, exprNode t, int line, int col,
		Types resultType){
		super(line, col, resultType, new Kinds(Kinds.Value));
		expressions = and;
		terms = t;
	}
	void Unparse(int indent) {
		expressions.Unparse(indent);
		System.out.print("&&");
		terms.Unparse(indent);
	}
	void checkTypes() {
		expressions.checkTypes();
		terms.checkTypes();
		//System.out.println(expressions.type);
		type = new Types(Types.Boolean);
		typeMustBe(expressions.type.val,Types.Boolean,error()+
			"Left operand of && must be boolean.");
		typeMustBe(terms.type.val,Types.Boolean,error()+
			"Right operand of && must be boolean.");

	}
	void cg(){
		expressions.cg();
		terms.cg();
		gen("iand");
	}
	private final exprNode expressions;
	private final exprNode terms;
}

class orExprNode extends exprNode {
	orExprNode(exprNode and, exprNode t, int line, int col,
		Types resulType){
		super(line, col, resulType, new Kinds(Kinds.Value));
		expressions = and;
		terms = t;
	}
	void Unparse(int indent) {
		expressions.Unparse(indent);
		System.out.print("||");
		terms.Unparse(indent);
	}
	void checkTypes() {
		expressions.checkTypes();
		terms.checkTypes();
		//System.out.println(expressions.type);
		type = new Types(Types.Boolean);
		typeMustBe(expressions.type.val,Types.Boolean,error()+
			"Left operand of || must be boolean.");
		typeMustBe(terms.type.val,Types.Boolean,error()+
			"Right operand of || must be boolean.");

	}
	void cg(){
		expressions.cg();
		terms.cg();
		gen("ior");
	}
	private final exprNode expressions;
	private final exprNode terms;
}

class termNode extends exprNode {
	termNode(){
		super();
	}
	termNode(int line, int col){
		super(line,col);
	}
}

class logicTermNode extends exprNode {
	logicTermNode(exprNode f0, int op, exprNode f1, int line, int col){
		super(line, col,new Types(Types.Boolean), new Kinds(Kinds.Expr));
		leftfactor =f0;
		rightfactor = f1;
		operatorCode = op;
	}
	static void printOp(int op){
		switch (op) {
			case sym.LT:
				System.out.print(" < ");
				break;
			case sym.GT:
				System.out.print(" > ");
				break;
			case sym.GEQ:
				System.out.print(" >= ");
				break;
			case sym.LEQ:
				System.out.print(" <= ");
				break;
			case sym.EQ:
				System.out.print(" == ");
				break;
			case sym.NOTEQ:
				System.out.print(" != ");
				break;
			default:
				Error up = new Error("printOp: case not found");
				throw up; // haha. 

		}
	}
		static String stringOp(int op){
			switch (op) {
				case sym.LT:
					return " < ";
				case sym.GT:
					return " > ";
				case sym.GEQ:
					return " >= ";
				case sym.LEQ:
					return " <= ";
				case sym.EQ:
					return " == ";
				case sym.NOTEQ:
					return " != ";
				default:
					Error up = new Error("printOp: case not found");
					throw up; // haha. 
	
			}
	}
	void Unparse(int indent) {
		leftfactor.Unparse(indent);
		printOp(operatorCode);
		rightfactor.Unparse(indent);
	}
	void checkTypes(){
		leftfactor.checkTypes();
		rightfactor.checkTypes();
		//System.out.println("l:"+leftfactor.type);
		//System.out.println("r:"+rightfactor.type);
		if (leftfactor.type.val == Types.Boolean){ // if one is boolean they both must be.
			typeMustBe(leftfactor.type.val, Types.Boolean, error()+
				"Left operand of "+stringOp(operatorCode));
			typeMustBe(rightfactor.type.val, Types.Boolean, error()+
				"Right operand of "+stringOp(operatorCode));
		}
		else {
			typesMustBe(leftfactor.type.val, Types.Character, Types.Integer, error()+
				"Right operand of"+stringOp(operatorCode)+"must be char or int.");
			typesMustBe(rightfactor.type.val, Types.Character, Types.Integer, error()+
				"Left operand of"+stringOp(operatorCode)+"must be char or int.");
		}

	}
	void cg() {
		String trueLabel, falseLabel;
		leftfactor.cg();
		rightfactor.cg();
		trueLabel = buildlabel(labelCnt++);
		falseLabel = buildlabel(labelCnt++);
		switch(operatorCode){
				case sym.LT:
					gen("if_icmplt",trueLabel);
					break;
				case sym.GT:
					gen("if_icmpgt",trueLabel);
					break;
				case sym.GEQ:
					gen("if_icmpge",trueLabel);
					break;
				case sym.LEQ:
					gen("if_icmple",trueLabel);
					break;
				case sym.EQ:
					gen("if_icmpeq",trueLabel);
					break;
				case sym.NOTEQ:
					gen("if_icmpne",trueLabel);
					break;
				default:
					throw new Error();
	
		}
		gen("ldc",0); //this is is we have a false value.
		gen("goto",falseLabel);
		genlab(trueLabel);
		gen("ldc",1);
		genlab(falseLabel);

	}
	

	private final exprNode leftfactor, rightfactor;
	private final int operatorCode;
}



class binaryOpFacNode extends exprNode {
	binaryOpFacNode(exprNode e1, int op, exprNode e2, int line, int col,
			Types resultType) {
		super(line, col, resultType ,new Kinds(Kinds.Value));
		operatorCode = op;
		leftOperand = e1;
		rightOperand = e2;
	}

	static void printOp(int op) {
		switch (op) {
			case sym.PLUS:
				System.out.print(" + ");
				break;
			case sym.MINUS:
				System.out.print(" - ");
				break;
			default:
				mustBe(false);
				throw new Error("printOp: case not found");
		}
	}
	static String toString(int op) {
		switch (op) {
			case sym.PLUS:
				return(" + ");
			case sym.MINUS:
				return(" - ");
			default:
				mustBe(false);
				return "";
		} // switch(op)
	} // toString
	
	void Unparse(int indent) {
		System.out.print("(");
		leftOperand.Unparse(0);
		printOp(operatorCode);
		rightOperand.Unparse(0);
		System.out.print(")");
	}
	void checkTypes() {
		type = new Types(Types.Integer);
		mustBe(operatorCode== sym.PLUS
		||operatorCode==sym.MINUS);  //Only two bin ops in CSX-lite
		leftOperand.checkTypes();
		rightOperand.checkTypes();
		//System.out.println("("+leftOperand.type+" "+toString(operatorCode)+" "+rightOperand.type+")");
		typesMustBe(leftOperand.type.val, Types.Integer, Types.Character,
			error() + "Left operand of" + toString(operatorCode) 
					+ "must be an int or char.");
		typesMustBe(rightOperand.type.val, Types.Integer, Types.Character,
			error() + "Right operand of" + toString(operatorCode) 
					+ "must be an int or char.");
	}
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
	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
} // class binaryOpNode 


class binaryPriNode extends exprNode {
	binaryPriNode(exprNode p, int op, exprNode u, int line, int col,
		Types resultType){
		super(line, col, resultType ,new Kinds(Kinds.Value));
		leftOperand = p;
		rightOperand = u;
		operatorCode = op;
	}
	static void printOp(int op) {
		switch (op) {
			case sym.TIMES:
				System.out.print(" * ");
				break;
			case sym.SLASH:
				System.out.print(" / ");
				break;
			default:
				throw new Error("printOp: case not found");
		}
	}
	static String toString(int op) {
		switch (op) {
			case sym.TIMES:
				return(" * ");
			case sym.SLASH:
				return(" / ");
			default:
				mustBe(false);
				return "";
		} // switch(op)
	} // toString
	void Unparse(int indent) {
		System.out.print("(");
		leftOperand.Unparse(indent);
		printOp(operatorCode);
		rightOperand.Unparse(indent);
		System.out.print(")");
	}
	void checkTypes() {
		mustBe(operatorCode== sym.TIMES
		||operatorCode==sym.SLASH);  //Only two bin ops in CSX-lite
		leftOperand.checkTypes();
		rightOperand.checkTypes();
		type = new Types(Types.Integer);
		typesMustBe(leftOperand.type.val, Types.Integer, Types.Character,
			error() + "Left operand of" + toString(operatorCode) 
					+ "must be an int.");
		typesMustBe(rightOperand.type.val, Types.Integer, Types.Character,
			error() + "Right operand of" + toString(operatorCode) 
					+ "must be an int.");
	}
		void cg() {
			// First translate the left and right operands
			leftOperand. cg();
			rightOperand. cg();
			// Now the values of the operands are on the stack
	
			if (operatorCode == sym.TIMES)
				gen("imul");
			else if (operatorCode == sym.SLASH)
				gen("idiv");
			else    throw new Error(); // Only * and /.
		};
	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
}



class notUnaryNode extends exprNode {
	notUnaryNode(exprNode un, int line, int col){
		super(line,col);
		unary = un;
	}
	void Unparse(int indent){
		System.out.print(" ! ");
		unary.Unparse(indent);
	}
	void checkTypes(){
		unary.checkTypes();
		//System.out.println("Анна Каренина:"+unary.type);
		type = new Types(Types.Boolean);
		typeMustBe(unary.type.val, Types.Boolean, error()+
			"Type after ! must be boolean");
	}
	void cg() {
		unary.cg();
		gen("ldc",1);	// !x is the same as x xor 1
		gen("ixor");
	}
	private final exprNode unary;
}

class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		operatorCode = op;
	}
	void Unparse(int indent) {
		super.Unparse(indent);
	}
	void checkTypes() {
		operand.checkTypes();
	}
	private final exprNode operand;
	private final int operatorCode; // Token code of the operator
} // class unaryOpNode 

class parenthExprNode extends exprNode {
	parenthExprNode(exprNode e,int line, int col){
		super(line, col);
		 expressions = e;
	}
	void Unparse(int indent) {
		genIndent(indent);
		//System.out.print("(");
		 expressions.Unparse(0);
		//System.out.print(")");
	}
	void checkTypes(){
		 expressions.checkTypes();
		type =  expressions.type;
	}
	void cg() {
		expressions.cg();
	}
	private final exprNode  expressions;
}
class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col) {
		super(line, col, t.type, new Kinds(Kinds.Other));
		operand = e;
		resultType = t;
	}
	void Unparse(int indent) {
		System.out.print(" ( ");
		resultType.Unparse(indent);
		System.out.print(" ) ");
		operand.Unparse(indent);

	}
	void checkTypes(){
		type3MustBe(resultType.type.val, Types.Boolean,Types.Character,Types.Integer, error()+
			"Can only cast to boolean, char, or int.");
		resultType.checkTypes();
		operand.checkTypes();
		type3MustBe(operand.type.val, Types.Boolean,Types.Character,Types.Integer, error()+
			"Can only cast booleans, chars, or ints.");
	}

	private final exprNode operand;
	private final typeNode resultType;
} // class castNode 

class fctCallNode extends stmtNode {
	fctCallNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		methodArgs = a;

	}
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		methodName.Unparse(indent);
		System.out.print(" (");
		methodArgs.Unparse(indent);
		System.out.println(" );");
	}
	void checkTypes() {
		methodArgs.checkTypes();
		for(SymbolInfo arg:  methodArgs.params){
			params.add(arg);
			//System.out.println(arg.toString());
		}
		//methodArgs.params.clear();
		//System.out.println(params.toString());
		SymbolInfo id;
		id = (SymbolInfo)st.globalLookup(methodName.idname);
		//System.out.println("global lookup function: "+id);
		if(id == null){
			System.out.println(error() +"function "+ methodName.idname + " is not declared.");
			typeErrors++;
			methodName.type = new Types(Types.Error);
			return;
		}
		if (id.funcParams != null){						//not a void function
			//System.out.println("foo:"+id.funcParams.size());
		}
		if ( id.type.val != Types.Void ){
			System.out.println(error()+"cannot call a non-void method in a statement.");
			typeErrors++;
			methodName.type = new Types(Types.Error);
			
		}
		methodName.checkTypes();
		checkParams(id.funcParams, params, error()+
		"no function found with that signature.");
	}
	void cg(){
		gen("invokestatic","test/"+methodName.idname+"()V");
	}
	public LinkedList<SymbolInfo> params = new LinkedList<SymbolInfo>();
	private final identNode methodName;
	private final argsNode methodArgs;
} // class fctCallNode 
class fctUnitCallNode extends exprNode { // this one is for unit
	fctUnitCallNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		methodArgs = a;
	}
	void Unparse(int indent) {
		methodName.Unparse(indent);
		System.out.print("(");
		methodArgs.Unparse(indent);
		System.out.print(")");
	}
	void checkTypes() {
		//System.out.println(methodName.idname);
		methodArgs.checkTypes();
		methodName.checkTypes();
		//System.out.println(methodArgs.toString());
		for(SymbolInfo arg:  methodArgs.params){
			params.add(arg);
			//System.out.println("ars:"+arg.toString());
		}
		//System.out.println("poop"+params.size());


		//System.out.println(params.size());
		SymbolInfo id;
		id = (SymbolInfo) st.globalLookup(methodName.idname);
		//type = id.type;
		//System.out.println(type);
		//System.out.println("global lookup function: "+id);
		//System.out.println("Foo:\t"+methodName.type);
		if(id == null){
			System.out.println(error() +"function "+ methodName.idname + " is not declared.");
			typeErrors++;
			methodName.type = new Types(Types.Error);
		}
		else {
			type = id.type; 
		}
		if ( id.type.val == Types.Void ){
			System.out.println(error()+"cannot call a void method in a declaration.");
			typeErrors++;
			methodName.type = new Types(Types.Error);
		}
		checkParams(id.funcParams, params, error()+
		"no function found with that signature.");
	}
	public LinkedList<SymbolInfo> params = new LinkedList<SymbolInfo>();
	private final identNode methodName;
	private final argsNode methodArgs;
} // class fctCallNode 
class actualsNode extends argsNode {
	actualsNode(exprNode e, argsNode a, int line, int col){
		super();
		exprs = e;
		more = a;
	}

	void Unparse(int indent) {
		exprs.Unparse(indent);
		if(!more.isNull()){
			System.out.print(" , ");
			more.Unparse(indent);
		}
	}
	@Override
	void checkTypes() {

		exprs.checkTypes();
		//exprs.Unparse(0);
		//System.out.println("type"+exprs.type+exprs.kind);
		SymbolInfo id = new SymbolInfo("_", exprs.kind, exprs.type); //the string is irelavent.
		params.add(id);
		more.checkTypes();
		//System.out.println("more.params:"+more.params);
		params.addAll(more.params);
		
	}
	static nullArgDeclsNode NULL = new nullArgDeclsNode();
	private exprNode exprs;
	private argsNode more;
}
class identNode extends exprNode {
	identNode(String identname, int line, int col) {
		super(line,col,new Types(Types.Unknown), new Kinds(Kinds.Var));
		idname = identname;
		nullFlag = false;
	} // identNode

	identNode(boolean flag) {
		super(0,0,new Types(Types.Unknown), new Kinds(Kinds.Var));
		idname = "";
		nullFlag = flag;
	} // identNode

	boolean isNull() {return nullFlag;} // Is this node null?

	static identNode NULL = new identNode(true);

	void Unparse(int indent) {
		System.out.print(idname);
	} // Unparse

	void checkTypes() {
		SymbolInfo id;
		//mustBe(kind.val == Kinds.Var); //In CSX-lite all IDs should be vars! 
		id = (SymbolInfo) st.globalLookup(idname);
		//System.out.println("Daniel:"+id);
		if (id == null) {
			System.out.println(error() + idname + " is not declared1.");
			typeErrors++;
			type = new Types(Types.Error);
		} else {
			kind = id.kind;
			type = id.type; 
			idinfo = id; // Save ptr to correct symbol table entry
		} // id != null
		//System.out.println(type);
	} // checkTypes
	void cg(){
		System.out.println(idinfo.type);
		if( idinfo.kind.val == Kinds.Array){
			gen("iaload",idinfo.varIndex);
			return;
		}
		switch(idinfo.type.val){
			case Types.Integer:
			case Types.Boolean:
			case Types.Character:
				//gen("dup");
				gen("iload",idinfo.varIndex);
				break;
			default:
				throw new Error();
		}
	}
	public String idname;
	public SymbolInfo idinfo; // symbol table entry for this ident
	private final boolean nullFlag;
} // class identNode 

class nameSubNode extends exprNode {
	nameSubNode(identNode id, exprNode expr, int line, int col) {
		super(line, col);
		varName = id;
		subscriptVal = expr;
	}

	void Unparse(int indent) {
		varName.Unparse(0); 
		System.out.print("[");
		subscriptVal.Unparse(indent);
		System.out.print("]");
	}
	void checkTypes() {
		SymbolInfo id;
		id = (SymbolInfo)st.globalLookup(varName.idname);
		if ( id == null){
			System.out.println(error()+"array"+varName.idname+ "not declaired.");
			typeErrors++;
		}
		else {
			kind = id.kind;
			type = id.type;
			idinfo = id;
		}
		varName.checkTypes();
		subscriptVal.checkTypes();
		typesMustBe(subscriptVal.type.val, Types.Integer,Types.Character, error()+"arrays can only be indexed by int or char.");// subscriptVal.type.val)
	}
	void cgForAsg(){
		gen("aload",idinfo.varIndex);
		subscriptVal.cg();
	}
	void cg() {
			//gen("dup");
			gen("aload",idinfo.varIndex);
			subscriptVal.cg();
			gen("iaload");

	}
	public String idname;
	public SymbolInfo idinfo; // symbol table entry for this ident
	private final identNode varName;
	private final exprNode subscriptVal;
} // class nameNode 

class nameNode extends exprNode {
	nameNode(int line, int col){
		super(line, col, new Types(Types.Ident),new Kinds(Kinds.Var));
	}
	nameNode(identNode id, int line, int col) {
		super(line, col,id.type,new Kinds(Kinds.Var));
		varName = id;
	}

	void Unparse(int indent) {
		varName.Unparse(0); // Subscripts not allowed in CSX Lite
	}
	void checkTypes() {
		idname = varName.idname;
		varName.checkTypes();
	}
	void cg() {
        // Load value of this variable onto stack using its index
       		gen("iload",varName.idinfo.varIndex);
	};

	public String idname;
	public identNode varName;
}

class intLitNode extends exprNode {
	intLitNode(int val, int line, int col) {
		super(line,col,new Types(Types.Integer),
				new Kinds(Kinds.Value));
		intval = val;
		//System.out.print();
	} // intLitNode
	void Unparse(int indent) {
		genIndent(indent);
		System.out.print(intval);
	}
	void checkTypes() {
		// intLits are typ-correct bc they already satisfied regexp
	}
	void cg() {
        // Load value of this literal onto stack
        	gen("ldc",intval);
	};
	public final int intval;
} // class intLitNode 

class charLitNode extends exprNode {
	charLitNode(String val, int line, int col) {
		super(line, col, new Types(Types.Character), new Kinds(Kinds.Value));
		charval = val;
	}
	void Unparse(int indent) {
		genIndent(indent);
		System.out.print(charval);
	}
	void checkTypes() {

	}
	private final String charval;
} // class charLitNode 

class trueNode extends exprNode {
	trueNode(int line, int col) {
		super(line, col, new Types(Types.Boolean),new Kinds(Kinds.Value));
	}
	void Unparse(int indent) {
		genIndent(indent);
		System.out.print(" true ");
	}
	void checkTypes() {

	}
	void cg() {
        // Load value of this literal onto stack
        	gen("ldc",1);
	};
} // class trueNode 

class falseNode extends exprNode {
	falseNode(int line, int col) {
		super(line, col, new Types(Types.Boolean),new Kinds(Kinds.Value));
	}
	void Unparse(int indent) {
		genIndent(indent);
		System.out.print(" false ");
	}
	void checkTypes() {

	}
	void cg() {
        // Load value of this literal onto stack
        	gen("ldc",0);
	};
} // class falseNode 
