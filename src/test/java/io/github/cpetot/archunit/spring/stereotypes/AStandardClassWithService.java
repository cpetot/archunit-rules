package io.github.cpetot.archunit.spring.stereotypes;

public class AStandardClassWithService {

	private final AValidTestService service;

	public AStandardClassWithService(AValidTestService service) {
		this.service = service;
	}
}
