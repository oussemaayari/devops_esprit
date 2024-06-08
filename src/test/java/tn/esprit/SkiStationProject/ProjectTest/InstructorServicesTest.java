package tn.esprit.SkiStationProject.ProjectTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.Instructor;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.repositories.InstructorRepository;
import tn.esprit.SkiStationProject.services.InstructorServicesImpl;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@SpringBootTest
public class InstructorServicesTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Test
    void addInstructor() {
        // Given
        Instructor instructor = new Instructor();
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        // Then
        assertNotNull(savedInstructor);
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void retrieveAllInstructors() {
        // Given
        List<Instructor> instructors = Arrays.asList(new Instructor(), new Instructor());
        when(instructorRepository.findAll()).thenReturn(instructors);

        // When
        List<Instructor> retrievedInstructors = instructorServices.retrieveAllInstructors();

        // Then
        assertNotNull(retrievedInstructors);
        assertEquals(2, retrievedInstructors.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void updateInstructor() {
        // Given
        Instructor instructor = new Instructor();
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        // Then
        assertNotNull(updatedInstructor);
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void retrieveInstructor() {
        // Given
        Instructor instructor = new Instructor();
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));

        // When
        Instructor retrievedInstructor = instructorServices.retrieveInstructor(1L);

        // Then
        assertNotNull(retrievedInstructor);
        verify(instructorRepository, times(1)).findById(anyLong());
    }

    @Test
    void addInstructorAndAssignToCourse() {
        // Given
        Instructor instructor = new Instructor();
        Course course = new Course();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        Instructor updatedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        // Then
        assertNotNull(updatedInstructor);
        assertTrue(updatedInstructor.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(anyLong());
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }
    @Test
    void addInstructorAndAssignToCourse_CourseNotFound() {
        // Given
        Instructor instructor = new Instructor();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorServices.addInstructorAndAssignToCourse(instructor, 1L);
        });

        // Then
        assertEquals("no course found with this id 1", exception.getMessage());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }
    @Test
    void retrieveInstructor_InstructorNotFound() {
        // Given
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorServices.retrieveInstructor(1L);
        });

        // Then
        assertEquals("no instructor found with this id 1", exception.getMessage());
        verify(instructorRepository, times(1)).findById(anyLong());
    }


}