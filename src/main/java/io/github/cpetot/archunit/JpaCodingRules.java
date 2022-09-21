package io.github.cpetot.archunit;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static io.github.cpetot.archunit.StandardCodingRules.BE_A_VOID_WITHOUT_PARAMETER;
import static io.github.cpetot.archunit.StandardCodingRules.HAS_A_PUBLIC_EMPTY_CONSTRUCTOR;
import static io.github.cpetot.archunit.StandardCodingRules.areAnnotatedByAny;

import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.lang.ArchRule;

/**
 * JpaCodingRules provides a set of general {@link ArchRule ArchRules} for coding that might be useful in the projects using JPA.
 */
public final class JpaCodingRules {

	private JpaCodingRules() {
	}

	/**
	 * A rule that checks that all of the given classes annotated by {@link Entity}
	 * have a public no-args constructor
	 *
	 * <p>
	 * In JPA, such a constructor is required for a valid entity.
	 * </p>
	 *
	 * <div>
	 * Valid examples :
	 * <pre>{@code
	 * 	&#64;Entity
	 * public class MyEntity {
	 *   // Fields
	 *   public MyEntity() {}
	 * }
	 *
	 * 	&#64;Entity
	 * public class MyEntity {
	 *   // Implicit public constructor
	 * }
	 *
	 * }</pre>
	 * </div>
	 *
	 * <div>
	 * Invalid examples :
	 * <pre>{@code
	 * 	&#64;Entity
	 * public class MyEntity {
	 *   // Fields
	 *   public MyEntity(Long id) {
	 *     this.id = id
	 *   }
	 * }
	 *
	 * 	&#64;Entity
	 * public class MyEntity {
	 *   // Fields
	 *   private MyEntity() {}
	 * }
	 * }</pre>
	 * </div>
	 *
	 * @see StandardCodingRules#HAS_A_PUBLIC_EMPTY_CONSTRUCTOR
	 */
	@PublicAPI(usage = ACCESS)
	public static final ArchRule JPA_ENTITIES_HAVE_PUBLIC_EMPTY_CONSTRUCTOR =
		classes().that().areAnnotatedWith(Entity.class)
			.should(HAS_A_PUBLIC_EMPTY_CONSTRUCTOR);

	// TODO Doc
	@PublicAPI(usage = ACCESS)
	public static final ArchRule LIFE_CYCLE_ANNOTATIONS_CORRECTLY_DECLARED =
		methods().that(areAnnotatedByAny(PrePersist.class, PreUpdate.class, PostPersist.class, PostUpdate.class))
			.should(BE_A_VOID_WITHOUT_PARAMETER);
}
