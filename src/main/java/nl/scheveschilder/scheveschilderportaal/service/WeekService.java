package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.LessonDto;
import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekDto;
import nl.scheveschilder.scheveschilderportaal.models.*;
import nl.scheveschilder.scheveschilderportaal.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WeekService {
    private final WeekRepository weekRepo;
    private final LessonRepository lessonRepo;
    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public WeekService(WeekRepository weekRepo, LessonRepository lessonRepo, StudentRepository studentRepo,
                       UserRepository userRepo, RoleRepository roleRepo) {
        this.weekRepo = weekRepo;
        this.lessonRepo = lessonRepo;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public Week createWeek(WeekDto dto) {
        Week week = new Week();
        week.setweekNum(dto.weekNum);
        week.setStartDate(dto.startDate);

        for (LessonDto lessonDto : dto.lessons) {
            Lesson lesson = new Lesson();
            lesson.setSlot(lessonDto.slot);
            lesson.setTime(lessonDto.time);
            lesson.setDate(lessonDto.date);
            lesson.setWeek(week);

            for (StudentDto studentDto : lessonDto.students) {
                if (lesson.getStudents().size() >= 10) {
                    throw new IllegalArgumentException("Les '" + lesson.getSlot() + "' op " + lesson.getDate() + " zit al vol (maximaal 10 studenten).");
                }

                Student student = studentRepo.findById(studentDto.id).orElseGet(() -> {
                    // 🔍 Check if user exists by email
                    User user = userRepo.findById(studentDto.email).orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(studentDto.email);
                        newUser.setPassword("changeme"); // TODO: Encrypt properly

                        // ✅ Assign ROLE_USER if available
                        Role roleUser = roleRepo.findById("ROLE_USER")
                                .orElseThrow(() -> new IllegalStateException("Role 'ROLE_USER' not found."));
                        newUser.setRoles(Set.of(roleUser));

                        return userRepo.save(newUser);
                    });

                    // ❗ Check if this user is already linked to a different student
                    if (user.getStudent() != null) {
                        throw new IllegalArgumentException("Gebruiker met email '" + user.getEmail() + "' is al gekoppeld aan een andere student.");
                    }

                    // 🧩 Create and link student
                    Student newStudent = new Student();
                    newStudent.setId(studentDto.id);
                    newStudent.setFirstname(studentDto.firstname);
                    newStudent.setLastname(studentDto.lastname);
                    newStudent.setDefaultSlot(studentDto.defaultSlot);
                    newStudent.setActiveMember(true);
                    newStudent.setUser(user);

                    return studentRepo.save(newStudent);
                });

                lesson.getStudents().add(student);
            }

            week.getLessons().add(lesson);
        }

        return weekRepo.save(week);
    }

    public List<Week> getAllWeeks() {
        return weekRepo.findAll();
    }

    public Optional<Week> getWeekById(Long id) {
        return weekRepo.findById(id);
    }

    private void validateUserNotLinkedToOtherStudent(User user) {
        validateUserNotLinkedToOtherStudent(user);
    }

}
