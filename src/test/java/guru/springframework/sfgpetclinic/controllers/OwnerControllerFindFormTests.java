package guru.springframework.sfgpetclinic.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;

@ExtendWith(MockitoExtension.class)
public class OwnerControllerFindFormTests {

    @InjectMocks
    OwnerController controller;
    
    @Mock
    BindingResult result;
    
    @Mock
    Model model;

    @Mock
    OwnerService ownerService;

    @Captor
    ArgumentCaptor<String> captor;

    @BeforeEach
    void setup() {
        when(ownerService.findAllByLastNameLike(captor.capture())).thenAnswer(
            invocation -> {
                String lastName = invocation.getArgument(0);
                List<Owner> owners = new ArrayList<>();

                if("%EmptyResult%".equals(lastName)) {
                    return owners;
                } else if("%OneResult%".equals(lastName)) {
                    owners.add(new Owner(10l, "Jim", "Smith"));
                    return owners;
                } else if("%Multiple%".equals(lastName)) {
                    owners.add(new Owner(10l, "Jim", "Smith"));
                    owners.add(new Owner(1l, "Jane", "Smith"));
                    return owners;
                }
                throw new RuntimeException("Illegal Argument");
            }
        );
    }
 
    @Test
    @DisplayName("Find Owner No Result")
    void testFindOwnerFormNoResults() {
        //Arrange
        Owner owner = new Owner(1l, "Empty", "EmptyResult");

        //Act
        String viewName = controller.processFindForm(owner, result, model);

        //Assert
        assertAll(
            () -> assertThat("Find Owner No Result View Assert Failed", viewName, is("owners/findOwners")),
            () -> assertThat("Find Owner No Result Service Argument Failed", captor.getValue(), is("%EmptyResult%"))
            
        );

        verify(ownerService).findAllByLastNameLike(eq(captor.getValue()));
    }

    @Test()
    @DisplayName("Find Owner One Result")
    void findOwnerOneResult() {
        //Arrange
        Owner owner = new Owner(1l, "One", "OneResult");

        //Act
        String viewName = controller.processFindForm(owner, result, model);

        //Assert
        assertAll(
            () -> assertThat("Find Owner One Result View Assert Failed", viewName, is("redirect:/owners/10")),
            () -> assertThat("Find Owner One Result Service Argument Failed", captor.getValue(), is("%OneResult%"))
        );

        verify(ownerService).findAllByLastNameLike(eq(captor.getValue()));
    }

    @Test
    @DisplayName("Find Owner Multiple Result")
    void findOwnerMultipleResults() {
        //Arrange
        Owner owner = new Owner(1l, "Multi", "Multiple");

        //Act
        String viewName = controller.processFindForm(owner, result, model);

        //Assert
        assertAll(
            () -> assertThat("Find Owner Multiple Result View Assert Failed", viewName, is("owners/ownersList")),
            () -> assertThat("Find Owner Multiple Result Service Argument Failed", captor.getValue(), is("%Multiple%"))
        );

        verify(ownerService).findAllByLastNameLike(eq(captor.getValue()));
    }
    
}