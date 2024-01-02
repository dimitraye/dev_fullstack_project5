package com.openclassrooms.starterjwt.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JwtUtils.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticateUser() throws Exception {
        // Créer un objet LoginRequest pour simuler la requête
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Créer un objet UserDetailsImpl pour simuler l'utilisateur authentifié
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .build();

        // Créer un objet User pour simuler les informations de l'utilisateur depuis le repository
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(false);

        // Configurer le comportement de authenticationManager
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Configurer le comportement de jwtUtils
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fakeJWT");

        // Configurer le comportement du repository
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Effectuer la requête POST pour simuler l'authentification
        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)));

        // Vérifier le statut HTTP attendu
        resultActions.andExpect(status().isOk());
    }

    // Méthode utilitaire pour convertir un objet en chaîne JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testRegisterUser() throws Exception {
        // Créer un objet SignupRequest pour la simulation de la requête
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Configurer le comportement du UserRepository pour simuler un e-mail non existant
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        // Configurer le comportement du PasswordEncoder pour simuler l'encodage du mot de passe
        Mockito.when(passwordEncoder.encode(Mockito.any(CharSequence.class))).thenReturn("encodedPassword");
        // Effectuer la requête POST avec le SignupRequest converti en JSON
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        // Vérifier le statut HTTP attendu (200 OK)
        resultActions.andExpect(status().isOk());

        // Vérifier le contenu de la réponse
        resultActions.andExpect(content().json(objectMapper.writeValueAsString(new MessageResponse("User registered successfully!"))));

        // Vérifier que la méthode save du UserRepository a été appelée une fois
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }


}
