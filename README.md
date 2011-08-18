<a name="README">[Robolectric](http://pivotal.github.com/robolectric/index.html)</a>
=======

**An Android Testing Framework**

Robolectric can be built using either Maven or Ant. Both Eclipse (with the M2Eclipse plug-in) and
IntelliJ can import the pom.xml file and will automatically generate their project files from it.

For more information about how to use Robolectric on your project, extend its functionality, and join the community of
contributors, please see: [http://pivotal.github.com/robolectric/index.html](http://pivotal.github.com/robolectric/index.html)

### Running Tests - Default

`mvn clean test`

or

`ant clean test`

### Running Tests - Real Android Jars
We are going to assume you know what you're doing here, so here are the power user instructions.

- download android source for 2.3.3
- build
- get out/target/common/obj/JAVA_LIBRARIES/framework_intermediates/classes.jar and put it in ~/android-real-jars/gingerbread-233/classes.jar
- Look at RobolectricTestRunner.USE_REAL_ANDROID_SOURCES and make sure it is set to true.
- Look at RobolectricTestRunner.getDefaultLoader() and make sure the paths point to the real jars you copied above.

`ant clean test.real`