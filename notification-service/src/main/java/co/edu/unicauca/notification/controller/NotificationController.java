package co.edu.unicauca.notification.controller;
import co.edu.unicauca.notification.model.Notification;
import co.edu.unicauca.notification.service.EmailService;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/notifications") public class NotificationController {
 private final EmailService service;
 public NotificationController(EmailService service){this.service=service;}
 @GetMapping public List<Notification> list(){return service.list();}
 @GetMapping("/{eventId}") public Notification get(@PathVariable String eventId){return service.get(eventId);}
}
