# WorkShare

-----------------RUNS ONLY ON WINDOWS--------------------

REQUIREMENTS:-

-> Apache Tomcat , RECOMMENDED VERSION: 10 or above

-> JDK (Java Development Kit) , RECOMMENDED VERSION: 8.x or the version which supports your Apache Tomcat version
   
----------------------------------------------------------

HOW TO SETUP:-

-> Install JDK (Java Development Kit) from https://www.oracle.com/java/technologies/downloads/

-> Now set an enviroment variable called JAVA_HOME to your JDK Directory, if it already exists and pointed to your JRE Directory change it to your JDK Directory and if it is already pointing to a JDK Installation ignore this step. This step is important because WorkShare uses some executables which are available in JDK and not in JRE.

-> Setup Apache Tomcat on your server , documentaion for help https://tomcat.apache.org/tomcat-10.0-doc/index.html.

-> Place the folder 'java' in webapps folder of your Tomcat.

---------------------------------------------------------

HOW TO SEND CODE:-

-> You can either send a GET request or a POST request to the servlet. 

-> In the GET Request, the server will send first url query parameter as response. 

-> In the POST request, the client needs to send Java code in the form of plain text format and will send a JSON document as response. THe JSON document has two objects 1) compile log and 2) output. The compile log object will have any output given by the Java compiler, this may include errors, warnings etc:- . The output object will have the output of the code sent.

---------------------------------------------------------

HOW TO WRITE THE CODE:-

-> It should have the main method

-> The class that conatins main method should be named 'Work'

-> The code shoudln't depend on any classes that aren't available by default.

-> If output is required then the code should contain the line 'System.setOut(new PrintStream(\"out.txt\"));'. This line will flush the output of your code to a file called out.txt. After the execution of your code is over, the content of out.txt will be sent back as response.

-------------------------------------------------------

SERVER SECURITY:-

-> Inside the core folder you can find the file permission.policy. Thid file makes sure the code sent to server dosen't have unrestricted access to the server. By default this file only gives 2 permissions to any  Java code sent to the server, 1) Permission to set Input or Output 2) Permission to write to the file out.txt.

-> You can add more permissions if the code wants access to some classes, but be wary because giving some crucial permissions will give the code ability to delete important system files like 'System32' which permanently wreck your Operating System or steal all your data, so give permissions very carefully.
