package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Controller;

@Controller
public class AValidTestController {

	private final AValidTestService service;

	public AValidTestController(AValidTestService service) {
		this.service = service;
	}
}
