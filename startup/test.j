	.class	public  test
	.super	java/lang/Object
	.method	 public static  main([Ljava/lang/String;)V
	invokestatic	test/main()V
	return
	.limit	locals  1
	.limit	stack  20
	.end	method
	.method	 public static  sortup([I)V
	.limit	locals  9
	iconst_m1
	istore	1
	iconst_m1
	istore	2
	iconst_m1
	istore	3
	ldc	8
	istore	2
label0:
	iload	2
	ldc	0
	if_icmpge	label2
	ldc	0
	goto	label3
label2:
	ldc	1
label3:
	ifeq	label1
	ldc	0
	istore	1
label4:
	iload	1
	iload	2
	if_icmple	label6
	ldc	0
	goto	label7
label6:
	ldc	1
label7:
	ifeq	label5
	aload	0
	iload	1
	iaload
	aload	0
	iload	1
	ldc	1
	iadd
	iaload
	if_icmpgt	label8
	ldc	0
	goto	label9
label8:
	ldc	1
label9:
	ifeq	label10
	aload	0
	iload	1
	iaload
	istore	3
	aload	0
	iload	1
	aload	0
	iload	1
	ldc	1
	iadd
	iaload
	iastore
	aload	0
	iload	1
	ldc	1
	iadd
	iload	3
	iastore
	goto	label11
label10:
label11:
	iload	1
	ldc	1
	iadd
	istore	1
	goto	label4
label5:
	iload	2
	ldc	1
	isub
	istore	2
	goto	label0
label1:
	return
	.limit	stack  20
	.end	method
	.method	 public static  sortdown([I)V
	.limit	locals  9
	iconst_m1
	istore	1
	iconst_m1
	istore	2
	iconst_m1
	istore	3
	ldc	8
	istore	2
label12:
	iload	2
	ldc	0
	if_icmpge	label14
	ldc	0
	goto	label15
label14:
	ldc	1
label15:
	ifeq	label13
	ldc	0
	istore	1
label16:
	iload	1
	iload	2
	if_icmple	label18
	ldc	0
	goto	label19
label18:
	ldc	1
label19:
	ifeq	label17
	aload	0
	iload	1
	iaload
	aload	0
	iload	1
	ldc	1
	iadd
	iaload
	if_icmplt	label20
	ldc	0
	goto	label21
label20:
	ldc	1
label21:
	ifeq	label22
	aload	0
	iload	1
	iaload
	istore	3
	aload	0
	iload	1
	aload	0
	iload	1
	ldc	1
	iadd
	iaload
	iastore
	aload	0
	iload	1
	ldc	1
	iadd
	iload	3
	iastore
	goto	label23
label22:
label23:
	iload	1
	ldc	1
	iadd
	istore	1
	goto	label16
label17:
	iload	2
	ldc	1
	isub
	istore	2
	goto	label12
label13:
	return
	.limit	stack  20
	.end	method
	.method	 public static  main()V
	.limit	locals  8
	ldc	"Testing program p39csx
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	0
	ldc	1
	iastore
	aload	0
	ldc	1
	ldc	-10
	iastore
	aload	0
	ldc	2
	ldc	100
	iastore
	aload	0
	ldc	3
	ldc	-1000
	iastore
	aload	0
	ldc	4
	ldc	10000
	iastore
	aload	0
	ldc	5
	ldc	-100000
	iastore
	aload	0
	ldc	6
	ldc	1000000
	iastore
	aload	0
	ldc	7
	ldc	-10000000
	iastore
	aload	0
	ldc	8
	ldc	100000000
	iastore
	aload	0
	ldc	9
	ldc	-1000000000
	iastore
	aload	0
	invokestatic	test/sortup([I)V
	ldc	"Sorted values (ascending order) are:
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	0
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	1
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	2
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	3
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	4
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	5
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	6
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	7
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	8
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	9
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	invokestatic	test/sortdown([I)V
	ldc	"Sorted values (ascending order) are:
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	0
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	1
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	2
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	3
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	4
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	5
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	6
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	7
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	8
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"	"
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	aload	0
	ldc	9
	iaload
	invokestatic	 CSXLib/printInt(I)V
	ldc	"
 "
	invokestatic	 CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  20
	.end	method
