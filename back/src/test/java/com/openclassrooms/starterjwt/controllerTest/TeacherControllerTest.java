package com.openclassrooms.starterjwt.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.DataTest;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebSecurityConfig.class)
@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TeacherControllerTest {

	@MockBean
	private  TeacherService teacherService;

	@MockBean
	private TeacherMapper teacherMapper;

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
		//1 - Creation Data :
		List<Teacher> list = DataTest.createSampleTeachers();

		//2 - Data Processing :
		when(teacherService.findAll()).thenReturn(list);

		mockMvc.perform(get("/api/teacher"))
				.andExpect(status().isOk())
				.andDo(print());
	}

	/*@Test
	void shouldFindByFirestationStation() throws Exception {
		//1 - Create data : create a container that will contain all thes persons separated in 2 sets, one for the adults, and the other for the children
		// + create 3 persons, 2 adults and one child
		// + create a set
		ContainerPersonDTO containerPersonDTO = new ContainerPersonDTO();

		Person person1 = DataTest.getPerson1();
		person1.setMedicalRecord(DataTest.getMedicalRecord1());
		person1.setFirestation(DataTest.getFirestation1());
		Person person2 = DataTest.getPerson2();
		person2.setMedicalRecord(DataTest.getMedicalRecord2());
		person2.setFirestation(DataTest.getFirestation1());
		Person person3 = DataTest.getPerson3();
		person3.setMedicalRecord(DataTest.getChildMedicalRecord());
		person3.setFirestation(DataTest.getFirestation1());

		Set<Person> persons = Set.of(person1, person2, person3);
		Set<PersonDTO> personDTOS = persons.stream().map(person -> new PersonDTO()).collect(Collectors.toSet());
		containerPersonDTO.setPersons(personDTOS);
		containerPersonDTO.setChildrenNumber(1);
		containerPersonDTO.setAdultNumber(2);

		int stationNumber = person1.getFirestation().getStation();
		//2 - Test : test that a list of person is that belongs to the same station is returned
		when(personService.findByFirestationStation(stationNumber)).thenReturn(containerPersonDTO);
		mockMvc.perform(get("/firestation").param("stationNumber", String.valueOf(stationNumber)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.persons.size()").value(personDTOS.size()))
				.andDo(print());
	}*/


	@Test
	void shouldReturnTeacherById() throws Exception {
		Teacher teacher = DataTest.createSampleTeachers().get(0);
		teacher.setId(1L);


		when(teacherService.findById(teacher.getId())).thenReturn(teacher);

		mockMvc.perform(get("/api/teacher/{id}",teacher.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}


}
