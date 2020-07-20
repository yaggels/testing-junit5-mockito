package guru.springframework.sfgpetclinic.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;

@ExtendWith(MockitoExtension.class)
public class OwnerControllerTest {

    @Mock
    BindingResult bindingResult;

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    @Test
    void processCreationFormExceptionTesting() {
        Owner owner = new Owner(1L, "Johnson", "Chow");
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = controller.processCreationForm(owner, bindingResult);

        assertEquals("owners/createOrUpdateOwnerForm", view);
        verify(bindingResult).hasErrors();
    }

    @Test
    void processCreationFormSuccess() {
        Owner owner = new Owner(5L, "Johnson", "Chow");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(owner)).willReturn(owner);

        String view = controller.processCreationForm(owner, bindingResult);

        assertEquals("redirect:/owners/5", view);
        then(bindingResult).should().hasErrors();
        then(ownerService).should().save(owner);
    }
    
}