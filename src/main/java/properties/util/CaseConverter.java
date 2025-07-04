package properties.util;

/**
 * Utility class for converting between camelCase and snake_case strings.
 * This class provides methods to convert a camelCase string to snake_case
 * and vice versa.
 */
public final class CaseConverter {
    private CaseConverter() {
    }

    /**
     * Converts a camelCase string to snake_case.
     *
     * @param str the camelCase string to convert
     * @return the converted snake_case string
     */
    public static String camelToSnake(final String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    /**
     * Converts a snake_case string to camelCase.
     *
     * @param str the snake_case string to convert
     * @return the converted camelCase string
     */
    public static String snakeToCamel(final String str) {
        final String[] parts = str.split("_");
        final StringBuilder sBuilder = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sBuilder.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }
        return sBuilder.toString();
    }
}