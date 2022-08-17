package io.github.cpetot.archunit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.cpetot.archunit.jpa.TestEntityWithExplicitPublicEmptyConstructor;
import io.github.cpetot.archunit.jpa.TestEntityWithImplicitPublicEmptyConstructor;
import io.github.cpetot.archunit.jpa.TestEntityWithPublicArgedConstructor;

class JpaCodingRulesTest {

	@Nested
	@DisplayName("when JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR")
	class JpaEntitiesHavePublicEmptyConstructorTest {

		@Test
		void should_raise_no_error_with_explicit_public_empty_constructor() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestEntityWithExplicitPublicEmptyConstructor.class);
			JpaCodingRules.JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_no_error_with_implicit_public_empty_constructor() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestEntityWithImplicitPublicEmptyConstructor.class);
			JpaCodingRules.JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_one_error_with_public_arguments_constructor() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestEntityWithPublicArgedConstructor.class);
			Assertions.assertThatThrownBy(() -> JpaCodingRules.JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("classes that are annotated with @Entity should have a public empty constructor' was violated (1 times)")
				.hasMessageContaining("Class %s has no public empty constructor", TestEntityWithPublicArgedConstructor.class.getName());
		}
	}
}
