package io.github.cpetot.archunit.spring.transaction;

import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository {

	void doSomethingWithDatabase();

	default void doSomethingWithDefault() {
		doSomethingWithDatabase();
	}
}
