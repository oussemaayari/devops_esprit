package tn.esprit.SkiStationProject.ProjectTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

import tn.esprit.SkiStationProject.entities.*;
import tn.esprit.SkiStationProject.entities.enums.Support;
import tn.esprit.SkiStationProject.entities.enums.TypeCourse;
import tn.esprit.SkiStationProject.repositories.*;
import tn.esprit.SkiStationProject.services.RegistrationServicesImpl;

public class RegistrationServicesTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private SkierRepository skierRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addRegistrationAndAssignToSkier() {
        // Given
        Skier skier = new Skier();
        Registration registration = new Registration();
        when(skierRepository.findById(anyLong())).thenReturn(java.util.Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationService.addRegistrationAndAssignToSkier(new Registration(), 1L);

        // Then
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void assignRegistrationToCourse() {
        // Given
        Registration registration = new Registration();
        Course course = new Course();
        when(registrationRepository.findById(anyLong())).thenReturn(java.util.Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(java.util.Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationService.assignRegistrationToCourse(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse() {
        // Test cases for this method should cover different scenarios based on skier's age and course type
        // Write multiple test cases considering different age and course types
    }

    @Test
    void numWeeksCourseOfInstructorBySupport() {
        // Given
        Instructor instructor = new Instructor();
        when(instructorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(instructor));
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(Arrays.asList(1, 2, 3));

        // When
        List<Integer> result = registrationService.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class));
    }

    @Test
    void addRegistrationAndAssignToSkier_skierExists() {
        // Given
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), Collections.emptySet());
        Registration registration = new Registration();
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationService.addRegistrationAndAssignToSkier(new Registration(), 1L);

        // Then
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }


    @Test
    void addRegistrationAndAssignToSkier_skierDoesNotExist() {
        // Given
        when(skierRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registrationService.addRegistrationAndAssignToSkier(new Registration(), 1L);
        });

        // Then
        verify(registrationRepository, never()).save(any(Registration.class));
    }
    @Test
    void assignRegistrationToCourse_registrationOrCourseDoesNotExist() {
        // Given
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(new Course())); // assuming course exists

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registrationService.assignRegistrationToCourse(1L, 1L);
        });

        // Then
        assertEquals("no registration found with this id 1", exception.getMessage());
        verify(registrationRepository, never()).save(any(Registration.class));
    }
    @Test
    void assignRegistrationToCourse_registrationAndCourseExist() {
        // Given
        Registration registration = new Registration();
        Course course = new Course();
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationService.assignRegistrationToCourse(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }
    @Test
    void numWeeksCourseOfInstructorBySupport_instructorExists() {
        // Given
        Instructor instructor = new Instructor();
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(Arrays.asList(1, 2, 3));

        // When
        List<Integer> result = registrationService.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class));
    }
    @Test
    void addRegistrationAndAssignToSkierAndCourse_CollectiveAdultCourse_SkierOldEnough() {
        // Given
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), Collections.emptySet());
        Course course = new Course(9, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(new Registration());

        // When
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, 9L);

        // Then
        assertNotNull(result);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }
    @Test
    void addRegistrationAndAssignToSkierAndCourse_CourseFull() {
        // Given
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), Collections.emptySet());
        Course course = new Course(9, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(6L); // Simulate full course

        // When
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, 9L);

        // Then
        assertNull(result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void numWeeksCourseOfInstructorBySupport_instructorDoesNotExist() {
        // Given
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registrationService.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
        });

        // Then
        assertEquals("No Instructor Found with this id 1", exception.getMessage());
        verify(registrationRepository, never()).numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class));
    }
    @Test
    void numWeeksCourseOfInstructorBySupport_noWeeksForSupport() {
        // Given
        Instructor instructor = new Instructor();
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(Collections.emptyList());

        // When
        List<Integer> result = registrationService.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class));
    }




}