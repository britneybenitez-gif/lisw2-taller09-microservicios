package co.edu.unicauca.validation.controller;
import co.edu.unicauca.validation.dto.*;
import co.edu.unicauca.validation.service.ValidationService;
import org.springframework.web.bind.annotation.*;
@RestController public class ValidationController {
 private final ValidationService service;
 public ValidationController(ValidationService service) { this.service=service; }
 @PostMapping("/validate") public ValidationResult validate(@RequestBody ValidationRequest request) { return service.validate(request); }
}
