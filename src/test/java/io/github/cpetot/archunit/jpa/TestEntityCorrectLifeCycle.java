package io.github.cpetot.archunit.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class TestEntityCorrectLifeCycle {

	public TestEntityCorrectLifeCycle() {
	}

	@PrePersist
	private void prePersist() {
	}

	@PreUpdate
	void preUpdate() {
	}

	@PostPersist
	protected void postPersist() {
	}

	@PostUpdate
	public void postUpdate() {
	}
}
