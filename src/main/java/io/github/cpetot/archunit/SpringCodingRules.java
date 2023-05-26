package io.github.cpetot.archunit;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * SpringCodingRules provides a set of general {@link ArchCondition ArchConditions}
 * and {@link ArchRule ArchRules} for coding that might be useful in the projects using Spring.
 */
public final class SpringCodingRules {

	private SpringCodingRules() {
	}

	/**
	 * A condition that checks if the methods of Java class are only called by methods annotated by {@link Transactional},
	 * directly on the method or at the class level.
	 *
	 * <p>
	 * If you use programmatic transaction management with {@code &#64;Transactional},
	 * it is recommended to always open a transaction (readonly or not) before calling a repository's method.
	 * </p>
	 *
	 * <div>
	 * Supposing you have such a repository :
	 * <pre>{@code
	 * &#64;Repository
	 * public class FooRepository {
	 *
	 * 	public void saveFoo(Foo) {
	 * 		// Impl
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Valid examples :
	 * <pre>{@code
	 * &#64;Transactional
	 * public class FooService {
	 *
	 * 	private final FooRepository repository;
	 *
	 * 	public FooService(FooRepository repository) {
	 * 		this.repository = repository;
	 * 	}
	 *
	 * 	// No annotation here, but present at class level
	 * 	public void createFoo() {
	 * 		Foo foo = new Foo();
	 * 		// Some code
	 * 		repository.saveFoo(foo);
	 * 	}
	 * }
	 *
	 * }</pre>
	 * or
	 * <pre>{@code
	 * // No annotation here, but present on the method
	 * public class FooService {
	 *
	 * 	private final FooRepository repository;
	 *
	 * 	public FooService(FooRepository repository) {
	 * 		this.repository = repository;
	 * 	}
	 *
	 * 	&#64;Transactional
	 * 	public void createFoo() {
	 * 		Foo foo = new Foo();
	 * 		// Some code
	 * 		repository.saveFoo(foo);
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Invalid examples :
	 * <pre>{@code
	 * // No annotation here
	 * public class FooService {
	 *
	 * 	private final FooRepository repository;
	 *
	 * 	public FooService(FooRepository repository) {
	 * 		this.repository = repository;
	 * 	}
	 *
	 * 	// No annotation here neither
	 * 	public void createFoo() {
	 * 		Foo foo = new Foo();
	 * 		// Some code
	 * 		repository.saveFoo(foo);
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 */
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
						if(originClass.equals(javaClass)) {
							return SimpleConditionEvent.satisfied(
								methodCall, String.format("Method %s is in the same class", originMethod.getFullName())
							);
						} else if (originMethod.isAnnotatedWith(Transactional.class)) {
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
	 * A rule that checks that all methods in a Spring {@link Repository}
	 * are only called by methods annotated by {@link Transactional},
	 * directly on the method or at the class level.
	 *
	 * <p>
	 * If you use programmatic transaction management with {@code &#64;Transactional},
	 * it is recommended to always open a transaction (readonly or not) before calling a repository's method.
	 * </p>
	 *
	 * <div>
	 * Supposing you have such a repository :
	 * <pre>{@code
	 * &#64;Repository
	 * public class FooRepository {
	 *
	 * 	public void saveFoo(Foo) {
	 * 		// Impl
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Valid examples :
	 * <pre>{@code
	 * &#64;Transactional
	 * public class FooService {
	 *
	 * 	private final FooRepository repository;
	 *
	 * 	public FooService(FooRepository repository) {
	 * 		this.repository = repository;
	 * 	}
	 *
	 * 	// No annotation here, but present at class level
	 * 	public void createFoo() {
	 * 		Foo foo = new Foo();
	 * 		// Some code
	 * 		repository.saveFoo(foo);
	 * 	}
	 * }
	 *
	 * }</pre>
	 * or
	 * <pre>{@code
	 * // No annotation here, but present on the method
	 * public class FooService {
	 *
	 * 	private final FooRepository repository;
	 *
	 * 	public FooService(FooRepository repository) {
	 * 		this.repository = repository;
	 * 	}
	 *
	 * 	&#64;Transactional
	 * 	public void createFoo() {
	 * 		Foo foo = new Foo();
	 * 		// Some code
	 * 		repository.saveFoo(foo);
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Invalid examples :
	 * <pre>{@code
	 * // No annotation here
	 * public class FooService {
	 *
	 * 	private final FooRepository repository;
	 *
	 * 	public FooService(FooRepository repository) {
	 * 		this.repository = repository;
	 * 	}
	 *
	 * 	// No annotation here neither
	 * 	public void createFoo() {
	 * 		Foo foo = new Foo();
	 * 		// Some code
	 * 		repository.saveFoo(foo);
	 * 	}
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * @see SpringCodingRules#BE_ACCESSED_BY_TRANSACTIONAL_CLASSES_OR_METHODS
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule REPOSITORIES_ARE_ACCESSED_ONLY_BY_TRANSACTIONAL_METHODS_OR_CLASSES =
		classes().that().areAnnotatedWith(Repository.class)
			.should(BE_ACCESSED_BY_TRANSACTIONAL_CLASSES_OR_METHODS);

	/**
	 * A rule that checks that all classes annotated by {@link Repository}
	 * are only accessed by classes annotated by {@link Service}.
	 *
	 * <div>
	 * Depending on your architecture, you <em>may</em> want to enforce this rule.
	 * If you do so, you will have errors if a repository is accessed by :
	 * <ul>
	 *     <li>another class annotated by {@link Repository},</li>
	 *     <li>a class annotated by {@link Controller},</li>
	 *     <li>a class without any annotation.</li>
	 * </ul>
	 * </div>
	 *
	 * @see StandardCodingRules#beAccessedOnlyByClassesMetaAnnotatedBy(Class)
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_CLASSES =
		classes().that().areAnnotatedWith(Repository.class)
			.should(StandardCodingRules.beAccessedOnlyByClassesMetaAnnotatedBy(Service.class));

	/**
	 * A rule that checks that all classes annotated by {@link Repository}
	 * are only accessed by classes annotated by {@link Service} or by {@link Controller}
	 *
	 * <div>
	 * Depending on your architecture, you <em>may</em> want to enforce this rule.
	 * If you do so, you will have errors if a repository is accessed by :
	 * <ul>
	 *     <li>another class annotated by {@link Repository},</li>
	 *     <li>a class without any annotation.</li>
	 * </ul>
	 * </div>
	 *
	 * @see StandardCodingRules#beAccessedOnlyByClassesMetaAnnotatedBy(Class)
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule REPOSITORIES_ARE_ACCESSED_ONLY_BY_SERVICE_OR_CONTROLLER_CLASSES =
		classes().that().areAnnotatedWith(Repository.class)
			.should(StandardCodingRules.beAccessedOnlyByClassesMetaAnnotatedByAny(Service.class, Controller.class));
}
