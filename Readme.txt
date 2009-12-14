RayTracer
by Idris Mokhtarzada
------------------------------
I chose to write this RayTracer in Java because I am more comfortable with 
Java as a language. Because there are concerns about speed with Java, I added 
a multi-threaded mode (-multi) to be used when rendering large, anti-aliased 
images.

Requirements
============
Java JDK 5.0 or higher is required to compile and run this RayTracer.
You will need the command line tools 'javac' and 'java'.
Java JDK 6.0 is downloadable from the Java website:
http://java.sun.com/javase/downloads/widget/jdk6.jsp


Building
========
I have included a Makefile which will compile the java source. 
Use 'make' to compile, and 'make clean' to remove the compiled .class files. 

If you do not have make (for example, on Windows), you can simply type the following:
javac src/raytracer/*.java src/raytracer/pigments/*.java src/raytracer/shapes/*.java


Usage
=====
After making, you can simply run the application using java as follows:
java -Djava.awt.headless=true -cp src raytracer.Main test05.txt test05.bmp 400 300

The -Djava.awt.headless=true flag alerts java not to popup a Java window. 
This is especially useful when running the command on a terminal or in an ssh 
connection.


Implemented Features
====================
Basic Features:
* shapes: spheres, planes
* pigments: solid, checker
* shadows
* reflection
* refraction

Extra Features:
* pigments: texmap
* anti-aliasing
    To enable anti-aliasing, simply add "-aa" as a command line parameter
* multi-threading
    To enable multi-threading, simply add "-multi" as a command line parameter
    Note: Multi-threading is only optimal when the render time is very high.
          For example, on my computer, when running test05.txt with 
          anti-aliasing enabled, and a canvas size of 800x600, multi-threading 
          saves about 6 seconds.
          Also keep in mind, when using multi-threaded mode, there is no 
          incremental output. It only outputs the time it took to render.


Test Files
==========
Beyond the main test files (test01 - test05), there are some extra tests 
included to demonstrate extra features:
* wood.txt  -  Looks very nice with "-aa" option!
    (uses texture mapping. hardwood.bmp must be in the working directory)
* test-texmap.txt
    (also uses texture mapping. spectrum.bmp must be in the working directory)
