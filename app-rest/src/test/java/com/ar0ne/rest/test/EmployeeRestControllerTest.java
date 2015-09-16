package com.ar0ne.rest.test;

import com.ar0ne.model.Employee;
import com.ar0ne.model.LocalDateSerializer;
import com.ar0ne.rest.EmployeeRestController;
import com.ar0ne.service.EmployeeService;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
public class EmployeeRestControllerTest {

    private MockMvc mockMvc;

    @Resource
    EmployeeRestController employeeRestController;

    @Autowired
    EmployeeService employeeServiceMock;

    public final static int EMPL_INIT_SIZE = 15;

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

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        messageConverter.setObjectMapper(mapper);

        return messageConverter;
    }

    private static ObjectMapper createObjectMapperWithJacksonConverter() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        mapper.registerModule(simpleModule);
        return mapper;
    }


    private Employee createDemoEmployee() {
        return new Employee(1L, 1L, "Williamson", "Precious", "John", new LocalDate("1990-04-05"), 3500L);
    }

    private List<Employee> createDemoEmployees() {
        return new ArrayList<Employee>(
            Arrays.asList(
                new Employee(1, 1, "Williamson", "Precious", "John", new LocalDate("1990-04-05"), 3500),
                new Employee(3, 1, "Walsh", "Max", "John", new LocalDate("1991-09-12"), 2500),
                new Employee(4, 1, "Thomas", "Isaac", "Houston", new LocalDate("1995-07-07"), 1900),
                new Employee(5, 1, "Mike", "Gordon", "Iris", new LocalDate("1992-09-03"), 2000),

                new Employee(6, 2, "Ella", "Gutierrez", "Martin", new LocalDate("1990-04-01"), 1200),
                new Employee(7, 2, "Shiloh", "Acosta", "Coffey", new LocalDate("1990-03-10"), 1100),
                new Employee(8, 2, "Alessandro", "Mccullough", "Molly", new LocalDate("1989-11-13"), 2100),
                new Employee(9, 2, "Briana", "Green", "Immanuel", new LocalDate("1996-01-06"), 1250),
                new Employee(10, 2, "Kieran", "Mathis", "Yates", new LocalDate("1972-02-09"), 4300),

                new Employee(11, 3, "Elijah", "Marquez", "Brandon", new LocalDate("1988-05-02"), 1900),
                new Employee(12, 3, "Aniya", "Ballard", "Dario", new LocalDate("1994-05-06"), 2230),
                new Employee(13, 3, "Aurora", "Bright", "Guerra",new LocalDate( "1994-02-06"), 1890),

                new Employee(14, 4, "Zariah", "Marks", "Austin", new LocalDate("1990-03-09"), 1290),
                new Employee(15, 4, "Cordell", "Wilson", "Azul",new LocalDate( "1991-04-05"), 3200),
                new Employee(16, 1, "Williamson", "Precious", "John", new LocalDate("1990-04-05"), 3500)
            )
        );
    }

    @Test
    public void getEmployeeById() throws Exception {

        employeeServiceMock.getEmployeeById(1L);
        expectLastCall().andReturn(createDemoEmployee());
        replay(employeeServiceMock);

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        Employee employee = createDemoEmployee();

        this.mockMvc.perform(
            get("/employee/id/" + 1)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(employee)));

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

    @Test
    public void getAllEmployees() throws  Exception {

        employeeServiceMock.getAllEmployees();
        expectLastCall().andReturn(createDemoEmployees());
        replay(employeeServiceMock);

        List<Employee> employees = createDemoEmployees();

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        this.mockMvc.perform(
            get("/employee/")
                .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(employees)));
    }



    @Test
    public void addEmployeeCorrectData()  throws Exception {

        employeeServiceMock.addEmployee(createDemoEmployee());
        expectLastCall().andReturn(EMPL_INIT_SIZE + 1);

        replay(employeeServiceMock);

        Employee employee = createDemoEmployee();

        this.mockMvc.perform(
            post("/employee/create")
                .param("name", employee.getName())
                .param("surname", employee.getSurname())
                .param("patronymic", employee.getPatronymic())
                .param("department_id", String.valueOf(employee.getDepartmentId()))
                .param("salary", String.valueOf(employee.getSalary()))
                .param("id", String.valueOf(employee.getId()))
                .param("date_of_birthday", employee.getDateOfBirthday().toString())
            )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(EMPL_INIT_SIZE + 1)));

    }

    @Test
    public void addEmployeeIncorrectData()  throws Exception {

        Employee employee = createDemoEmployee();
        employee.setName(null);

        employeeServiceMock.addEmployee(employee);
        expectLastCall().andThrow(new IllegalArgumentException());
        replay(employeeServiceMock);

        this.mockMvc.perform(
                post("/employee/create")
                        .param("name", employee.getName())
                        .param("surname", employee.getSurname())
                        .param("patronymic", employee.getPatronymic())
                        .param("department_id", String.valueOf(employee.getDepartmentId()))
                        .param("salary", String.valueOf(employee.getSalary()))
                        .param("id", String.valueOf(employee.getId()))
                        .param("date_of_birthday", employee.getDateOfBirthday().toString())
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void removeEmployee() throws Exception {

        employeeServiceMock.removeEmployee(2L);
        expectLastCall();
        replay(employeeServiceMock);

        this.mockMvc.perform(
            delete("/employee/delete/" + 2)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("\"Deleted\""));
    }

    @Test
    public void updateEmployee() throws Exception {

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        Employee employee = createDemoEmployee();

        String json = mapper.writeValueAsString(employee);

        employeeServiceMock.updateEmployee(employee);
        expectLastCall();
        replay(employeeServiceMock);

        this.mockMvc.perform(
            post("/employee/update")
                .param("name", employee.getName())
                .param("surname", employee.getSurname())
                .param("patronymic", employee.getPatronymic())
                .param("department_id", String.valueOf(employee.getDepartmentId()))
                .param("salary", String.valueOf(employee.getSalary()))
                .param("id", String.valueOf(employee.getId()))
                .param("date_of_birthday", employee.getDateOfBirthday().toString())
        )
            .andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    public void updateEmployeeWithIncorrectData() throws Exception {

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        Employee employee = createDemoEmployee();
        employee.setName(null);

        String json = mapper.writeValueAsString(employee);

        employeeServiceMock.updateEmployee(employee);
        expectLastCall();
        replay(employeeServiceMock);

        this.mockMvc.perform(
                post("/employee/update")
                        .param("name", employee.getName())
                        .param("surname", employee.getSurname())
                        .param("patronymic", employee.getPatronymic())
                        .param("department_id", String.valueOf(employee.getDepartmentId()))
                        .param("salary", String.valueOf(employee.getSalary()))
                        .param("id", String.valueOf(employee.getId()))
                        .param("date_of_birthday", employee.getDateOfBirthday().toString())
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getEmployeesByDateOfBirthday() throws  Exception {

        Employee employee = new Employee (10l, 2l, "Kieran", "Mathis", "Yates", new LocalDate("1972-02-09"), 4300l);
        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(employee);

        employeeServiceMock.getEmployeeByDateOfBirthday(employee.getDateOfBirthday());
        expectLastCall().andReturn(employeeList);
        replay(employeeServiceMock);

        this.mockMvc.perform(
                get("/employee/date/" + employee.getDateOfBirthday().toString())
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(employeeList)));


    }

    @Test
    public void getEmployeesByDateOfBirthdayWithWrongDate() throws  Exception {

        employeeServiceMock.getEmployeeByDateOfBirthday(new LocalDate("9999-09-09"));
        expectLastCall().andThrow(new IllegalArgumentException("Employee can't be empty", null));

        replay(employeeServiceMock);

        this.mockMvc.perform(
            get("/employee/date/"  + "9999-09-09" )
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string("\"Employee can't be empty\""));
    }


    @Test
    public void getEmployeeBetweenDoB() throws Exception {

        Employee employee = new Employee (10l, 2l, "Kieran", "Mathis", "Yates", new LocalDate("1972-02-09"), 4300l);
        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        employeeServiceMock.getEmployeeBetweenDatesOfBirtday(new LocalDate("1972-02-09"), new LocalDate("1972-03-09"));

        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(employee);

        expectLastCall().andReturn(employeeList);
        replay(employeeServiceMock);

        this.mockMvc.perform(
            get("/employee/date/" + "1972-02-09" + "/" + "1972-03-09")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(employeeList)));

    }



}


