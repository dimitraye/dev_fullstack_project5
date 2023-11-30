package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SessionServiceTest {
	
	@InjectMocks
	private SessionService sessionService;

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}


	@Test
	public void deleteTest() {
		//2 - Data Processing : Delete the person
		sessionService.delete(1L);

		//3 - Test : Verify that the person has been deleted correctly
		verify(sessionRepository, times(1)).deleteById(1L);
	}



	@Test
	public void findByIdTest() {
		//1 - Data Processing  : find the firestation by entering its address
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		Session sessionFromDB = sessionService.getById(1L);

		//2 - Test : test that the correct firestation is returned when we enter its address
		assertEquals(session.getName(), sessionFromDB.getName());
		assertEquals(session.getDescription(), sessionFromDB.getDescription());

	}


	@Test
	public void findAll(){
		//1 - Creation Data :
		List<Session> list = DataTest.generateSessions();

		//2 - Data Processing :
		when(sessionRepository.findAll()).thenReturn(list);

		//3 - Test :
		List<Session> sessions = sessionService.findAll();

		assertEquals(3, sessions.size());
		verify(sessionRepository, times(1)).findAll();
	}


	@Test
	public void createTest() {
		//1 - Data Creation :
		Session session = DataTest.session();

		//2 - Data Processing : Save the person
		sessionService.create(session);

		//3 - Test : Verify that the person has been saved correctly
		verify(sessionRepository, times(1)).save(session);
	}


	@Test
	void shouldUpdate() {

		//1 - Data Creation :
		Session session = DataTest.session();

		//2 - Data Processing : Update the person
		sessionService.update(1L, session);

		//3 - Test : Verify that the person has been updated correctly
		verify(sessionRepository, times(1)).save(session);
	}


	@Test
	public void participateIfOk() {
		//1 -récup session
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		//2 - récup user
		User user1 = DataTest.user1();
		user1.setId(1L);
		Optional<User> optionalUser1 = Optional.of(user1);
		when(userRepository.findById(1L)).thenReturn(optionalUser1);

		List<User> sessionUsers = DataTest.generateUsers();
		AtomicInteger i = new AtomicInteger(2);

		sessionUsers.forEach(user -> user.setId((long) i.getAndIncrement()));
		session.setUsers(sessionUsers);
		//3 - Save the person
		when(sessionRepository.save(session)).thenReturn(session);

		sessionService.participate(1L, 1L);


		//4 - Test : test that the correct firestation is returned when we enter its address
		verify(sessionRepository, times(1)).findById(1L);
		verify(userRepository, times(1)).findById(1L);
		verify(sessionRepository, times(1)).save(session);

	}

	@Test
	public void alreadyParticipate() {
		//1 -récup session
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		//2 - récup user
		User user1 = DataTest.user1();
		user1.setId(1L);
		Optional<User> optionalUser1 = Optional.of(user1);
		when(userRepository.findById(1L)).thenReturn(optionalUser1);

		List<User> sessionUsers = DataTest.generateUsers();
		AtomicInteger i = new AtomicInteger(1);

		sessionUsers.forEach(user -> user.setId((long) (i.getAndIncrement())));
		session.setUsers(sessionUsers);
		//3 - Save the person
		when(sessionRepository.save(session)).thenReturn(session);

		assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
	}


	@Test
	public void participateShouldThrowNotFound() {
		//1 -récup session
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		//2 - récup user
		User user1 = DataTest.user1();
		user1.setId(1L);
		Optional<User> optionalUser1 = Optional.of(user1);
		when(userRepository.findById(1L)).thenReturn(optionalUser1);

		List<User> sessionUsers = DataTest.generateUsers();
		AtomicInteger i = new AtomicInteger(2);

		sessionUsers.forEach(user -> user.setId((long) (i.getAndIncrement())));
		session.setUsers(sessionUsers);
		//3 - Save the person
		when(sessionRepository.save(session)).thenReturn(session);

		assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 7L));
	}


	@Test
	public void noLongerParticipateIfOk() {
		//1 -récup session
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		List<User> sessionUsers = DataTest.generateUsers();
		AtomicInteger i = new AtomicInteger(1);

		sessionUsers.forEach(user -> user.setId((long) (i.getAndIncrement())));
		session.setUsers(sessionUsers);
		//3 - Save the person
		when(sessionRepository.save(session)).thenReturn(session);
		//4 - quit the participate
		sessionService.noLongerParticipate(1L, 1L);

		//5 - Test : test that the correct firestation is returned when we enter its address
		verify(sessionRepository, times(1)).findById(1L);
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	public void alreadyNoLongerParticipate() {
		//1 -récup session
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		List<User> sessionUsers = DataTest.generateUsers();
		AtomicInteger i = new AtomicInteger(1);

		sessionUsers.forEach(user -> user.setId((long) (i.getAndIncrement())));
		session.setUsers(sessionUsers);
		//3 - Save the person
		when(sessionRepository.save(session)).thenReturn(session);

		assertThrows(BadRequestException.class, () -> sessionService
				.noLongerParticipate(1L, 6L));
	}

	@Test
	public void noLongerParticipateShouldThrowNotFound() {
		//1 -récup session
		Session session = DataTest.session();
		session.setId(1L);
		Optional<Session> sessionOptional = Optional.of(session);
		when(sessionRepository.findById(1L)).thenReturn(sessionOptional);

		List<User> sessionUsers = DataTest.generateUsers();
		AtomicInteger i = new AtomicInteger(1);

		sessionUsers.forEach(user -> user.setId((long) (i.getAndIncrement())));
		session.setUsers(sessionUsers);
		//3 - Save the person
		when(sessionRepository.save(session)).thenReturn(session);

		assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(2L, 1L));
	}


}
