package co.edu.unicauca.question.repository;
import co.edu.unicauca.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QuestionRepository extends JpaRepository<Question,Long>{}
