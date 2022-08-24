package io.github.cpetot.archunit.spring.stereotypes;

public class AStandardClassWithRepository {

	private final AValidTestRepository repository;

	public AStandardClassWithRepository(AValidTestRepository repository) {
		this.repository = repository;
	}
}
