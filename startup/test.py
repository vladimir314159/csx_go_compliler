import os
for i in range(0,9):
    os.system("make inputfile file=test-0"+str(i)+".csx_go")
for i in range(10,16):
    os.system("make inputfile file=test-"+str(i)+".csx_go")
for i in range(18,43):
    os.system("make inputfile file=test-"+str(i)+".csx_go")
