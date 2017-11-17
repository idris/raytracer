JAVAC = javac
JFLAGS = 

ROOT_PACKAGE = 'src/main/java/com/github/idris/raytracer'

all:
	$(JAVAC) $(JFLAGS) $(ROOT_PACKAGE)/*.java $(ROOT_PACKAGE)/pigments/*.java $(ROOT_PACKAGE)/shapes/*.java

clean:
	$(RM) $(ROOT_PACKAGE)/*.class $(ROOT_PACKAGE)/*.class $(ROOT_PACKAGE)/*.class