package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}


	@Test
	public void deleteTest() {
		//1 - Data Creation :
		User user = DataTest.user1();
		user.setId(1L);

		//2 - Data Processing : Delete the person
		userService.delete(user.getId());

		//3 - Test : Verify that the person has been deleted correctly
		verify(userRepository, times(1)).deleteById(user.getId());
	}

	@Test
	public void findByIdTest() {
		//1 - Data Processing  : find the firestation by entering its address
		User user1 = DataTest.user1();
		user1.setId(1L);
		Optional<User> optionalUser1 = Optional.of(user1);
		when(userRepository.findById(1L)).thenReturn(optionalUser1);

		User userFromDB = userService.findById(1L);

		//2 - Test : test that the correct firestation is returned when we enter its address
		assertEquals(user1.getFirstName(), userFromDB.getFirstName());
		assertEquals(user1.getLastName(), userFromDB.getLastName());

	}

}
