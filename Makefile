JAVAC = javac
JFLAGS = 

all:
	$(JAVAC) $(JFLAGS) src/raytracer/*.java src/raytracer/pigments/*.java src/raytracer/shapes/*.java

clean:
	$(RM) src/raytracer/*.class src/raytracer/pigments/*.class src/raytracer/shapes/*.class