package com.woowa.user.domain;

public enum Gender {
	MALE, FEMALE, OTHER;

	public static Gender fromRegisterNumber(String number) {
		return switch (number) {
			case "1", "3" -> MALE;
			case "2", "4" -> FEMALE;
			default -> OTHER;
		};
	}
}

