package nl.scheveschilder.scheveschilderportaal.repositories;

import nl.scheveschilder.scheveschilderportaal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}
