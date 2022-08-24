package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Controller;

@Controller
public class ATestControllerWithRepository {

	private final AValidTestRepository repository;

	public ATestControllerWithRepository(AValidTestRepository repository) {
		this.repository = repository;
	}
}
