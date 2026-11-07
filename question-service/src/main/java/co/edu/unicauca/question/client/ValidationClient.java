package co.edu.unicauca.question.client;
import co.edu.unicauca.question.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
@Component public class ValidationClient {
 private final RestClient client;
 public ValidationClient(@Value("${services.validation-url}") String url){this.client=RestClient.builder().baseUrl(url).build();}
 public ValidationResult validate(QuestionPayload payload){
  try{return client.post().uri("/validate").body(payload).retrieve().body(ValidationResult.class);}
  catch(RestClientException ex){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Validation Service no disponible: "+ex.getMessage());}
 }
}
