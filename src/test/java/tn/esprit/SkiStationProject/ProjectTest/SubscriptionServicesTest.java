package tn.esprit.SkiStationProject.ProjectTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.SkiStationProject.entities.Skier;
import tn.esprit.SkiStationProject.entities.Subscription;
import tn.esprit.SkiStationProject.entities.enums.TypeSubscription;
import tn.esprit.SkiStationProject.repositories.SkierRepository;
import tn.esprit.SkiStationProject.repositories.SubscriptionRepository;
import tn.esprit.SkiStationProject.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServicesTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        when(subscriptionRepository.save(any())).thenReturn(subscription);

        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);

        assertEquals(subscription.getTypeSub(), savedSubscription.getTypeSub());
        // You can add more assertions based on your requirements
    }

    @Test
    void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        when(subscriptionRepository.save(any())).thenReturn(subscription);

        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);

        assertEquals(subscription.getTypeSub(), updatedSubscription.getTypeSub());
        // You can add more assertions based on your requirements
    }

    @Test
    void testRetrieveSubscriptionById() {
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription();
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(java.util.Optional.of(subscription));

        Subscription retrievedSubscription = subscriptionServices.retrieveSubscriptionById(subscriptionId);

        assertEquals(subscription.getId(), retrievedSubscription.getId());
    }

    @Test
    void testGetSubscriptionByType() {
        TypeSubscription type = TypeSubscription.MONTHLY;
        Subscription subscription1 = new Subscription();
        subscription1.setTypeSub(type);
        Subscription subscription2 = new Subscription();
        subscription2.setTypeSub(type);
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(subscription1);
        subscriptions.add(subscription2);
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<Subscription> retrievedSubscriptions = subscriptionServices.getSubscriptionByType(type);

        assertEquals(subscriptions.size(), retrievedSubscriptions.size());
        // You can add more assertions if needed
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        List<Subscription> expectedSubscriptions = new ArrayList<>();
        // Add expected subscriptions to the list

        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(expectedSubscriptions);

        List<Subscription> retrievedSubscriptions = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertEquals(expectedSubscriptions.size(), retrievedSubscriptions.size());
        // You can add more assertions if needed
    }
    @Test
    void testRetrieveSubscriptionById_Null() {
        Long subscriptionId = 999L; // A non-existing subscription ID
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

        Subscription retrievedSubscription = subscriptionServices.retrieveSubscriptionById(subscriptionId);

        assertNull(retrievedSubscription);
    }

    @Test
    void testGetSubscriptionByType_Empty() {
        TypeSubscription type = TypeSubscription.ANNUAL; // Assuming DAILY type does not exist
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(new HashSet<>());

        Set<Subscription> retrievedSubscriptions = subscriptionServices.getSubscriptionByType(type);

        assertTrue(retrievedSubscriptions.isEmpty());
    }
    @Test
    void testCalculateEndDate_AnnualSubscription() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 4, 22);
        LocalDate startEnd = LocalDate.of(2025, 4, 22);
        Subscription subscription = new Subscription(startDate,startEnd, 100.0F,TypeSubscription.MONTHLY);

        // When
        LocalDate endDate = subscription.getEndDate();

        // Then
        assertEquals(startDate.plusYears(1), endDate);
    }



}