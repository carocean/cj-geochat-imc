package cj.geochat.imc.common;

/**
 * Callback interface that accepts a single input argument and returns no result.
 *
 * @param <T> the type of the input to the operation
 * @author Eleftheria Stein
 * @since 5.2
 */
@FunctionalInterface
public interface Customizer<T> {

	/**
	 * Performs the customizations on the input argument.
	 * @param t the input argument
	 */
	void customize(T t);

	/**
	 * Returns a {@link Customizer} that does not alter the input argument.
	 * @return a {@link Customizer} that does not alter the input argument.
	 */
	static <T> Customizer<T> withDefaults() {
		return (t) -> {
		};
	}

}
