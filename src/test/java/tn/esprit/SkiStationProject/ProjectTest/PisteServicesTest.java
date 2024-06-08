package tn.esprit.SkiStationProject.ProjectTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import tn.esprit.SkiStationProject.entities.Piste;
import tn.esprit.SkiStationProject.repositories.PisteRepository;
import tn.esprit.SkiStationProject.services.PisteServicesImpl;

public class PisteServicesTest {

    @Mock
    private PisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRetrieveAllPistes() {
        // Given
        List<Piste> pistes = new ArrayList<>();
        pistes.add(new Piste());
        pistes.add(new Piste());
        when(pisteRepository.findAll()).thenReturn(pistes);

        // When
        List<Piste> result = pisteService.retrieveAllPistes();

        // Then
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        // Given
        Piste pisteToAdd = new Piste();
        when(pisteRepository.save(pisteToAdd)).thenReturn(pisteToAdd);

        // When
        Piste result = pisteService.addPiste(pisteToAdd);

        // Then
        assertEquals(pisteToAdd, result);
        verify(pisteRepository, times(1)).save(pisteToAdd);
    }

    @Test
    void testRemovePiste() {
        // Given
        Long pisteIdToRemove = 1L;

        // When
        pisteService.removePiste(pisteIdToRemove);

        // Then
        verify(pisteRepository, times(1)).deleteById(pisteIdToRemove);
    }

    @Test
    void testRetrievePiste() {
        // Given
        Long pisteId = 1L;
        Piste piste = new Piste();
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));

        // When
        Piste result = pisteService.retrievePiste(pisteId);

        // Then
        assertEquals(piste, result);
        verify(pisteRepository, times(1)).findById(pisteId);
    }
    @Test
    void testRetrievePiste_NotFound() {
        // Given
        Long nonExistentId = 100L;
        when(pisteRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pisteService.retrievePiste(nonExistentId);
        });

        // Then
        assertEquals("no piste found with this id " + nonExistentId, exception.getMessage());
        verify(pisteRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testRetrievePiste_Success() {
        // Given
        Long pisteId = 1L;
        Piste expectedPiste = new Piste();
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(expectedPiste));

        // When
        Piste result = pisteService.retrievePiste(pisteId);

        // Then
        assertEquals(expectedPiste, result);
        verify(pisteRepository, times(1)).findById(pisteId);
    }

    @Test
    void removeNonExistentPiste() {
        // Given
        Long nonExistentId = 100L;
        doThrow(EmptyResultDataAccessException.class).when(pisteRepository).deleteById(nonExistentId);

        // When
        assertThrows(EmptyResultDataAccessException.class, () -> pisteService.removePiste(nonExistentId));

        // Then
        verify(pisteRepository, times(1)).deleteById(nonExistentId);
    }

    @Test
    void retrieveAllPistesEmpty() {
        // Given
        when(pisteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Piste> result = pisteService.retrieveAllPistes();

        // Then
        assertTrue(result.isEmpty());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void retrievePisteWithNegativeId() {
        // When
        assertThrows(IllegalArgumentException.class, () -> pisteService.retrievePiste(-1L));
    }

    @Test
    void retrieveNonExistentPiste() {
        // Given
        Long nonExistentPisteId = 999L;
        when(pisteRepository.findById(nonExistentPisteId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> pisteService.retrievePiste(nonExistentPisteId));
    }


// Add more tests for validation rules if necessary


}