package io.github.cpetot.archunit.jpa;

import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
public class TestEntityIncorrectLifeCycleArguments {

	public TestEntityIncorrectLifeCycleArguments() {
	}

	@PrePersist
	private void prePersist(int param) {
	}
}
