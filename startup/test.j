	.class	public  test
	.super	java/lang/Object
	.field static	globe  I
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	test/main()V
	return
	.limit	locals  1
	.limit	stack  20
	.end	method
	.method	 public static  main()V
	.limit	locals  2
	ldc	9
	putstatic	test/globe  I
	getstatic	test/globe  I
	invokestatic	 CSXLib/printInt(I)V
	return
	.limit	stack  20
	.end	method
