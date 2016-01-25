##Tiny Renderer in Java
This project is an attempt to write a 3d model render basing on the series of articles located at [habrahabr.ru][1]
(Caution: Russian).

C++ version is [here][2].

###Build Requirements
* Java 8
* Maven 3.2

###Runtime Requirements
  * Java 8

###Download from Github
```
git clone https://github.com/dimaopen/tinyrenderer-java.git
cd tinyrenderer-java
```

###Build
`mvn clean package`

###Execute
`java -jar tinyrenderer-1.0.0-SNAPSHOT.jar path/to/your/model.obj`

You can render a sample model with running
```
mvn exec:java
```
The result picture is located at `working_dir/result.png`


[1]: http://habrahabr.ru/post/248153/ "Brief course of computer graphic"
[2]: https://github.com/ssloy/tinyrenderer "c++ version"

