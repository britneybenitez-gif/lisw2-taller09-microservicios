package co.edu.unicauca.notification.service;
import co.edu.unicauca.notification.dto.ReviewerAssignedEvent;
import co.edu.unicauca.notification.model.Notification;
import co.edu.unicauca.notification.repository.NotificationRepository;
import java.util.*;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service public class EmailService {
 private static final Logger logger=LoggerFactory.getLogger(EmailService.class);
 private final NotificationRepository repo;
 public EmailService(NotificationRepository repo){this.repo=repo;}
 @Transactional public void simulate(ReviewerAssignedEvent event){
  if(event==null || event.eventId()==null) throw new IllegalArgumentException("Evento inválido");
  if(repo.existsById(event.eventId())) return; // Idempotencia ante reintentos.
  String message="Se asignó pregunta "+event.questionId()+" al revisor "+event.reviewerId();
  repo.save(new Notification(event.eventId(),event.assignmentId(),event.questionId(),event.reviewerId(),message));
  logger.info("[NOTIFICACION SIMULADA] {}",message);
 }
 public List<Notification> list(){return repo.findAll();}
 public Notification get(String id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Notificación no encontrada"));}
}
