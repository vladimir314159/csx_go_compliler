	.class	public  test
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	test/main()V
	return
	.limit	locals  1
	.limit	stack  20
	.end	method
	.method	 public static  sum(II)I
	.limit	locals  22
	iload	0
	iload	1
	iadd
	ireturn
	ireturn
	.limit	stack  20
	.end	method
	.method	 public static  plus1(I)I
	.limit	locals  21
	ldc	1
	iload	0
	invokestatic	test/sum(II)I
	ireturn
	ireturn
	.limit	stack  20
	.end	method
	.method	 public static  main()V
	.limit	locals  20
	ldc	"Testing Program p42csx"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	ldc	"100+1 = "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	ldc	100
	invokestatic	test/plus1(I)I
	invokestatic	 CSXLib/printInt(I)V
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
