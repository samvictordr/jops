# jops
Java application containerization demonstration with JDBC/MySQL containers.

## Installation and Set-up instructions(Local Setup):
clone the project, get it set up on VSCode or Eclipse (choose your poison, go wild) and compile the package with:

```shell
javac -cp ".:lib/mysql-connector-j-9.1.0.jar" -d bin src/com/sece/*.java
```

then run with:

```shell
java -cp ".:lib/mysql-connector-j-9.1.0.jar:bin" com.sece.Main
```
