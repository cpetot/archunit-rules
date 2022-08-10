package io.github.cpetot.archunit;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.lang.conditions.ArchConditions.dependOnClassesThat;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.Date;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

public final class StandardCodingRules {

	private StandardCodingRules() {
	}

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> USE_JUNIT_4 = useJunit4();

	private static ArchCondition<JavaClass> useJunit4() {
		return dependOnClassesThat(resideInAPackage("org.junit"))
			.as("use JUnit 4");
	}

	@PublicAPI(usage = ACCESS)
	public static final ArchRule NO_CLASSES_SHOULD_USE_JUNIT_4 =
		noClasses()
			.should(USE_JUNIT_4)
			.because("Use JUnit 5 instead");

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> USE_JAVA_UTIL_DATE = useJavaUtilDate();

	private static ArchCondition<JavaClass> useJavaUtilDate() {
		return dependOnClassesThat(type(Date.class))
			.as("use java.util.Date");
	}

	public static final ArchRule NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE =
		noClasses()
			.should(USE_JAVA_UTIL_DATE)
			.because("Use dates API in java.time instead");

	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> HAS_A_PUBLIC_EMPTY_CONSTRUCTOR = hasAPublicEmptyConstructor();

	private static ArchCondition<JavaClass> hasAPublicEmptyConstructor() {
		return new ArchCondition<>("have a public empty constructor") {
			@Override
			public void check(JavaClass javaClass, ConditionEvents events) {
				boolean hasPublicEmptyConstructor = javaClass.tryGetConstructor() // Zero arg constructor
					.filter(constructor -> constructor.getModifiers().contains(JavaModifier.PUBLIC))
					.isPresent();
				if (!hasPublicEmptyConstructor) {
					events.add(SimpleConditionEvent.violated(
						javaClass, String.format("Class %s has no public empty constructor", javaClass.getFullName()))
					);
				}
			}
		};
	}

}
