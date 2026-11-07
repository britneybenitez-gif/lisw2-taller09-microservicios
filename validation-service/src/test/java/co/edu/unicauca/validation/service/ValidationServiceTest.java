package co.edu.unicauca.validation.service;
import co.edu.unicauca.validation.dto.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ValidationServiceTest {
 private final ValidationService service=new ValidationService();
 private ValidationRequest valid(){return new ValidationRequest("¿Cuál es el resultado de 2 + 2?",List.of(
  new ValidationRequest.OptionInput("A","3"),new ValidationRequest.OptionInput("B","4"),
  new ValidationRequest.OptionInput("C","5"),new ValidationRequest.OptionInput("D","6")),"B");}
 @Test void validQuestion(){assertTrue(service.validate(valid()).valid());}
 @Test void rejectsWrongAnswerKey(){ValidationRequest r=valid();
  assertFalse(service.validate(new ValidationRequest(r.statement(),r.options(),"Z")).valid());}
 @Test void rejectsMissingOptions(){assertFalse(service.validate(new ValidationRequest("Pregunta válida",List.of(),"A")).valid());}
 @Test void rejectsBlankStatement(){ValidationRequest r=valid();
  assertFalse(service.validate(new ValidationRequest("",r.options(),"B")).valid());}
 @Test void rejectsDuplicatedOptionIds(){ValidationRequest r=valid();
  var ops=new ArrayList<>(r.options());ops.set(0,new ValidationRequest.OptionInput("B","3"));
  assertFalse(service.validate(new ValidationRequest(r.statement(),ops,"B")).valid());}
}
