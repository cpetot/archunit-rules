package io.github.cpetot.archunit.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
public class TestEntityIncorrectLifeCycleReturnType {

	public TestEntityIncorrectLifeCycleReturnType() {
	}

	@PrePersist
	private String prePersist() {
		return "foo";
	}
}
