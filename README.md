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

## Installation and Set-up (Docker):
Clone the project (duh), and get a terminal open, navigate into the directory where you extracted project files. Run:

```shell
docker-compose up --build
```
This will build the container locally. It should start automatically. Since Docker doesn't have pseudoTTY support until specificed, use:

```shell
docker attach <container_id>
```
 run `docker ps` to find out your containerID. This will attach the Java runner to your terminal and allow for input. 

> [!NOTE]
> The Java app will fail to run at first, as the Java container starts BEFORE the MySQL container. To fix this, simply restart the Java image and re-attach the container to terminal.
