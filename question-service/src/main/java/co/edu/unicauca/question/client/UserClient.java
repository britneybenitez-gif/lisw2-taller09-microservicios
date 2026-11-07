package co.edu.unicauca.question.client;
import co.edu.unicauca.question.dto.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Set;
@Component public class UserClient {
 private final RestClient client;
 public UserClient(@Value("${services.user-url}") String url){this.client=RestClient.builder().baseUrl(url).build();}
 public UserProfile requireAuthor(String authorization){
  try{
   UserProfile profile=client.get().uri("/auth/verify").header(HttpHeaders.AUTHORIZATION,
    authorization==null?"":authorization).retrieve().body(UserProfile.class);
   if(profile==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sin sesión");
   if(profile.roles()==null || profile.roles().stream().noneMatch(Set.of("AUTHOR","TEACHER","ADMIN")::contains))
    throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Se requiere rol autor/docente");
   return profile;
  }catch(RestClientResponseException ex){
   throw new ResponseStatusException(ex.getStatusCode(),"Error de autenticación: "+ex.getStatusCode());
  }catch(ResourceAccessException ex){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"User Service no disponible");}
 }
}
