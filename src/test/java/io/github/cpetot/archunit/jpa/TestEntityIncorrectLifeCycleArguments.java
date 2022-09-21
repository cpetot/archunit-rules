package io.github.cpetot.archunit.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
public class TestEntityIncorrectLifeCycleArguments {

	public TestEntityIncorrectLifeCycleArguments() {
	}

	@PrePersist
	private void prePersist(int param) {
	}
}
