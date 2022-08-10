package io.github.cpetot.archunit.spring.transaction;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TestTransactionalService {

	private final TestRepository repository;

	public TestTransactionalService(TestRepository repository) {
		this.repository = repository;
	}

	public void callRepository() {
		repository.doSomethingWithDatabase();
	}
}
