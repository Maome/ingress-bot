all: Pygress.java
	javac -cp lib/jars/\* Pygress.java *.java -Xlint:unchecked -d ./bin/

run:
	java -cp ./bin:lib/jars/\* Pygress
