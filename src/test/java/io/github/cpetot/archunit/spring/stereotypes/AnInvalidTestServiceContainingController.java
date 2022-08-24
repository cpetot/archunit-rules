package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Service;

@Service
public class AnInvalidTestServiceContainingController {

	private final AValidTestController controller;

	public AnInvalidTestServiceContainingController(AValidTestController controller) {
		this.controller = controller;
	}
}
