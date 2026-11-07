package co.edu.unicauca.user.dto;
public record LoginResponse(boolean authenticated, String accessToken, String tokenType, UserProfile user) {}
