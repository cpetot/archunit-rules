package io.github.cpetot.archunit.jpa;

import javax.persistence.Entity;

@Entity
public class TestEntityWithPrivateEmptyConstructor {

	private TestEntityWithPrivateEmptyConstructor() {

	}
}
