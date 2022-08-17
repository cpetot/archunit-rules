package io.github.cpetot.archunit;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import java.util.stream.Stream;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * SpringCodingRulesTest provides a set of general {@link ArchCondition ArchConditions}
 * and {@link ArchRule ArchRules} for coding that might be useful in the projects using Spring.
 */
public final class SpringCodingRules {

	private SpringCodingRules() {
	}

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> BE_ACCESSED_BY_TRANSACTIONAL_CLASSES_OR_METHODS = beAccessedByTransactionalClassesOrMethods();

	private static ArchCondition<JavaClass> beAccessedByTransactionalClassesOrMethods() {
		return new ArchCondition<>("be accessed by @Transactional classes or methods") {
			@Override
			public void check(JavaClass javaClass, ConditionEvents events) {
				Stream.concat(
						javaClass.getMethodCallsFromSelf().stream(),
						javaClass.getMethodCallsToSelf().stream()
					)
					.map(methodCall -> {
						JavaClass originClass = methodCall.getOriginOwner();
						JavaCodeUnit originMethod = methodCall.getOrigin();
						if (originMethod.isAnnotatedWith(Transactional.class)) {
							return SimpleConditionEvent.satisfied(
								methodCall, String.format("Method %s is @Transactional", originMethod.getFullName())
							);
						} else if (originClass.isAnnotatedWith(Transactional.class)) {
							return SimpleConditionEvent.satisfied(
								methodCall, String.format("Class %s is @Transactional", originClass.getFullName())
							);
						} else {
							return SimpleConditionEvent.violated(
								methodCall, String.format("Neither Class %s or Method %s are annotated by @Transactional", originClass.getFullName(), originMethod.getFullName()));
						}
					})
					.forEach(events::add);
			}
		};
	}

	/**
	 * Attention, pour les méthodes héritées de Spring Data sur les repositories, dans la version actuelle,
	 * ArchUnit n'est pas capable de faire cette vérification.
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES =
		classes().that().areAnnotatedWith(Repository.class)
			.should(BE_ACCESSED_BY_TRANSACTIONAL_CLASSES_OR_METHODS);

}
