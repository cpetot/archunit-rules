package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Repository;

@Repository
public class AnInvalidTestRepositoryContainingController {

	private final AValidTestController controller;

	public AnInvalidTestRepositoryContainingController(AValidTestController controller) {
		this.controller = controller;
	}
}
