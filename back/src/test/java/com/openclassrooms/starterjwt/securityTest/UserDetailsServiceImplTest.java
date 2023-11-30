package com.openclassrooms.starterjwt.securityTest;

import com.openclassrooms.starterjwt.DataTest;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailsServiceImplTest {

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	@Mock
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void loadByUserNameTest() {
		//1 - Data Creation :
		User user = DataTest.generateUsers().get(0);
		user.setId(1L);

		UserDetailsImpl userDetails = UserDetailsImpl
				.builder()
				.id(user.getId())
				.username(user.getEmail())
				.lastName(user.getLastName())
				.firstName(user.getFirstName())
				.password(user.getPassword())
				.build();

		//2 - Data Processing : Find a person by its firstname and lastname
		when(userRepository.findByEmail(user.getEmail()))
				.thenReturn(Optional.of(user));

		UserDetailsImpl userFromDB = (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());

		//3 - Test : Test that the infos of the person are returned
		assertEquals(userDetails.getId(), userFromDB.getId());
		assertEquals(userDetails.getPassword(), userFromDB.getPassword());
		assertEquals(userDetails.getLastName(), userFromDB.getLastName());
		assertEquals(userDetails.getFirstName(), userFromDB.getFirstName());
		assertEquals(userDetails.getUsername(), userFromDB.getUsername());
	}


	@Test
	public void userNameNotFoundTest() {
		//1 - Data Creation :
		User user = DataTest.generateUsers().get(0);
		user.setId(1L);

		UserDetailsImpl userDetails = UserDetailsImpl
				.builder()
				.id(user.getId())
				.username(user.getEmail())
				.lastName(user.getLastName())
				.firstName(user.getFirstName())
				.password(user.getPassword())
				.build();

		//2 - Data Processing : Find a person by its firstname and lastname
		when(userRepository.findByEmail(user.getEmail()))
				.thenReturn(Optional.of(user));

		//3 - Test : Test that the infos of the person are returned
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("kara@mail.com"));

	}

}
