package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Repository;

@Repository
public class AnInvalidTestRepositoryContainingRepository {

	private final AValidTestRepository repository;

	public AnInvalidTestRepositoryContainingRepository(AValidTestRepository repository) {
		this.repository = repository;
	}
}
