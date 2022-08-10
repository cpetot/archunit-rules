package io.github.cpetot.archunit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.cpetot.archunit.date.DateExample;
import io.github.cpetot.archunit.date.LocalDateExample;

class StandardCodingRulesTest {

	@Nested
	@DisplayName("when NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE_OR_CALENDAR")
	class RepositoriesAreAccessedOnlyByTransactionalMethodsOrClasses {

		@Test
		void should_raise_no_error_with_class_using_java_time() {
			JavaClasses classes = new ClassFileImporter().importClasses(LocalDateExample.class);
			StandardCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE.check(classes);
		}

		@Test
		void should_raise_two_error_with_class_containing_date_field() {
			JavaClasses classes = new ClassFileImporter().importClasses(DateExample.class);
			Assertions.assertThatThrownBy(() -> StandardCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE.check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("no classes should use java.util.Date, because Use dates API in java.time instead' was violated (2 times)")
				.hasMessageContaining("calls constructor <java.util.Date.<init>()>")
				.hasMessageContaining("Field <%s.A_DATE> has type <java.util.Date>", DateExample.class.getName());
		}
	}
}
