package com.openclassrooms.starterjwt.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(UserMapper.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindById() throws Exception {
        // Créer un ID d'utilisateur pour le test
        Long userId = 1L;

        // Créer un utilisateur de test
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");

        // Configurer le comportement du service
        when(userService.findById(userId)).thenReturn(user);

        // Configurer le comportement du mapper
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        // Effectuer la requête GET
        ResultActions resultActions = mockMvc.perform(get("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier le statut HTTP attendu
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        // Créer un ID d'utilisateur pour le test
        Long userId = 1L;

        // Configurer le comportement du service pour retourner null (utilisateur non trouvé)
        when(userService.findById(userId)).thenReturn(null);

        // Effectuer la requête GET
        ResultActions resultActions = mockMvc.perform(get("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier le statut HTTP attendu (404 Not Found)
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        // Créez un ID d'utilisateur pour le test
        Long userId = 1L;

        // Créez un utilisateur de test
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");

        // Configurer le comportement du service pour retourner l'utilisateur
        when(userService.findById(userId)).thenReturn(user);

        // Configurer le comportement du SecurityContextHolder
        UserDetails userDetails = UserDetailsImpl.builder().username(user.getEmail()).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Effectuer la requête DELETE
        ResultActions resultActions = mockMvc.perform(delete("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier le statut HTTP attendu
        resultActions.andExpect(status().isOk());

        // Vérifier que la méthode delete du service a été appelée une fois avec le bon ID
        verify(userService, times(1)).delete(userId);
    }


    @Test
    public void testDeleteUserNotFound() throws Exception {
        // Créez un ID d'utilisateur pour le test
        Long userId = 1L;

        // Configurer le comportement du service pour retourner null (utilisateur non trouvé)
        when(userService.findById(userId)).thenReturn(null);

        // Effectuer la requête DELETE
        ResultActions resultActions = mockMvc.perform(delete("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier le statut HTTP attendu (404 Not Found)
        resultActions.andExpect(status().isNotFound());

        // Vérifier que la méthode delete du service n'a pas été appelée
        verify(userService, never()).delete(userId);
    }

    @Test
    public void testDeleteUnauthorized() throws Exception {
        // Créez un ID d'utilisateur pour le test
        Long userId = 1L;

        // Créez un utilisateur de test
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");

        // Configurer le comportement du service pour retourner l'utilisateur
        when(userService.findById(userId)).thenReturn(user);

        // Configurer le comportement du SecurityContextHolder avec un utilisateur différent
        UserDetails userDetails = UserDetailsImpl.builder().username("differentUser@example.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Effectuer la requête DELETE
        ResultActions resultActions = mockMvc.perform(delete("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier le statut HTTP attendu (401 Unauthorized)
        resultActions.andExpect(status().isUnauthorized());

        // Vérifier que la méthode delete du service n'a pas été appelée
        verify(userService, never()).delete(userId);
    }


}
