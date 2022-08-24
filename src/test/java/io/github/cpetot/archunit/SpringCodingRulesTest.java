package io.github.cpetot.archunit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.cpetot.archunit.spring.stereotypes.AStandardClassWithRepository;
import io.github.cpetot.archunit.spring.stereotypes.ATestControllerWithRepository;
import io.github.cpetot.archunit.spring.stereotypes.ATestRestControllerWithRepository;
import io.github.cpetot.archunit.spring.stereotypes.AValidTestRepository;
import io.github.cpetot.archunit.spring.stereotypes.AValidTestService;
import io.github.cpetot.archunit.spring.stereotypes.AnInvalidTestRepositoryContainingRepository;
import io.github.cpetot.archunit.spring.transaction.TestRepository;
import io.github.cpetot.archunit.spring.transaction.TestService;
import io.github.cpetot.archunit.spring.transaction.TestTransactionalService;

class SpringCodingRulesTest {

	@Nested
	@DisplayName("when REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES")
	class RepositoriesAreAccessedOnlyByTransactionalMethodsOrClasses {

		@Test
		void should_raise_no_error_with_class_annotated_transactional() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestRepository.class, TestTransactionalService.class);
			SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_one_error_on_method_not_annotated_with_class_not_annotated_transactional() {
			JavaClasses classes = new ClassFileImporter().importClasses(TestRepository.class, TestService.class);
			Assertions.assertThatThrownBy(() -> SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Rule 'classes that are annotated with @Repository should be accessed by @Transactional classes or methods' was violated (1 times)")
				.hasMessageContaining("Neither Class %s or Method %s.callRepositoryWithoutTransactional() are annotated by @Transactional",
					TestService.class.getName(), TestService.class.getName());
		}
	}

	@Nested
	@DisplayName("when REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES")
	class RepositoriesAreAccessedOnlyByServiceClasses {

		@Test
		void should_raise_no_error_with_service_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, AValidTestService.class);
			SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_one_error_with_controller_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, ATestControllerWithRepository.class);
			Assertions.assertThatThrownBy(() -> SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("classes that are annotated with @Repository should be called by @Service classes' was violated (1 times)")
				.hasMessageContaining("Class %s is not annotated by @Service", ATestControllerWithRepository.class.getName());
		}

		@Test
		void should_raise_one_error_with_no_spring_annotated_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, AStandardClassWithRepository.class);
			Assertions.assertThatThrownBy(() -> SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("classes that are annotated with @Repository should be called by @Service classes' was violated (1 times)")
				.hasMessageContaining("Class %s is not annotated by @Service", AStandardClassWithRepository.class.getName());
		}

		@Test
		void should_raise_one_error_with_repository_class_depending_on_another_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, AnInvalidTestRepositoryContainingRepository.class);
			Assertions.assertThatThrownBy(() -> SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("classes that are annotated with @Repository should be called by @Service classes' was violated (1 times)")
				.hasMessageContaining("Class %s is not annotated by @Service", AnInvalidTestRepositoryContainingRepository.class.getName());
		}
	}

	@Nested
	@DisplayName("when REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES")
	class RepositoriesAreAccessedOnlyByServiceOrControllerClasses {

		@Test
		void should_raise_no_error_with_service_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, AValidTestService.class);
			SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_no_error_with_controller_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, ATestControllerWithRepository.class);
			SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_no_error_with_rest_controller_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, ATestRestControllerWithRepository.class);
			SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES.allowEmptyShould(false).check(classes);
		}

		@Test
		void should_raise_one_error_with_no_spring_annotated_class_depending_on_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, AStandardClassWithRepository.class);
			Assertions.assertThatThrownBy(() -> SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Rule 'classes that are annotated with @Repository should be called by @Service or @Controller classes' was violated (1 times)")
				.hasMessageContaining("Class %s is annotated neither by @Service or @Controller", AStandardClassWithRepository.class.getName());
		}

		@Test
		void should_raise_one_error_with_repository_class_depending_on_another_repository() {
			JavaClasses classes = new ClassFileImporter().importClasses(AValidTestRepository.class, AnInvalidTestRepositoryContainingRepository.class);
			Assertions.assertThatThrownBy(() -> SpringCodingRules.REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES.allowEmptyShould(false).check(classes))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("classes that are annotated with @Repository should be called by @Service or @Controller classes' was violated (1 times)")
				.hasMessageContaining("Class %s is annotated neither by @Service or @Controller", AnInvalidTestRepositoryContainingRepository.class.getName());
		}
	}

}
