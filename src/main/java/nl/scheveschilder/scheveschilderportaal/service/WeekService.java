package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.LessonDto;
import nl.scheveschilder.scheveschilderportaal.dtos.StudentDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekDto;
import nl.scheveschilder.scheveschilderportaal.models.*;
import nl.scheveschilder.scheveschilderportaal.repositories.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            lesson.setWeek(week); // Link back to parent

            Set<Student> students = new HashSet<>();

            for (StudentDto studentDto : lessonDto.students) {
                // 🔍 Lookup by ID first
                Student student = studentRepo.findById(studentDto.id).orElse(null);

                // 🛑 If not found, throw error (or create fallback logic)
                if (student == null) {
                    throw new IllegalArgumentException("Student met ID " + studentDto.id + " bestaat niet.");
                }

                students.add(student);
            }

            if (students.size() > 10) {
                throw new IllegalArgumentException("Les '" + lessonDto.slot + "' mag max 10 studenten hebben.");
            }

            lesson.setStudents(students);
            week.getLessons().add(lesson);
        }

        return weekRepo.save(week); // ✅ Cascade saves lessons too
    }

    public List<Week> getAllWeeks() {
        return weekRepo.findAll();
    }

    public Optional<Week> getWeekById(Long id) {
        return weekRepo.findById(id);
    }

    public Week updateWeek(Long id, WeekDto dto) {
        Week week = weekRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Week met id " + id + " niet gevonden."));

        // Clear old lessons (if you want full overwrite behavior)
        week.getLessons().clear();
        lessonRepo.deleteAllByWeek(week);

        week.setweekNum(dto.weekNum);
        week.setStartDate(dto.startDate);

        for (LessonDto lessonDto : dto.lessons) {
            Lesson lesson = new Lesson();
            lesson.setSlot(lessonDto.slot);
            lesson.setTime(lessonDto.time);
            lesson.setDate(lessonDto.date);
            lesson.setWeek(week);

            for (StudentDto studentDto : lessonDto.students) {
                Student student = studentRepo.findById(studentDto.id)
                        .orElseThrow(() -> new IllegalArgumentException("Student met id " + studentDto.id + " niet gevonden."));
                lesson.getStudents().add(student);
            }

            week.getLessons().add(lesson);
        }

        return weekRepo.save(week);
    }

    public void deleteWeek(Long id) {
        Week week = weekRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Week met id " + id + " niet gevonden."));
        weekRepo.delete(week);
    }


    private void validateUserNotLinkedToOtherStudent(User user) {
        if (user.getStudent() != null) {
            throw new IllegalStateException("Gebruiker met email '" + user.getEmail() + "' is al gekoppeld aan een andere student.");
        }
    }

    public LessonDto updateLesson(Long weekId, Long lessonId, LessonDto dto) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Les niet gevonden"));

        if (!lesson.getWeek().getId().equals(weekId)) {
            throw new IllegalArgumentException("Les hoort niet bij week " + weekId);
        }

        lesson.setSlot(dto.slot);
        lesson.setTime(dto.time);
        lesson.setDate(dto.date);

        return LessonDto.fromEntity(lessonRepo.save(lesson));
    }

    public LessonDto updateLessonStudents(Long weekId, Long lessonId, List<String> studentIds) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Les met ID " + lessonId + " niet gevonden."));

        if (!lesson.getWeek().getId().equals(weekId)) {
            throw new IllegalArgumentException("Les hoort niet bij week " + weekId);
        }

        Set<Student> newStudents = studentIds.stream()
                .map(id -> studentRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Student met ID " + id + " niet gevonden.")))
                .collect(Collectors.toSet());

        if (newStudents.size() > 10) {
            throw new IllegalArgumentException("Maximaal 10 studenten toegestaan in een les.");
        }

        lesson.setStudents(newStudents);
        lessonRepo.save(lesson);

        // Convert to DTO
        LessonDto dto = new LessonDto();
        dto.id = lesson.getId();
        dto.slot = lesson.getSlot();
        dto.time = lesson.getTime();
        dto.date = lesson.getDate();
        dto.students = lesson.getStudents().stream().map(student -> {
            StudentDto s = new StudentDto();
            s.id = student.getId();
            s.firstname = student.getFirstname();
            s.lastname = student.getLastname();
            s.defaultSlot = student.getDefaultSlot();
            s.email = student.getUser().getEmail();
            return s;
        }).toList();

        return dto;
    }

    public LessonDto addStudentByEmail(Long weekId, Long lessonId, String email) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Les niet gevonden"));

        if (!lesson.getWeek().getId().equals(weekId)) {
            throw new IllegalArgumentException("Les hoort niet bij week " + weekId);
        }

        if (lesson.getStudents().size() >= 10) {
            throw new IllegalArgumentException("Les zit al vol (max. 10 studenten).");
        }

        Student student = studentRepo.findAll().stream()
                .filter(s -> s.getUser() != null && s.getUser().getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student met email '" + email + "' niet gevonden."));

        if (lesson.getStudents().contains(student)) {
            throw new IllegalArgumentException("Student zit al in de les.");
        }

        lesson.getStudents().add(student);
        return LessonDto.fromEntity(lessonRepo.save(lesson));
    }

    public LessonDto removeStudentByEmail(Long weekId, Long lessonId, String email) {
        Lesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Les niet gevonden"));

        if (!lesson.getWeek().getId().equals(weekId)) {
            throw new IllegalArgumentException("Les hoort niet bij week " + weekId);
        }

        Student student = lesson.getStudents().stream()
                .filter(s -> s.getUser() != null && s.getUser().getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student met email '" + email + "' zit niet in deze les."));

        lesson.getStudents().remove(student);
        return LessonDto.fromEntity(lessonRepo.save(lesson));
    }


}
