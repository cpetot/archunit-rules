package io.github.cpetot.archunit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.cpetot.archunit.date.DateExample;
import io.github.cpetot.archunit.date.LocalDateExample;
import io.github.cpetot.archunit.junit4.JUnit4Test;

class StandardCodingRulesTest {

	@Nested
	@DisplayName("when NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE")
	class NoClassShouldUseDateTest {

		@Test
		void should_raise_no_error_with_class_using_java_time() {
			JavaClasses classes = new ClassFileImporter().importClasses(LocalDateExample.class);
			StandardCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_two_error_with_class_containing_date_field() {
			JavaClasses classes = new ClassFileImporter().importClasses(DateExample.class);
			Assertions.assertThatThrownBy(() -> StandardCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("no classes should use java.util.Date, because Use dates API in java.time instead' was violated (2 times)")
				.hasMessageContaining("calls constructor <java.util.Date.<init>()>")
				.hasMessageContaining("Field <%s.A_DATE> has type <java.util.Date>", DateExample.class.getName());
		}
	}

	@Nested
	@DisplayName("when NO_CLASSES_SHOULD_USE_JUNIT4")
	class NoClassShouldUseJunit4Test {

		@Test
		void should_raise_no_error_with_class_using_junit5() {
			JavaClasses classes = new ClassFileImporter().importClasses(StandardCodingRulesTest.class);
			StandardCodingRules.NO_CLASSES_SHOULD_USE_JUNIT_4.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_5_errors_with_class_importing_junit_4_dependencies() {
			JavaClasses classes = new ClassFileImporter().importClasses(JUnit4Test.class);
			Assertions.assertThatThrownBy(() -> StandardCodingRules.NO_CLASSES_SHOULD_USE_JUNIT_4.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("no classes should use JUnit 4, because Use JUnit 5 instead")
				.hasMessageContaining("is annotated with <org.junit.After>")
				.hasMessageContaining("is annotated with <org.junit.AfterClass>")
				.hasMessageContaining("is annotated with <org.junit.Before>")
				.hasMessageContaining("is annotated with <org.junit.BeforeClass>")
				.hasMessageContaining("is annotated with <org.junit.Test>");
		}
	}
}
