package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Repository;

@Repository
public class AnInvalidTestControllerContainingController {

	private final AValidTestController controller;

	public AnInvalidTestControllerContainingController(AValidTestController controller) {
		this.controller = controller;
	}
}
