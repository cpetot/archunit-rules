package io.github.cpetot.archunit;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public final class SpringCodingRules {

	private SpringCodingRules() {
	}

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> BE_ACCESSED_BY_TRANSACTIONAL_METHODS = beAccessedByTransactionalMethods();

	private static ArchCondition<JavaClass> beAccessedByTransactionalMethods() {
		return new ArchCondition<>("be accessed by @Transactional methods") {
			@Override
			public void check(JavaClass javaClass, ConditionEvents events) {
				javaClass.getMethodCallsToSelf().stream()
					.filter(methodCall -> !methodCall.getOrigin().isAnnotatedWith(Transactional.class))
					.map(methodCall -> SimpleConditionEvent.violated(
						methodCall, String.format("Method %s is not @Transactional", methodCall.getOrigin().getFullName()))
					)
					.forEach(events::add);
			}
		};
	}

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> BE_ACCESSED_BY_TRANSACTIONAL_CLASSES = beAccessedByTransactionalClasses();

	private static ArchCondition<JavaClass> beAccessedByTransactionalClasses() {
		return new ArchCondition<>("be accessed by @Transactional classes") {
			@Override
			public void check(JavaClass javaClass, ConditionEvents events) {
				javaClass.getMethodCallsFromSelf().stream()
					.filter(methodCall -> !methodCall.getOriginOwner().isAnnotatedWith(Transactional.class))
					.map(methodCall -> SimpleConditionEvent.violated(
						methodCall, String.format("Class %s is not @Transactional", methodCall.getOriginOwner().getFullName()))
					)
					.forEach(events::add);
			}
		};
	}

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> BE_ACCESSED_BY_TRANSACTIONAL_CLASSES_OR_METHODS =
		BE_ACCESSED_BY_TRANSACTIONAL_CLASSES.or(BE_ACCESSED_BY_TRANSACTIONAL_METHODS);

	/**
	 * Attention, pour les méthodes héritées de Spring Data sur les repositories, dans la version actuelle,
	 * ArchUnit n'est pas capable de faire cette vérification.
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES =
		classes().that().areAnnotatedWith(Repository.class)
			.should(BE_ACCESSED_BY_TRANSACTIONAL_CLASSES_OR_METHODS);

}
