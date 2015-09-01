package com.ar0ne.rest.test;

import com.ar0ne.model.Employee;
import com.ar0ne.model.LocalDateDeserializer;
import com.ar0ne.model.LocalDateSerializer;
import com.ar0ne.rest.EmployeeRestController;
import com.ar0ne.service.EmployeeService;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.easymock.EasyMock;
import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
public class EmployeeRestControllerTest {

    private MockMvc mockMvc;

    @Resource
    EmployeeRestController employeeRestController;

    @Autowired
    EmployeeService employeeServiceMock;

    @After
    public void down(){
        reset(employeeServiceMock);
    }

    @Before
    public void setUp() {
        this.mockMvc = standaloneSetup(employeeRestController).
                setMessageConverters(createJacksonMessageConverter()).build();
    }

    private static MappingJackson2HttpMessageConverter createJacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        mapper.registerModule(simpleModule);

        messageConverter.setObjectMapper(mapper);

        return messageConverter;
    }


    private Employee createDemoEmployee() {
        return new Employee(1L, 1L, "Williamson", "Precious", "John", new LocalDate("1990-04-05"), 3500L);

    }

    public static final String EMPLOYEE_ID_1_JSON = "{\"id\":1,\"surname\":\"Williamson\",\"name\":\"Precious\",\"patronymic\":\"John\",\"salary\":3500,\"date_of_birthday\":\"1990-04-05\",\"department_id\":1}";

    @Test
    public void getEmployeeById() throws Exception {

        employeeServiceMock.getEmployeeById(1L);
        expectLastCall().andReturn(createDemoEmployee());
        replay(employeeServiceMock);

        this.mockMvc.perform(
                get("/employee/id/" + 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(EMPLOYEE_ID_1_JSON));

    }


    @Test
    public void getEmployeeByIdWithIncorrectId()  throws Exception{

        employeeServiceMock.getEmployeeById(20000L);
        expectLastCall().andThrow(new IllegalArgumentException("Employee not found for id=20000, error:Employee with this ID doesn't exist",null));

        replay(employeeServiceMock);

        this.mockMvc.perform(
                get("/employee/id/"  + 20000)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Employee not found for id=20000, error:Employee with this ID doesn't exist\""));

    }


    @Test
    public void getEmployeeByIdWithNegativeId()  throws Exception{

        employeeServiceMock.getEmployeeById(-2L);
        expectLastCall().andThrow(new IllegalArgumentException("Employee not found for id=-2, error:Employee with this ID doesn't exist",null));

        replay(employeeServiceMock);

        this.mockMvc.perform(
                get("/employee/id/"  + "-2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Employee not found for id=-2, error:Employee with this ID doesn't exist\""));

    }

    @Test
    public void getEmployeeByIdWithZeroId()  throws Exception{

        employeeServiceMock.getEmployeeById(0);
        expectLastCall().andThrow(new IllegalArgumentException("Employee not found for id=0, error:Employee with this ID doesn't exist",null));

        replay(employeeServiceMock);

        this.mockMvc.perform(
                get("/employee/id/"  + 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Employee not found for id=0, error:Employee with this ID doesn't exist\""));

    }

//    @Test
//    public void addEmployee()  throws Exception {
//
//        employeeServiceMock.addEmployee(createDemoEmployee());
//        expectLastCall().andReturn(1L);
//
//        replay(employeeServiceMock);
//
//        Employee employee = createDemoEmployee();

//        ObjectMapper mapper = new ObjectMapper();
//
//        SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
//        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
//        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
//        mapper.registerModule(simpleModule);
//
//        String json = mapper.writeValueAsString(employee);
//        System.out.println(json);

//        this.mockMvc.perform(
//                post("/employee/create")
////                        .content(json)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .accept(MediaType.APPLICATION_JSON))
//                        .param("name", employee.getName())
//                        .param("surname", employee.getSurname())
//                        .param("patronymic", employee.getPatronymic())
//                        .param("department_id", employee.getDepartmentId())
////
//                        .andDo(print())
//                        .andExpect(status().isCreated())
//                        .andExpect(content().string("1"));
//
//    }
//




}


