package io.github.cpetot.archunit;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.lang.conditions.ArchConditions.dependOnClassesThat;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * StandardCodingRules provides a set of general {@link ArchCondition ArchConditions}
 * and {@link ArchRule ArchRules} for coding that might be useful in various projects
 * but are not present in {@link com.tngtech.archunit.library.GeneralCodingRules GeneralCodingRules}.
 */
public final class StandardCodingRules {

	private StandardCodingRules() {
	}

	/**
	 * A condition that checks that the given class use JUnit 4.
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> USE_JUNIT_4 = useJunit4();

	private static ArchCondition<JavaClass> useJunit4() {
		return dependOnClassesThat(resideInAPackage("org.junit"))
			.as("use JUnit 4");
	}

	/**
	 * A rule that checks that the given class does not use JUnit 4.
	 *
	 * <p>
	 * Junit 5 is recommanded, but since some test annotations are similar, it may be easy to use the old version.
	 * </p>
	 *
	 * @see StandardCodingRules#USE_JUNIT_4
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule NO_CLASSES_SHOULD_USE_JUNIT_4 =
		noClasses()
			.should(USE_JUNIT_4)
			.because("Use JUnit 5 instead");

	/**
	 * A condition that checks if the given class use {@link Date}
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchCondition<JavaClass> USE_JAVA_UTIL_DATE = useJavaUtilDate();

	private static ArchCondition<JavaClass> useJavaUtilDate() {
		return dependOnClassesThat(type(Date.class))
			.as("use java.util.Date");
	}

	/**
	 * A rule that checks that all the given classes do not use {@link Date}
	 *
	 * <p>
	 * Since Java 8, all classes in {@code java.time.*} are recommended to deal with date and time.
	 * </p>
	 *
	 * @see StandardCodingRules#USE_JAVA_UTIL_DATE
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule NO_CLASSES_SHOULD_USE_JAVA_UTIL_DATE =
		noClasses()
			.should(USE_JAVA_UTIL_DATE)
			.because("Use dates API in java.time instead");

	/**
	 * A condition that checks if the Java class has a public no-args constructor
	 * <div>
	 * Matching examples :
	 * <pre>{@code
	 * public class MyClass {
	 *   // Fields
	 *   public MyClass() {}
	 * }
	 *
	 * public class MyClass {
	 *   // Implicit public constructor
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Not marching examples :
	 * <pre>{@code
	 * public class MyClass {
	 *   // Fields
	 *   public MyClass(String description) {
	 *     this.description = description
	 *   }
	 * }
	 *
	 * public class MyClass {
	 *   // Fields
	 *   private MyClass() {}
	 * }
	 * }</pre>
	 * </div>
	 */
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

	/**
	 * A condition that checks if the Java class is only accessed by classes meta annotated by a given annotation.
	 * <div>
	 * Matching examples if you're looking for mandatory &#64;YourAnnotation for the classes calling ATargetClass:
	 * <pre>{@code
	 * public class ATargetClass {
	 * }
	 *
	 * &#64;YourAnnotation
	 * public class ACallingClass {
	 * 	private final ATargetClass aTargetClass;
	 *
	 * 	public ACallingClass(ATargetClass aTargetClass) {
	 * 		this.aTargetClass = aTargetClass;
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Not marching examples if you're looking for mandatory &#64;YourAnnotation for the classes calling ATargetClass:
	 * <pre>{@code
	 * public class ATargetClass {
	 * }
	 *
	 * &#64;AnotherAnnotation
	 * public class ACallingClass {
	 * 	private final ATargetClass aTargetClass;
	 *
	 * 	public ACallingClass(ATargetClass aTargetClass) {
	 * 		this.aTargetClass = aTargetClass;
	 * 	}
	 * }
	 *
	 * // No annotation
	 * public class ACallingClass {
	 * 	private final ATargetClass aTargetClass;
	 *
	 * 	public ACallingClass(ATargetClass aTargetClass) {
	 * 		this.aTargetClass = aTargetClass;
	 * 	}
	 * }
	 * }</pre>
	 * </div>
	 *
	 * @param annotationClass the mandatory annotation class.
	 * @return the condition.
	 */
	@PublicAPI(usage = ACCESS)
	public static ArchCondition<JavaClass> beAccessedOnlyByClassesMetaAnnotatedBy(Class<? extends Annotation> annotationClass) {
		return new ArchCondition<>("be called by @%s classes", annotationClass.getSimpleName()) {
			@Override
			public void check(JavaClass javaClass, ConditionEvents events) {
				javaClass.getDirectDependenciesToSelf().stream()
					.map(Dependency::getOriginClass)
					.distinct()
					.filter(originClass -> !originClass.isMetaAnnotatedWith(annotationClass))
					.map(originClass -> SimpleConditionEvent.violated(originClass, String.format(
							"Class %s is not annotated by @%s",
							originClass.getFullName(),
							annotationClass.getSimpleName()
						))
					)
					.forEach(events::add);
			}
		};
	}

	/**
	 * Similar to {@link #beAccessedOnlyByClassesMetaAnnotatedBy(Class)}, but target <em>any</em> of the given annotations.
	 *
	 * @param annotationsClasses the annotation classes.
	 * @return the condition.
	 */
	@PublicAPI(usage = ACCESS)
	public static ArchCondition<JavaClass> beAccessedOnlyByClassesMetaAnnotatedByAny(Class<? extends Annotation>... annotationsClasses) {
		String annotationsDescription = Arrays.stream(annotationsClasses)
			.map(annotationClass -> "@" + annotationClass.getSimpleName())
			.collect(Collectors.joining(" or "));
		return new ArchCondition<>("be called by %s classes", annotationsDescription) {
			@Override
			public void check(JavaClass javaClass, ConditionEvents events) {
				javaClass.getDirectDependenciesToSelf().stream()
					.map(Dependency::getOriginClass)
					.distinct()
					.filter(originClass -> Arrays.stream(annotationsClasses).noneMatch(originClass::isMetaAnnotatedWith))
					.map(originClass -> SimpleConditionEvent.violated(originClass, String.format(
							"Class %s is annotated neither by %s",
							originClass.getFullName(),
							annotationsDescription
						))
					)
					.forEach(events::add);
			}
		};
	}
}
