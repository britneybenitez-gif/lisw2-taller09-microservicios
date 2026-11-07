package co.edu.unicauca.review.client;
import co.edu.unicauca.review.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
@Component public class LifecycleClient {
 private final RestClient client;
 public LifecycleClient(@Value("${services.lifecycle-url}") String url){this.client=RestClient.builder().baseUrl(url).build();}
 public LifecycleView get(Long id){
  try{return client.get().uri("/lifecycle/{id}",id).retrieve().body(LifecycleView.class);}
  catch(RestClientResponseException e){throw new ResponseStatusException(e.getStatusCode(),"Estado no encontrado");}
  catch(RestClientException e){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Lifecycle Service no disponible");}
 }
 public void transition(Long id,String expected,String target,String reason){
  try{client.post().uri("/lifecycle/{id}/transition",id)
   .body(new TransitionRequest(expected,target,reason)).retrieve().toBodilessEntity();}
  catch(RestClientResponseException e){throw new ResponseStatusException(e.getStatusCode(),"Transición rechazada: "+e.getMessage());}
  catch(RestClientException e){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Lifecycle Service no disponible durante transición");}
 }
}
