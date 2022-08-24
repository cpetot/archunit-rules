package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AValidTestRestController {

	private final AValidTestService service;

	public AValidTestRestController(AValidTestService service) {
		this.service = service;
	}
}
