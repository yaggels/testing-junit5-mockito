package guru.springframework.sfgpetclinic.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;

@ExtendWith(MockitoExtension.class)
public class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void findByIdBddTest() {
        // given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1l)).willReturn(Optional.of(speciality));

        // when
        Speciality foundSpeciality = service.findById(1l);

        // then
        assertSame(speciality, foundSpeciality);
        assertNotNull(speciality);
        then(specialtyRepository).should().findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findById() {
        Speciality speciality = new Speciality();

        when(specialtyRepository.findById(1l)).thenReturn(Optional.of(speciality));

        Speciality foundSpeciality = service.findById(1l);

        assertSame(speciality, foundSpeciality);

        assertNotNull(speciality);

        verify(specialtyRepository).findById(eq(1l));
    }

    @Test
    void deleteById() {
        service.deleteById(1l);

        verify(specialtyRepository).deleteById(1l);
    }

    @Test
    void delete() {
        service.delete(new Speciality());
    }

    @Test
    void testDoThrow() {
        doThrow(RuntimeException.class).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testDoThrowDeleteBDD() {
        willThrow(new RuntimeException("KaBoom")).given(specialtyRepository).delete(any());

        Speciality speciality = new Speciality();
        assertThrows(RuntimeException.class, () -> service.delete(speciality));

        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLamda() {
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription("MATCH_ME");

        Speciality saveSpeciality = new Speciality();
        saveSpeciality.setId(1l);

        when(specialtyRepository.save(argThat(arg -> MATCH_ME.equals(arg.getDescription())))).thenReturn(saveSpeciality);

        Speciality save = service.save(speciality);
        assertNotNull(save);
        assertEquals(1l, save.getId());
    }
    
}