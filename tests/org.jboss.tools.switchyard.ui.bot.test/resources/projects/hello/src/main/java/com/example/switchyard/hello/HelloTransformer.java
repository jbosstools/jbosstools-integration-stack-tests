package com.example.switchyard.hello;

import org.switchyard.annotations.Transformer;

public final class HelloTransformer {

	@Transformer
	public Person transformStringToPerson(String from) {
		return new Person(from);
	}

}
