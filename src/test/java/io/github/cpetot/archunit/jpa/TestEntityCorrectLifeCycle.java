package io.github.cpetot.archunit.jpa;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
