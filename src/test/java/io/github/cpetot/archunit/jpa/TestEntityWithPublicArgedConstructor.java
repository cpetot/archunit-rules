package io.github.cpetot.archunit.jpa;

import javax.persistence.Entity;

@Entity
public class TestEntityWithPublicArgedConstructor {

	private Long id;

	public TestEntityWithPublicArgedConstructor(Long id) {
		this.id = id;
	}
}
