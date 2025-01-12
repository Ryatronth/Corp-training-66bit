package rnn.core.authentication.mapper;

public enum Provider {
    GIT_HUB,
    GIT_LAB;

    public static Provider fromRegistrationId(String registrationId) {
        return switch (registrationId) {
            case "github" -> GIT_HUB;
            case "gitlab" -> GIT_LAB;
            default -> throw new IllegalArgumentException("Unknown auth provider: " + registrationId);
        };
    }
}
