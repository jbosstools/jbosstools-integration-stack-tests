package com.example.switchyard.hello;

import org.switchyard.validate.BaseValidator;
import org.switchyard.validate.ValidationResult;

public class HelloValidator extends BaseValidator<String> {

	@Override
	public ValidationResult validate(String content) {
		if (content.contains("Johny")) {
			return BaseValidator.invalidResult("Hey, it should be 'Johnny' instead of 'Johny'");
		}
		return BaseValidator.validResult();
	}

}
