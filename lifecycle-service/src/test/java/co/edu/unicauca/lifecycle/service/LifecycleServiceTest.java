package co.edu.unicauca.lifecycle.service;
import co.edu.unicauca.lifecycle.dto.*;
import co.edu.unicauca.lifecycle.model.*;
import co.edu.unicauca.lifecycle.repository.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class LifecycleServiceTest {
 private QuestionStateRepository states;
 private OperationResultRepository operations;
 private LifecycleService service;
 private QuestionState question;
 @BeforeEach void init(){states=mock(QuestionStateRepository.class);operations=mock(OperationResultRepository.class);
  question=new QuestionState(7L);service=new LifecycleService(states,operations);
  when(states.findById(7L)).thenReturn(Optional.of(question));
  when(states.save(any(QuestionState.class))).thenAnswer(call->call.getArgument(0));
 }
 @Test void draftToPendingReview(){service.transition(7L,new TransitionRequest(QuestionStatus.DRAFT,QuestionStatus.PENDING_REVIEW,"enviar"));
  assertEquals(QuestionStatus.PENDING_REVIEW,question.getStatus());}
 @Test void cannotApproveDraft(){assertThrows(ResponseStatusException.class,
  ()->service.transition(7L,new TransitionRequest(QuestionStatus.DRAFT,QuestionStatus.APPROVED,"")));
  assertEquals(QuestionStatus.DRAFT,question.getStatus());}
 @Test void rejectNeedsReason(){question.transition(QuestionStatus.IN_REVIEW,"inicial");
  assertThrows(ResponseStatusException.class,()->service.transition(7L,
   new TransitionRequest(QuestionStatus.IN_REVIEW,QuestionStatus.REJECTED,"")));}
 @Test void commandProducesCompletedOperation(){
  when(operations.existsById("op-1")).thenReturn(false);
  service.processSubmit(new SubmitQuestionCommand(7L,"op-1"));
  assertEquals(QuestionStatus.PENDING_REVIEW,question.getStatus());
  verify(operations).save(argThat(o->o.getStatus().equals("COMPLETED") && o.getOperationId().equals("op-1")));
 }
 @Test void duplicateCommandDoesNotReprocess(){
  when(operations.existsById("op-1")).thenReturn(true);
  service.processSubmit(new SubmitQuestionCommand(7L,"op-1"));
  verify(states,never()).save(any(QuestionState.class));
 }
 @Test void invalidTransitionIsRecordedAsFailure(){
  question.transition(QuestionStatus.APPROVED,"aprobada");
  service.processSubmit(new SubmitQuestionCommand(7L,"op-2"));
  verify(operations).save(argThat(o->o.getStatus().equals("FAILED")));
 }
}
