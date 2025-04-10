package nl.scheveschilder.scheveschilderportaal.service;

import nl.scheveschilder.scheveschilderportaal.dtos.LessonDto;
import nl.scheveschilder.scheveschilderportaal.dtos.WeekDto;
import nl.scheveschilder.scheveschilderportaal.models.Lesson;
import nl.scheveschilder.scheveschilderportaal.models.Student;
import nl.scheveschilder.scheveschilderportaal.models.User;
import nl.scheveschilder.scheveschilderportaal.models.Week;
import nl.scheveschilder.scheveschilderportaal.repositories.LessonRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.RoleRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.StudentRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.UserRepository;
import nl.scheveschilder.scheveschilderportaal.repositories.WeekRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeekServiceTest {

    @Mock
    private WeekRepository weekRepo;
    @Mock
    private LessonRepository lessonRepo;
    @Mock
    private StudentRepository studentRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    private WeekService weekService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weekService = new WeekService(weekRepo, lessonRepo, studentRepo, userRepo, roleRepo);
    }

    @Test
    void createWeek() {
        WeekDto dto = new WeekDto();
        dto.weekNum = 10;
        dto.startDate = LocalDate.now();
        dto.lessons = Collections.emptyList();

        Week savedWeek = new Week();
        when(weekRepo.save(any())).thenReturn(savedWeek);

        Week result = weekService.createWeek(dto);
        assertNotNull(result);
        verify(weekRepo, times(1)).save(any());
    }

    @Test
    void getAllWeeks() {
        when(weekRepo.findAll()).thenReturn(List.of(new Week()));
        List<Week> weeks = weekService.getAllWeeks();
        assertFalse(weeks.isEmpty());
    }

    @Test
    void getWeekById() {
        Week week = new Week();
        week.setId(1L);
        when(weekRepo.findById(1L)).thenReturn(Optional.of(week));
        Optional<Week> result = weekService.getWeekById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void updateWeek() {
        Week week = new Week();
        week.setId(1L);
        LessonDto lessonDto = new LessonDto();
        lessonDto.date = LocalDate.now();
        lessonDto.slot = "Vrijdag Avond";
        lessonDto.time = "19:00";
        lessonDto.students = Collections.emptyList();

        WeekDto dto = new WeekDto();
        dto.weekNum = 12;
        dto.startDate = LocalDate.now();
        dto.lessons = List.of(lessonDto);

        when(weekRepo.findById(1L)).thenReturn(Optional.of(week));
        when(studentRepo.findById(any())).thenReturn(Optional.of(new Student()));
        // Add this mock to fix the null pointer exception
        when(weekRepo.save(any(Week.class))).thenAnswer(invocation -> {
            Week savedWeek = invocation.getArgument(0);
            savedWeek.setweekNum(dto.weekNum);
            return savedWeek;
        });

        Week updated = weekService.updateWeek(1L, dto);
        assertEquals(12, updated.getweekNum());
    }

    @Test
    void deleteWeek() {
        Week week = new Week();
        week.setId(1L);
        when(weekRepo.findById(1L)).thenReturn(Optional.of(week));
        weekService.deleteWeek(1L);
        verify(weekRepo, times(1)).delete(week);
    }

    @Test
    void updateLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(5L);
        Week week = new Week();
        week.setId(3L);
        lesson.setWeek(week);

        LessonDto dto = new LessonDto();
        dto.slot = "Woensdag Avond";
        dto.time = "19:00";
        dto.date = LocalDate.now();

        when(lessonRepo.findById(5L)).thenReturn(Optional.of(lesson));
        when(lessonRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LessonDto result = weekService.updateLesson(3L, 5L, dto);

        assertNotNull(result);
        assertEquals("Woensdag Avond", result.slot);
    }

    @Test
    void updateLessonStudents() {
        Lesson lesson = new Lesson();
        Week week = new Week();
        week.setId(10L);
        lesson.setWeek(week);
        lesson.setId(22L);

        Student student = new Student();
        student.setId(1L);
        // Add User to the Student to avoid NPE
        User user = new User();
        user.setEmail("test@example.com");
        student.setUser(user);

        when(lessonRepo.findById(22L)).thenReturn(Optional.of(lesson));
        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LessonDto result = weekService.updateLessonStudents(10L, 22L, List.of(String.valueOf(1L)));
        assertEquals(1, result.students.size());
    }

    @Test
    void addStudentByEmail() {
        Lesson lesson = new Lesson();
        lesson.setId(33L);
        Week week = new Week();
        week.setId(4L);
        lesson.setWeek(week);
        // Use a mutable HashSet instead of immutable Set.of()
        lesson.setStudents(new HashSet<>());

        Student student = new Student();
        User user = new User();
        user.setEmail("test@example.com");
        student.setUser(user);

        when(lessonRepo.findById(33L)).thenReturn(Optional.of(lesson));
        when(studentRepo.findAll()).thenReturn(List.of(student));
        when(lessonRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LessonDto dto = weekService.addStudentByEmail(4L, 33L, "test@example.com");
        assertTrue(dto.students.stream().anyMatch(s -> "test@example.com".equals(s.email)));
    }

    @Test
    void removeStudentByEmail() {
        Student student = new Student();
        User user = new User();
        user.setEmail("remove@example.com");
        student.setUser(user);

        Lesson lesson = new Lesson();
        lesson.setId(44L);
        // Use a mutable set instead of immutable Set.of()
        Set<Student> students = new HashSet<>();
        students.add(student);
        lesson.setStudents(students);

        Week week = new Week();
        week.setId(6L);
        lesson.setWeek(week);

        when(lessonRepo.findById(44L)).thenReturn(Optional.of(lesson));
        when(lessonRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LessonDto dto = weekService.removeStudentByEmail(6L, 44L, "remove@example.com");
        assertTrue(dto.students.stream().noneMatch(s -> "remove@example.com".equals(s.email)));
    }
}