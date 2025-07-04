package properties.util;

public final class CaseConverter {
    private CaseConverter() {
    }

    public static String camelToSnake(final String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

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