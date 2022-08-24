package io.github.cpetot.archunit.spring.stereotypes;

import org.springframework.stereotype.Service;

@Service
public class AValidTestServiceContainingService {

	private final AValidTestService service;

	public AValidTestServiceContainingService(AValidTestService service) {
		this.service = service;
	}
}
