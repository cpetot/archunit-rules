[![CI](https://github.com/cpetot/archunit-rules/actions/workflows/build.yml/badge.svg)](https://github.com/cpetot/archunit-rules/actions/workflows/maven.yml?query=branch%3Amain++)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.cpetot/archunit-rules/badge.svg)](https://search.maven.org/search?q=g:io.github.cpetot%20AND%20a:archunit-rules)
[![javadoc](https://javadoc.io/badge2/io.github.cpetot/archunit-rules/javadoc.svg)](https://javadoc.io/doc/io.github.cpetot/archunit-rules)
[![License](https://img.shields.io/badge/licence-Apache%202-blue)](https://github.com/cpetot/archunit-rules/blob/main/LICENSE)

# ArchUnit rules

Add rules on [ArchUnit](https://github.com/TNG/ArchUnit)

## Requirements

### JDK
A JDK 17 or more is required.

## Usage

Check the latest available version on [Maven central](https://search.maven.org/search?q=g:io.github.cpetot%20AND%20a:archunit-rules)

###### Gradle

```
testImplementation 'io.github.cpetot:archunit-rules:YOUR_VERSION'
```

###### Maven

```xml

<dependency>
	<groupId>io.github.cpetot</groupId>
	<artifactId>archunit-rules</artifactId>
	<version>YOUR_VERSION</version>
	<scope>test</scope>
</dependency>
```

#### Create a test

```java
//(...)
import static io.github.cpetot.archunit.StandardCodingRules.NO_CLASSES_SHOULD_USE_JUNIT_4;
import static io.github.cpetot.archunit.StandardCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE;
import static io.github.cpetot.archunit.JpaCodingRules.JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR;
import static io.github.cpetot.archunit.SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES;
import static io.github.cpetot.archunit.SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES;
import static io.github.cpetot.archunit.SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES;
import static io.github.cpetot.archunit.SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES;

@AnalyzeClasses(packages = "org.example")
class MyCodingRulesTest {

	// Standard rules
	@ArchTest
	private ArchRule no_classes_should_use_junit_4 = NO_CLASSES_SHOULD_USE_JUNIT_4;

	@ArchTest
	private ArchRule no_classes_should_use_java_util_date = NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE;

	// JPA rules
	@ArchTest
	private ArchRule jpa_entities_must_declare_a_public_empty_constructor = JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTORS;

	// Spring rules
	@ArchTest
	private ArchRule repositories_are_accessed_by_transactional_classes_or_methods = REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES;

	@ArchTest
	private ArchRule repositories_are_accessed_only_by_service_classes = REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES;

	@ArchTest
	private ArchRule repositories_are_accessed_only_by_service_or_controller_classes = REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES;
}
```

#### Let the API guide you

* [StandardCodingRules](https://javadoc.io/doc/io.github.cpetot/archunit-rules/latest/io/github/cpetot/archunit/StandardCodingRules.html)
* [SpringCodingRules](https://javadoc.io/doc/io.github.cpetot/archunit-rules/latest/io/github/cpetot/archunit/SpringCodingRules.html)
* [JpaCodingRules](https://javadoc.io/doc/io.github.cpetot/archunit-rules/latest/io/github/cpetot/archunit/JpaCodingRules.html)
