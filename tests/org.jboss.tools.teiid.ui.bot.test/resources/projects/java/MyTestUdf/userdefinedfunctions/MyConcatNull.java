package userdefinedfunctions;

public class MyConcatNull {

	public static String myConcatNull(String value1, String value2) {
		if (value1 == null) {
			return value2;
		}

		if (value2 == null) {
			return value1;
		}

		String sv1 = value1.toString();
		String sv2 = value2.toString();
		return sv1 + sv2;
	}
}
