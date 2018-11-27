class Types{
 public static final int Character = 9;
 public static final int Integer = 1;
 public static final int Boolean = 2;
 public static final int Error = 3;
 public static final int Unknown = 4;
 public static final int Void = 5;
 public static final int String = 6;
 public static final int Ident = 8;

 Types(int i){val = i;}
 Types(){val = Unknown;}

 public String toString() {
	switch(val){
	  case 9: return "Character";
	  case 1: return "Integer";
	  case 2: return "Boolean";
	  case 3: return "Error";
	  case 4: return "Unknown";
	  case 5: return "Void";
	  case 6: return "String";
	  case 8: return "Identifier";
	  default: throw new RuntimeException();
	}
 }

 int val;
}

