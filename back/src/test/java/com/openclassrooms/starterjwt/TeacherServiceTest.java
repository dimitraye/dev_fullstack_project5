package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeacherServiceTest {

	@InjectMocks
	private  TeacherService teacherService;

	@Mock
	private TeacherRepository teacherRepository;

	@Test
	public void contextLoads() {
	}


	@Test
	public void findAll(){
		//1 - Creation Data :
		List<Teacher> list = DataTest.createSampleTeachers();


		//2 - Data Processing :
		when(teacherRepository.findAll()).thenReturn(list);

		//3 - Test :
		List<Teacher> teachers = teacherService.findAll();

		assertEquals(5, teachers.size());
		verify(teacherRepository, times(1)).findAll();
	}

	@Test
	public void findByIdTest() {
		//1 - Data Processing  : find the firestation by entering its address
		Teacher teacher1 = DataTest.createSampleTeachers().get(0);
		teacher1.setId(1L);
		Optional<Teacher> optionalTeacher1 = Optional.of(teacher1);
		when(teacherRepository.findById(1L)).thenReturn(optionalTeacher1);

		Teacher teacherFromDB = teacherService.findById(1L);

		//2 - Test : test that the correct firestation is returned when we enter its address
		assertEquals(teacher1.getFirstName(), teacherFromDB.getFirstName());
		assertEquals(teacher1.getLastName(), teacherFromDB.getLastName());

	}

}
