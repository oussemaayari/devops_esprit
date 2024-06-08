package tn.esprit.SkiStationProject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.SkiStationProject.entities.*;
import tn.esprit.SkiStationProject.entities.enums.TypeSubscription;
import tn.esprit.SkiStationProject.repositories.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SkierServicesImpl implements ISkierServices {

    private final SkierRepository skierRepository;

    private final PisteRepository pisteRepository;

    private final CourseRepository courseRepository;

    private final RegistrationRepository registrationRepository;

    private final SubscriptionRepository subscriptionRepository;


    @Override
    public List<Skier> retrieveAllSkiers() {
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        switch (skier.getSubscription().getTypeSub()) {
            case ANNUAL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                break;
        }
        return skierRepository.save(skier);
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);
        skier.setSubscription(subscription);
        return skierRepository.save(skier);
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.findById(numCourse).orElseThrow(() -> new IllegalArgumentException("No Course Found with this id " + numCourse));
        savedSkier.getRegistrations().forEach(registration -> {
            registration.setSkier(savedSkier);
            registration.setCourse(course);
            registrationRepository.save(registration);
        });
        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        skierRepository.deleteById(numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        return skierRepository.findById(numSkier).orElse(null);
    }

    public Skier assignSkierToPiste(Long skierId, Long pisteId) {
        Optional<Skier> skierOptional = skierRepository.findById(skierId);
        Optional<Piste> pisteOptional = pisteRepository.findById(pisteId);

        if (skierOptional.isPresent() && pisteOptional.isPresent()) {
            Skier skier = skierOptional.get();
            Piste piste = pisteOptional.get();

            // Check if the skier is already assigned to the piste
            if (skier.getPistes().contains(piste)) {
                // Skier is already assigned to the piste, no need to reassign
                return skier;
            }

            // Assign the skier to the piste
            skier.getPistes().add(piste);
            skierRepository.save(skier); // Save the updated skier object

            return skier;
        } else {
            // Skier or piste not found
            return null;
        }
    }



    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}