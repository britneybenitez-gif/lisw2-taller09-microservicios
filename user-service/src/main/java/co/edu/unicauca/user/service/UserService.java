package co.edu.unicauca.user.service;
import co.edu.unicauca.user.dto.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service public class UserService {
 private final Map<String,UserProfile> byUsername = new HashMap<>();
 private final Map<Long,UserProfile> byId = new HashMap<>();
 private final Map<String,Long> sessions = new ConcurrentHashMap<>();
 private final String demoPassword;
 public UserService(@Value("${demo.password}") String demoPassword) {
  this.demoPassword = demoPassword;
  add(new UserProfile(1L,"docente", "docente@ejemplo.test", Set.of("TEACHER","AUTHOR")));
  add(new UserProfile(2L,"administrador", "admin@ejemplo.test", Set.of("ADMIN")));
  add(new UserProfile(3L,"revisor", "revisor@ejemplo.test", Set.of("REVIEWER")));
 }
 private void add(UserProfile p) { byUsername.put(p.username(),p); byId.put(p.id(),p); }
 public LoginResponse login(LoginRequest request) {
  if(request==null || request.username()==null || !Objects.equals(demoPassword,request.password())
      || !byUsername.containsKey(request.username())) {
   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas");
  }
  UserProfile user=byUsername.get(request.username());
  String token=UUID.randomUUID().toString(); // Token opaco en memoria; NO es JWT.
  sessions.put(token,user.id());
  return new LoginResponse(true,token,"Bearer",user);
 }
 public UserProfile verify(String authorization) {
  if(authorization==null || !authorization.startsWith("Bearer "))
   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Falta Bearer token");
  Long id=sessions.get(authorization.substring(7));
  if(id==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesión inválida o expirada");
  return byId.get(id);
 }
 public UserProfile findById(Long id) {
  UserProfile user=byId.get(id);
  if(user==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado");
  return user;
 }
}
