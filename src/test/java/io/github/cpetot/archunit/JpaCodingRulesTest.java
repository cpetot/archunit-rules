package io.github.cpetot.archunit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.cpetot.archunit.jpa.TestEntityIncorrectLifeCycleArguments;
import io.github.cpetot.archunit.jpa.TestEntityIncorrectLifeCycleReturnType;
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

	@Nested
	@DisplayName("when LIFE_CYCLE_ANNOTATIONS_CORRECTLY_DECLARED")
	class LifeCycleAnnotationsCorrectlyDeclaredTest {

		@Test
		void should_raise_no_error_with_void_without_parameter() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestEntityWithExplicitPublicEmptyConstructor.class);
			JpaCodingRules.LIFE_CYCLE_ANNOTATIONS_CORRECTLY_DECLARED.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_one_error_with_return_type_instead_of_void() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestEntityIncorrectLifeCycleReturnType.class);
			Assertions.assertThatThrownBy(() -> JpaCodingRules.LIFE_CYCLE_ANNOTATIONS_CORRECTLY_DECLARED.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("methods that are annotated with PrePersist or PreUpdate or PostPersist or PostUpdate should be a void without any parameter' was violated (1 times)")
				.hasMessageContaining("Method %s.prePersist() is not a void but returns a java.lang.String", TestEntityIncorrectLifeCycleReturnType.class.getName());
		}

		@Test
		void should_raise_one_error_with_arguments_for_method() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestEntityIncorrectLifeCycleArguments.class);
			Assertions.assertThatThrownBy(() -> JpaCodingRules.LIFE_CYCLE_ANNOTATIONS_CORRECTLY_DECLARED.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("methods that are annotated with PrePersist or PreUpdate or PostPersist or PostUpdate should be a void without any parameter' was violated (1 times)")
				.hasMessageContaining("Method %s.prePersist(int) should be without any parameter but has 1 parameter(s)", TestEntityIncorrectLifeCycleArguments.class.getName());
		}
	}
}
