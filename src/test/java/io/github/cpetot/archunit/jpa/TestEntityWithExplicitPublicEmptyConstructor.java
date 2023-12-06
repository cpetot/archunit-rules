package io.github.cpetot.archunit.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
public class TestEntityWithExplicitPublicEmptyConstructor {

	public TestEntityWithExplicitPublicEmptyConstructor() {
	}

	@PrePersist
	private void prePersist1() {
	}

	@PrePersist
	void prePersist2() {
	}

	@PrePersist
	protected void prePersist3() {
	}

	@PrePersist
	public void prePersist4() {
	}
}
