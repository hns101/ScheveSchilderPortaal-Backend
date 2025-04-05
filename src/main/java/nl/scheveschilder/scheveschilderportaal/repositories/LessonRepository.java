package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Lesson;
import nl.scheveschilder.scheveschilderportaal.models.Week;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    void deleteAllByWeek(Week week);
}