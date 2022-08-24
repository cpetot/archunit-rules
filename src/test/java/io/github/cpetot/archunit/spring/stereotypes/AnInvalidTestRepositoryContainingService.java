package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Repository;

@Repository
public class AnInvalidTestRepositoryContainingService {

	private final AValidTestService service;

	public AnInvalidTestRepositoryContainingService(AValidTestService service) {
		this.service = service;
	}
}
