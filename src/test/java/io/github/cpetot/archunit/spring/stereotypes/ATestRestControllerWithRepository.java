package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ATestRestControllerWithRepository {

	private final AValidTestRepository repository;

	public ATestRestControllerWithRepository(AValidTestRepository repository) {
		this.repository = repository;
	}
}
