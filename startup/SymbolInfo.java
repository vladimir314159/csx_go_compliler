/**************************************************
*  class used to hold information associated w/
*  Symbs (which are stored in SymbolTables)
*
****************************************************/

import java.util.LinkedList;
class SymbolInfo extends Symb {
 public boolean globel;
 public int  varIndex; // Index used to address a CSX-lite variable
 public Kinds kind; // Should always be Var in CSX-lite
 public Types type; // Should always be Integer or Boolean in CSX-lite
 public int length; // This is only assigned for string.
 public boolean returned; // is a function been returned.
 public LinkedList<SymbolInfo> funcParams;
 public SymbolInfo(String id, Kinds k, Types t, LinkedList<SymbolInfo> ll){
	super(id);
	kind = k;
	type = t; 
	funcParams = ll;
	globel = returned = false;

 }
 public SymbolInfo(String id, Kinds k, Types t, int lenOfString){
	 super(id);
	 kind = k;
	 type = t;
	 length = lenOfString;
	 globel = returned = false;
 };
 public SymbolInfo(String id, Kinds k, Types t){
	super(id);
	//System.out.println(id+":"+t);
	kind = k; type = t;	globel = returned = false;};
 public SymbolInfo(String id, int k, int t){
	super(id);
	kind = new Kinds(k); type = new Types(t);	globel = returned = false;};
 public String toString(){
             return "("+name()+": kind=" + kind+ ", type="+  type+")";};
}

