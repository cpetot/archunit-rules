[![CI](https://github.com/cpetot/archunit-rules/actions/workflows/build.yml/badge.svg)](https://github.com/cpetot/archunit-rules/actions/workflows/maven.yml?query=branch%3Amain++)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.cpetot/archunit-rules/badge.svg)](https://search.maven.org/search?q=g:io.github.cpetot%20AND%20a:archunit-rules)
[![License](https://img.shields.io/badge/licence-Apache%202-blue)](https://github.com/cpetot/archunit-rules/blob/main/LICENSE)
# ArchUnit rules

Add rules on [ArchUnit](https://github.com/TNG/ArchUnit)

## Usage

###### Gradle

```
testImplementation 'io.github.cpetot.archunit:archunit-rules:0.1-SNAPSHOT'
```

###### Maven

```
<dependency>
    <groupId>io.github.cpetot</groupId>
    <artifactId>archunit-rules</artifactId>
    <version>0.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

#### Create a test

```java
i(...)
import static io.github.cpetot.archunit.StandardCodingRules.NO_CLASSES_SHOULD_USE_JUNIT_4;
import static io.github.cpetot.archunit.StandardCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE;
import static io.github.cpetot.archunit.JpaCodingRules#JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR;

@AnalyzeClasses(packages = "org.example")
class MyCodingRulesTest {

    @ArchTest
    private ArchRule no_classes_should_use_junit_4 = NO_CLASSES_SHOULD_USE_JUNIT_4;

    @ArchTest
    private ArchRule no_classes_should_use_java_util_date = NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE;

    @ArchTest
    private ArchRule jpa_entities_must_declare_a_public_empty_constructor = NO_CLASJPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTORSES_SHOULD_USE_JAVA_UTIL_DATE;
}
