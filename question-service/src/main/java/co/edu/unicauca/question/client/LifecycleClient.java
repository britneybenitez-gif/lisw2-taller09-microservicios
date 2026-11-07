package co.edu.unicauca.question.client;
import co.edu.unicauca.question.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
@Component public class LifecycleClient {
 private final RestClient client;
 public LifecycleClient(@Value("${services.lifecycle-url}") String url){this.client=RestClient.builder().baseUrl(url).build();}
 public void initialize(Long id){
  try{client.post().uri("/lifecycle/{id}/initialize",id).retrieve().toBodilessEntity();}
  catch(RestClientException ex){throw unavailable(ex);}
 }
 public LifecycleView state(Long id){
  try{return client.get().uri("/lifecycle/{id}",id).retrieve().body(LifecycleView.class);}
  catch(RestClientResponseException ex){throw new ResponseStatusException(ex.getStatusCode(),"No se obtuvo estado: "+ex.getMessage());}
  catch(RestClientException ex){throw unavailable(ex);}
 }
 public void transition(Long id,TransitionRequest req){
  try{client.post().uri("/lifecycle/{id}/transition",id).body(req).retrieve().toBodilessEntity();}
  catch(RestClientResponseException ex){throw new ResponseStatusException(ex.getStatusCode(),"Transición rechazada: "+ex.getMessage());}
  catch(RestClientException ex){throw unavailable(ex);}
 }
 public OperationView operation(String id){
  try{return client.get().uri("/operations/{id}",id).retrieve().body(OperationView.class);}
  catch(HttpClientErrorException.NotFound ex){return null;}
  catch(RestClientException ex){throw unavailable(ex);}
 }
 private ResponseStatusException unavailable(Exception ex){return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Lifecycle Service no disponible: "+ex.getMessage());}
}
