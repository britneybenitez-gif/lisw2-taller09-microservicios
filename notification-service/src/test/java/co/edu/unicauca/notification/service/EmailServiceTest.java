package co.edu.unicauca.notification.service;
import co.edu.unicauca.notification.dto.ReviewerAssignedEvent;
import co.edu.unicauca.notification.repository.NotificationRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
class EmailServiceTest {
 @Test void deduplicatesEvent(){
  var repo=mock(NotificationRepository.class);
  when(repo.existsById("ev-1")).thenReturn(true);
  new EmailService(repo).simulate(new ReviewerAssignedEvent("ev-1",1L,2L,3L,Instant.now()));
  verify(repo,never()).save(any());
 }
}
