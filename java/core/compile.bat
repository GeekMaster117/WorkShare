@echo off
cd ..
cd temp
cd %1
javac Work.java 2> %SERVER_HOME%\temp\%1\compile.txt
cd ..
cd ..
cd core