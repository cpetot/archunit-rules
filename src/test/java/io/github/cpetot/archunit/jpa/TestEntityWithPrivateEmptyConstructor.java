package io.github.cpetot.archunit.jpa;

import jakarta.persistence.Entity;

@Entity
public class TestEntityWithPrivateEmptyConstructor {

	private TestEntityWithPrivateEmptyConstructor() {

	}
}
