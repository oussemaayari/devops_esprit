package tn.esprit.SkiStationProject.ProjectTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.Piste;
import tn.esprit.SkiStationProject.entities.Registration;
import tn.esprit.SkiStationProject.entities.Skier;
import tn.esprit.SkiStationProject.entities.Subscription;
import tn.esprit.SkiStationProject.entities.enums.Color;
import tn.esprit.SkiStationProject.entities.enums.Support;
import tn.esprit.SkiStationProject.entities.enums.TypeCourse;
import tn.esprit.SkiStationProject.entities.enums.TypeSubscription;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.repositories.PisteRepository;
import tn.esprit.SkiStationProject.repositories.RegistrationRepository;
import tn.esprit.SkiStationProject.repositories.SkierRepository;
import tn.esprit.SkiStationProject.repositories.SubscriptionRepository;
import tn.esprit.SkiStationProject.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesTest {

    @Mock
    SkierRepository skierRepository;

    @Mock
    PisteRepository pisteRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    RegistrationRepository registrationRepository;

    @Mock
    SubscriptionRepository subscriptionRepository;

    @InjectMocks
    SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void removeSkier() {
        // Calling the method under test
        skierServices.removeSkier(1L);

        // Verifying the method call
        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void retrieveSkier() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        // Calling the method under test
        Skier retrievedSkier = skierServices.retrieveSkier(1L);

        // Verifying the result
        assertEquals(skier, retrievedSkier);
    }
    @Test
    void retrieveAllSkiers() {
        // Mocking repository behavior
        List<Skier> skiers = Collections.singletonList(new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>()));
        when(skierRepository.findAll()).thenReturn(skiers);

        // Calling the method under test
        List<Skier> retrievedSkiers = skierServices.retrieveAllSkiers();

        // Verifying the result
        assertEquals(skiers, retrievedSkiers);
    }
    @Test
    void addSkier() {

        Subscription subscription = new Subscription(LocalDate.of(2024, 4, 1), LocalDate.of(2024, 10, 1), 50.0f, TypeSubscription.MONTHLY);

        Set<Piste> pisteSet = new HashSet<>();
        Set<Registration>  registrations= new HashSet<>();

        // Créer l'objet Skier avec les ensembles créés
        Skier skierToAdd = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", subscription, pisteSet,registrations);

        // Configurer le comportement du repository mocké
        when(skierRepository.save(skierToAdd)).thenReturn(skierToAdd);

        // Appeler la méthode à tester
        Skier addedSkier = skierServices.addSkier(skierToAdd);

        // Vérifier le résultat
        assertEquals(skierToAdd, addedSkier);
    }


    @Test
    public void addSkierAndAssignToCourse_shouldSaveSkierAndAssignToCourse() {
        // Préparation des données de test
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        Long numCourse = 1L; // ID de la course
        Course course = new Course(/* données de test */);

        // Configuration du comportement attendu des mocks
        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.findById(numCourse)).thenReturn(Optional.of(course));

        // Appel de la méthode à tester
        Skier savedSkier = skierServices.addSkierAndAssignToCourse(skier, numCourse);

        // Vérifications
        verify(skierRepository).save(skier); // Vérifie que skierRepository.save() est appelé avec le bon skieur
        verify(courseRepository).findById(numCourse); // Vérifie que courseRepository.findById() est appelé avec le bon ID de cours
        verify(registrationRepository, times(skier.getRegistrations().size())).save(any(Registration.class)); // Vérifie que registrationRepository.save() est appelé pour chaque inscription

        // Vérification que chaque inscription a été correctement configurée
        for (Registration registration : savedSkier.getRegistrations()) {
            assertEquals(skier, registration.getSkier());
            assertEquals(course, registration.getCourse());
        }

        // Assurez-vous d'adapter ces vérifications en fonction de votre implémentation réelle et de vos besoins spécifiques
    }

    @Test
    void assignSkierToSubscription() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.MONTHLY);
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier); // Mocking save method

        // Calling the method under test
        Skier assignedSkier = skierServices.assignSkierToSubscription(1L, 1L);

        // Verifying the result
        assertEquals(subscription, assignedSkier.getSubscription());
    }

    @Test
    void assignSkierToPiste() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        Piste piste = new Piste("Red", Color.RED, 1000, 50, new HashSet<>());
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier); // Mocking save method

        // Calling the method under test
        Skier assignedSkier = skierServices.assignSkierToPiste(1L, 1L);

        // Verifying the result
        assertEquals(1, assignedSkier.getPistes().size());
        assertEquals(piste, assignedSkier.getPistes().iterator().next());
    }

    @Test
    void retrieveSkiersBySubscriptionType() {
        // Mocking repository behavior
        List<Skier> skiers = Collections.singletonList(new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>()));
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.MONTHLY)).thenReturn(skiers);

        // Calling the method under test
        List<Skier> retrievedSkiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.MONTHLY);

        // Verifying the result
        assertEquals(skiers, retrievedSkiers);
    }


    @Test
    void addSkierWithDifferentSubscriptionTypes() {
        // Test adding skiers with different subscription types
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        Subscription annualSubscription = new Subscription(startDate, null, 50.0f, TypeSubscription.ANNUAL);
        Subscription semestrialSubscription = new Subscription(startDate, null, 50.0f, TypeSubscription.SEMESTRIEL);
        Subscription monthlySubscription = new Subscription(startDate, null, 50.0f, TypeSubscription.MONTHLY);

        Skier skierWithAnnualSubscription = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", annualSubscription, Collections.emptySet(), Collections.emptySet());
        Skier skierWithSemestrialSubscription = new Skier("Jane", "Doe", LocalDate.of(1990, 5, 15), "City", semestrialSubscription, Collections.emptySet(), Collections.emptySet());
        Skier skierWithMonthlySubscription = new Skier("Alice", "Doe", LocalDate.of(1990, 5, 15), "City", monthlySubscription, Collections.emptySet(), Collections.emptySet());

        // Mock repository behavior
        when(skierRepository.save(skierWithAnnualSubscription)).thenReturn(skierWithAnnualSubscription);
        when(skierRepository.save(skierWithSemestrialSubscription)).thenReturn(skierWithSemestrialSubscription);
        when(skierRepository.save(skierWithMonthlySubscription)).thenReturn(skierWithMonthlySubscription);

        // Call the method under test
        Skier addedSkierWithAnnualSubscription = skierServices.addSkier(skierWithAnnualSubscription);
        Skier addedSkierWithSemestrialSubscription = skierServices.addSkier(skierWithSemestrialSubscription);
        Skier addedSkierWithMonthlySubscription = skierServices.addSkier(skierWithMonthlySubscription);

        // Verify the result
        assertEquals(addedSkierWithAnnualSubscription.getSubscription().getEndDate(), startDate.plusYears(1));
        assertEquals(addedSkierWithSemestrialSubscription.getSubscription().getEndDate(), startDate.plusMonths(6));
        assertEquals(addedSkierWithMonthlySubscription.getSubscription().getEndDate(), startDate.plusMonths(1));
    }

    @Test
    void assignSkierToPiste_NonExistingSkier() {
        // Test assigning a skier to a piste when skier does not exist
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method under test
        Skier assignedSkier = skierServices.assignSkierToPiste(1L, 1L);

        // Verify the result
        assertNull(assignedSkier);
    }

    @Test
    void assignSkierToPiste_NonExistingPiste() {
        // Test assigning a skier to a piste when piste does not exist
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, Collections.emptySet(), Collections.emptySet());
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method under test
        Skier assignedSkier = skierServices.assignSkierToPiste(1L, 1L);

        // Verify the result
        assertNull(assignedSkier); // Ensure that assignedSkier is null
    }


    @Test
    void assignSkierToPiste_SkierAlreadyAssignedToPiste() {
        // Test assigning a skier to a piste when skier is already assigned to the piste
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), Collections.emptySet());
        Piste piste = new Piste("Red", Color.RED, 1000, 50, Collections.emptySet());
        skier.getPistes().add(piste); // Add the same piste to simulate skier already assigned to it
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        // Call the method under test
        Skier assignedSkier = skierServices.assignSkierToPiste(1L, 1L);

        // Verify the result
        assertNotNull(assignedSkier);
        assertEquals(1, assignedSkier.getPistes().size()); // Ensure only one piste is assigned

        // Additional assertion to check if the assigned piste is the same as the one already assigned
        assertTrue(assignedSkier.getPistes().contains(piste));
    }



    @Test
    void retrieveSkier_NonExistingSkier() {
        // Test retrieving a skier that does not exist
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method under test
        Skier retrievedSkier = skierServices.retrieveSkier(1L);

        // Verify the result
        assertNull(retrievedSkier);
    }

    @Test
    void retrieveSkiersBySubscriptionType_EmptyList() {
        // Test retrieving skiers by subscription type when no skiers with that type exist
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(Collections.emptyList());

        // Call the method under test
        List<Skier> skiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        // Verify the result
        assertNotNull(skiers);
        assertTrue(skiers.isEmpty());
    }
    // Add other tests here for the remaining methods in SkierServicesImpl
}