package co.edu.unicauca.validation.service;
import co.edu.unicauca.validation.dto.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Service public class ValidationService {
 public ValidationResult validate(ValidationRequest req) {
  List<String> errors=new ArrayList<>();
  if(req==null) return new ValidationResult(false,List.of("El cuerpo de la pregunta es obligatorio"));
  if(req.statement()==null || req.statement().isBlank() || req.statement().trim().length()<10)
   errors.add("El enunciado debe tener al menos 10 caracteres");
  List<ValidationRequest.OptionInput> options=req.options();
  if(options==null || options.size()!=4) errors.add("La pregunta debe tener exactamente 4 opciones");
  else {
   if(options.stream().anyMatch(o->o==null || o.id()==null || o.id().isBlank() || o.text()==null || o.text().isBlank()))
    errors.add("Todas las opciones deben tener identificador y texto");
   List<String> ids=options.stream().filter(Objects::nonNull).map(ValidationRequest.OptionInput::id)
    .filter(Objects::nonNull).collect(Collectors.toList());
   if(new HashSet<>(ids).size()!=4) errors.add("Los identificadores de opción deben ser únicos");
   if(req.correctOptionId()==null || ids.stream().noneMatch(id->id.equals(req.correctOptionId())))
    errors.add("La clave debe coincidir con el identificador de una opción");
  }
  return new ValidationResult(errors.isEmpty(),errors);
 }
}
