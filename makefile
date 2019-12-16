JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Building.java \
        Constants.java \
        MinHeap.java \
        RedBlackTree.java \
	risingCity.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
