package co.edu.unicauca.user.service;
import co.edu.unicauca.user.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
class UserServiceTest {
 private final UserService service=new UserService("Demo123!");
 @Test void loginAndVerifyOpaqueToken(){LoginResponse login=service.login(new LoginRequest("docente","Demo123!"));
  assertTrue(login.authenticated());assertTrue(service.verify("Bearer "+login.accessToken()).roles().contains("AUTHOR"));}
 @Test void wrongPasswordReturnsUnauthorized(){assertThrows(ResponseStatusException.class,
  ()->service.login(new LoginRequest("docente","mal")));}
 @Test void invalidSessionReturnsUnauthorized(){assertThrows(ResponseStatusException.class,
  ()->service.verify("Bearer sin-sesion"));}
}
