package io.github.cpetot.archunit.spring.transaction;

import org.springframework.transaction.annotation.Transactional;

public class TestService {

	private final TestRepository repository;

	public TestService(TestRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void callRepositoryWithTransactional() {
		repository.doSomethingWithDatabase();
	}

	public void callRepositoryWithoutTransactional() {
		repository.doSomethingWithDatabase();
	}
}
