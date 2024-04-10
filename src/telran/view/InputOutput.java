package telran.view;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.*;

public interface InputOutput {
	public static final String OPTIONS_ERROR_MSG = "provided string does not included in options";
	public static final String PREDICATE_ERROR_MSG = "provided string does not matches the predicate";
	public static final String DATE_NOT_IN_RANGE_MSG = "date must be in between %d and %d";
	public static final String NUMBER_NOT_IN_RANGE_MSG = "number must be in between %d and %d";

	String readString(String prompt);

	void writeString(String string);

	default void writeLine(String string) {
		writeString(string + "\n");
	}

	default void writeObject(Object object) {
		writeString(object.toString());
	}

	default void writeObjectLine(Object object) {
		writeObject(object + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		while (true) {
			try {
				writeLine(prompt);
				return mapper.apply(readString(prompt));
			} catch (RuntimeException e) {
				writeLine(errorPrompt + ": " + e.getMessage());
			}
		}
	}

	default <T> T readObject(String prompt, String erPrompt, String prPrompt, Function<String, T> mapper,
			Predicate<T> predicate) {
		Function<String, T> fnMapper = mapper.andThen(e -> {
			if (!predicate.test(e)) {
				throw new RuntimeException(prPrompt);
			}
			return e;
		});
		return readObject(prompt, erPrompt, fnMapper);
	}

	default Integer readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	default Integer readInt(String prompt, String errorPrompt, int min, int max) {
		return readObject(prompt, errorPrompt, String.format(NUMBER_NOT_IN_RANGE_MSG, min, max), Integer::parseInt,
				o -> o >= min && o < max);
	}

	default Long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default Long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(prompt, errorPrompt, String.format(NUMBER_NOT_IN_RANGE_MSG, min, max), Long::parseLong,
				o -> o >= min && o < max);
	}

	default Double readDouble(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}

	default String readString(String prompt, String errorPrompt, Predicate<String> predicate) {
		return readObject(prompt, errorPrompt, PREDICATE_ERROR_MSG, Function.identity(), predicate);
	}

	default String readString(String prompt, String errorPrompt, Set<String> options) {
		return readObject(prompt, errorPrompt, OPTIONS_ERROR_MSG, Function.identity(), s -> options.contains(s));
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt, LocalDate min, LocalDate max) {
		return readObject(prompt, errorPrompt, String.format(DATE_NOT_IN_RANGE_MSG, min, max), LocalDate::parse,
				o -> o.isEqual(min) || (o.isAfter(min) && o.isBefore(max)));
	}

}
