package co.edu.unicauca.review.client;
import co.edu.unicauca.review.dto.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
@Component public class UserClient {
 private final RestClient client;
 public UserClient(@Value("${services.user-url}") String url){this.client=RestClient.builder().baseUrl(url).build();}
 public UserProfile authenticated(String authorization){
  try{return client.get().uri("/auth/verify").header(HttpHeaders.AUTHORIZATION,authorization==null?"":authorization)
    .retrieve().body(UserProfile.class);}
  catch(RestClientResponseException e){throw new ResponseStatusException(e.getStatusCode(),"Autenticación rechazada");}
  catch(RestClientException e){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"User Service no disponible");}
 }
 public UserProfile requireAdmin(String auth){
  UserProfile p=authenticated(auth);
  if(p==null || p.roles()==null || !p.roles().contains("ADMIN")) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo ADMIN puede asignar revisores");
  return p;
 }
 public UserProfile requireReviewer(String auth){
  UserProfile p=authenticated(auth);
  if(p==null || p.roles()==null || !p.roles().contains("REVIEWER")) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Se requiere rol REVIEWER");
  return p;
 }
 public void ensureEligibleReviewer(Long reviewerId){
  try{
   UserProfile p=client.get().uri("/users/{id}",reviewerId).retrieve().body(UserProfile.class);
   if(p==null || p.roles()==null || !p.roles().contains("REVIEWER"))
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El usuario no tiene rol REVIEWER");
  }catch(RestClientResponseException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Revisor no existe");}
  catch(ResourceAccessException e){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"User Service no disponible");}
 }
}
