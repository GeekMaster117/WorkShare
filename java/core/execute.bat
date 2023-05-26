@echo off
cd ..
cd temp
cd %1
java -classpath "%SERVER_HOME%\temp\%1" -Djava.security.manager=allow -Djava.security.manager -Djava.security.policy=%SERVER_HOME%\core\permission.policy Work
cd ..
cd ..
cd core