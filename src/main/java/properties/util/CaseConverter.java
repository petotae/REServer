package properties.util;

public final class CaseConverter {
    private CaseConverter() {
    }

    public static String camelToSnake(final String s) {
        return s.replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    public static String snakeToCamel(final String s) {
        String[] parts = s.split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }
        return sb.toString();
    }
}