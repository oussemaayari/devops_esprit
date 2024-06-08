package tn.esprit.SkiStationProject.ProjectTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.enums.Support;
import tn.esprit.SkiStationProject.entities.enums.TypeCourse;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.services.CourseServicesImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServicesTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void retrieveAllCourses() {
        // Mocking repository behavior
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1,TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null));
        courses.add(new Course(2, TypeCourse.COLLECTIVE_CHILDREN, Support.SNOWBOARD, 60.0f, 2, null));
        when(courseRepository.findAll()).thenReturn(courses);

        // Calling the method under test
        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        // Verifying the result
        assertEquals(courses, retrievedCourses);
    }

    @Test
    void addCourse() {
        // Mocking repository behavior
        Course courseToAdd = new Course(1, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(courseRepository.save(courseToAdd)).thenReturn(courseToAdd);

        // Calling the method under test
        Course addedCourse = courseServices.addCourse(courseToAdd);

        // Verifying the result
        assertEquals(courseToAdd, addedCourse);
    }

    @Test
    void updateCourse() {
        // Mocking repository behavior
        Course courseToUpdate = new Course(1, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(courseRepository.save(courseToUpdate)).thenReturn(courseToUpdate);

        // Calling the method under test
        Course updatedCourse = courseServices.updateCourse(courseToUpdate);

        // Verifying the result
        assertEquals(courseToUpdate, updatedCourse);
    }

    @Test
    void retrieveCourse() {
        // Mocking repository behavior
        Long courseId = 1L;
        Course course = new Course(1, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Calling the method under test
        Course retrievedCourse = courseServices.retrieveCourse(courseId);

        // Verifying the result
        assertEquals(course, retrievedCourse);
    }

    @Test
    void retrieveAllCoursesEmpty() {
        // Mocking repository behavior
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        // Calling the method under test
        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        // Verifying the result
        assertTrue(retrievedCourses.isEmpty());
    }

    @Test
    void retrieveNonExistentCourse() {
        // Mocking repository behavior
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Calling the method under test
        assertThrows(IllegalArgumentException.class, () -> courseServices.retrieveCourse(courseId));
    }

    @Test
    void updateNonExistentCourse() {
        // Mocking repository behavior
        Course nonExistentCourse = new Course(9, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(courseRepository.save(nonExistentCourse)).thenThrow(new IllegalArgumentException());

        // Calling the method under test
        assertThrows(IllegalArgumentException.class, () -> courseServices.updateCourse(nonExistentCourse));
    }

    @Test
    void deleteNonExistentCourse() {
        // Mocking repository behavior
        Long courseId = 999L;
        doThrow(new IllegalArgumentException()).when(courseRepository).deleteById(eq(courseId));

        // Calling the method under test
        assertThrows(IllegalArgumentException.class, () -> courseServices.retrieveCourse(courseId));
    }

    @Test
    void retrieveAllCoursesFailure() {
        // Mocking repository behavior
        when(courseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Calling the method under test
        assertThrows(RuntimeException.class, () -> courseServices.retrieveAllCourses());
    }

    @Test
    void addCourseFailure() {
        // Mocking repository behavior
        Course courseToAdd = new Course(1, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(courseRepository.save(courseToAdd)).thenThrow(new RuntimeException("Database error"));

        // Calling the method under test
        assertThrows(RuntimeException.class, () -> courseServices.addCourse(courseToAdd));
    }

    @Test
    void updateCourseFailure() {
        // Mocking repository behavior
        Course courseToUpdate = new Course(1, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(courseRepository.save(courseToUpdate)).thenThrow(new RuntimeException("Database error"));

        // Calling the method under test
        assertThrows(RuntimeException.class, () -> courseServices.updateCourse(courseToUpdate));
    }


}