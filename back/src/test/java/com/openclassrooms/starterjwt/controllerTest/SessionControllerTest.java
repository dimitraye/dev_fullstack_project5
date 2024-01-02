package com.openclassrooms.starterjwt.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.DataTest;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SessionMapper.class)
@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SessionControllerTest {

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

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
    public void findAll() throws Exception {
        List<Session> sessions = DataTest.generateSessions();

        when(sessionService.findAll()).thenReturn(sessions);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void findById() throws Exception {
        Session session = DataTest.generateSessions().get(0);
        session.setId(1L);


        when(sessionService.getById(session.getId())).thenReturn(session);

        mockMvc.perform(get("/api/session/{id}",session.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testCreateSession() throws Exception {
        // Créer un objet SessionDto valide
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Test Description");
        // ... Ajouter d'autres propriétés nécessaires

        // Convertir l'objet SessionDto en JSON
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        // Définir le comportement attendu lors de l'appel à la méthode create du service
        when(sessionService.create(sessionMapper.toEntity(sessionDto))).thenReturn(new Session());

        // Envoyer une requête POST avec l'objet SessionDto
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk());
    }



    @Test
    public void testUpdateSession() throws Exception {
        // Créez une SessionDto de test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Test Description");

        // Configurez le comportement du mapper
        when(sessionMapper.toEntity(sessionDto)).thenReturn(new Session()); // ou votre objet Session simulé

        // Configurez le comportement du service
        when(sessionService.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Session.class))).thenReturn(new Session()); // ou votre objet Session simulé

        // Effectuez la requête PUT
        ResultActions resultActions = mockMvc.perform(put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)));

        // Vérifiez le statut HTTP attendu
        resultActions.andExpect(status().isOk());

        // Vérifiez que la méthode update du service a été appelée une fois
        verify(sessionService, times(1)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Session.class));
    }


    @Test
    public void deleteSession() throws Exception {
        // Créez une Session de test
        Session session = new Session();
        session.setId(1L);

        // Configurez le comportement du service
        when(sessionService.getById(1L)).thenReturn(session);
        doNothing().when(sessionService).delete(1L);

        // Effectuez la requête DELETE
        ResultActions resultActions = mockMvc.perform(delete("/api/session/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifiez le statut HTTP attendu
        resultActions.andExpect(status().isOk());

        // Vérifiez que la méthode getById du service a été appelée une fois avec l'ID 1L
        verify(sessionService, times(1)).getById(1L);

        // Vérifiez que la méthode delete du service a été appelée une fois avec l'ID 1L
        verify(sessionService, times(1)).delete(1L);
    }


    @Test
    public void testParticipateSession() throws Exception {
        // Créez un objet SessionDto de test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Test Description");

        // Configurez le comportement du mapper
        when(sessionMapper.toEntity(sessionDto)).thenReturn(new Session()); // ou votre objet Session simulé

        // Configurez le comportement du service
        doNothing().when(sessionService).participate(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        // Effectuez la requête POST
        ResultActions resultActions = mockMvc.perform(post("/api/session/1/participate/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)));

        // Vérifiez le statut HTTP attendu
        resultActions.andExpect(status().isOk());

        // Vérifiez que la méthode participate du service a été appelée une fois
        verify(sessionService, times(1)).participate(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
    }

    @Test
    public void noLongerParticipate() throws Exception {
        // Créer un ID de session et d'utilisateur pour le test
        Long sessionId = 1L;
        Long userId = 2L;

        // Configurer le comportement du service
        doNothing().when(sessionService).noLongerParticipate(sessionId, userId);

        // Effectuer la requête DELETE
        ResultActions resultActions = mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier le statut HTTP attendu
        resultActions.andExpect(status().isOk());

        // Vérifier que la méthode noLongerParticipate du service a été appelée une fois
        verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
    }
}
