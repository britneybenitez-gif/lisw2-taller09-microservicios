package co.edu.unicauca.question.dto;
import java.util.Set;
public record UserProfile(Long id,String username,String email,Set<String> roles){}
