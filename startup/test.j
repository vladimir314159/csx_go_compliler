	.class	public  test
	.super	java/lang/Object
	ldc	100
	istore	0
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	test/main()V
	return
	.limit	locals  2
	.limit	stack  20
	.end	method
	.method	 public static  foo()V
	.limit	locals  3
	iload	0
	invokestatic	 CSXLib/printInt(I)V
	ldc	"I called foo()
 Woot!!!
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  20
	.end	method
	.method	 public static  main()V
	.limit	locals  3
	iconst_m1
	istore	1
	iconst_m1
	istore	2
	invokestatic	test/foo()V
	ldc	"Testing Program p26csx"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	ldc	"Test completed"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  20
	.end	method
