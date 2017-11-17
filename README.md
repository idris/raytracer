# RayTracer #

by Idris Mokhtarzada

I chose to write this RayTracer in Java because I am more comfortable with
Java as a language. Because there are concerns about speed with Java, I added
a multi-threaded mode (-multi) to be used when rendering large, anti-aliased
images.

## Requirements ##

Java JDK 5.0 or higher is required to compile and run this RayTracer.
Java JDK 6.0 is downloadable from the Java website:

<http://java.sun.com/javase/downloads/widget/jdk6.jsp>

Additionally, Apache Maven is required for building the project. Maven comes
with basically any Java IDE available, but you can install it manually from
here:

<https://maven.apache.org/download.cgi>

## Building ##

### Apache Maven ###

Compile and package with Maven:

`mvn clean compile package`

Maven will create a `target` directory with the runnable JAR inside.

### Manual ###

If you want to compile manually, you can simply type the following:

`javac src/main/java/com/github/idris/raytracer/*.java src/main/java/com/github/idris/raytracer/pigments/*.java src/main/java/com/github/idris/raytracer/shapes/*.java`

## Usage ##

Simply run the JAR from the command line:

`java -jar target/raytracer-*.jar`

The wildcard is a convenience to not hardcode the application version into the command
( instead of, for example `java -jar target/raytracer-0.1-SNAPSHOT.jar` )

### Make ###

A Makefile is included with the project, which will compile the Java source.

Use `make` to compile, and `make clean` to remove the compiled .class files.

### Manual build ###

After building, you can simply run the application using java as follows:

`java -cp src/main/java/ com.github.idris.raytracer.Main`

## Command line arguments ##

Run the jar without command line arguments to display a summary of parameters.

The -Djava.awt.headless=true flag alerts java not to popup a Java window.
This is especially useful when running the command on a terminal or in an ssh
connection.

## Implemented Features ##

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

## Test Files ##

Beyond the main test files (test01 - test05), there are some extra tests
included in the `samples` directory to demonstrate extra features:

* wood.txt  -  Looks very nice with "-aa" option!
    (uses texture mapping. hardwood.bmp must be in the working directory)
* test-texmap.txt
    (also uses texture mapping. spectrum.bmp must be in the working directory)

> Note: Samples using textures only work when ran from the project root.
> This will work:

```bash
foo@bar MINGW64 ~/dev/raytracer (master)
λ java -jar target/raytracer-0.1-SNAPSHOT.jar samples/wood.txt out.bmp 640 360 -multi -aa
Finished in: 4202ms
```

> This wont':

```bash
foo@bar MINGW64 ~/dev/raytracer/samples (master)
λ java -jar ../target/raytracer-0.1-SNAPSHOT.jar wood.txt out.bmp 640 360 -multi -aa
ERROR: Could not locate texmap file 'hardwood.bmp'.
```