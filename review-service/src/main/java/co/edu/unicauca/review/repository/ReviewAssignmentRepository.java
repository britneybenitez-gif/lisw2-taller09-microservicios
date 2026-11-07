package co.edu.unicauca.review.repository;
import co.edu.unicauca.review.model.ReviewAssignment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewAssignmentRepository extends JpaRepository<ReviewAssignment,Long>{
 Optional<ReviewAssignment> findFirstByQuestionIdOrderByIdDesc(Long questionId);
}
