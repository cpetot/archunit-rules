package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Service;

@Service
public class AValidTestService {
	private final AValidTestRepository repository;

	public AValidTestService(AValidTestRepository repository) {
		this.repository = repository;
	}
}
