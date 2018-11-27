class Kinds{
 public static final int Var = 0;
 public static final int Value = 1;
 public static final int Func = 3;
 public static final int Const = 4;
 public static final int Other = 2;
 public static final int Expr = 5;
 public static final int Array = 6;
 public static final int ArrayParam = 7;
 public static final int ScalarParam = 8;

 Kinds(int i){val = i;}
 Kinds(int i, int l){
   val = i;
   length = l;}
 Kinds(){val = Other;}

 public String toString() {
        switch(val){
          case 0: return "Var";
          case 1: return "Value";
          case 2: return "Other";
          case 3: return "Func";
          case 4: return "Const";
          case 5: return "Expr";
          case 6: return "Array";
          case 7: return "ArrayParam";
          case 8: return "ScalarParam";
          default: throw new RuntimeException();
        }
 }

 int val;
 int length;
}
