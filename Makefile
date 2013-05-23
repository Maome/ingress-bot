all: Pygress.java
	javac -cp lib/jars/\* Pygress.java *.java -Xlint:unchecked

run:
	java -cp .:lib/jars/\* Pygress
