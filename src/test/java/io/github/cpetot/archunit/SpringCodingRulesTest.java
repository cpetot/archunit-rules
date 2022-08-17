package io.github.cpetot.archunit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
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

}
