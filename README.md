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
```
java -jar tinyrenderer-1.0.0-SNAPSHOT.jar path/to/your/model.obj
```
As a single argument tt takes a path to [Wavefront .obj file][3]. The model texture should be at the same directory
named your_model_name_diffuse.tga. You can download some models from
[https://github.com/ssloy/tinyrenderer/tree/master/obj][4]

To render a sample model just execute
```
mvn exec:java
```
The result picture is located at `working_dir/result.png`


[1]: http://habrahabr.ru/post/248153/ "Brief course of computer graphic"
[2]: https://github.com/ssloy/tinyrenderer "C++ version"
[3]  https://en.wikipedia.org/wiki/Wavefront_.obj_file "Wavefront .obj file"
[4]: https://github.com/ssloy/tinyrenderer/tree/master/obj "Models"

