package io.github.cpetot.archunit.jpa;

import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
public class TestEntityIncorrectLifeCycleReturnType {

	public TestEntityIncorrectLifeCycleReturnType() {
	}

	@PrePersist
	private String prePersist() {
		return "foo";
	}
}
