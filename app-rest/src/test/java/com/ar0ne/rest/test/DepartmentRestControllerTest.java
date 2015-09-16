package com.ar0ne.rest.test;

import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import com.ar0ne.model.LocalDateSerializer;
import com.ar0ne.rest.DepartmentRestController;
import com.ar0ne.service.DepartmentService;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-rest-mock-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
public class DepartmentRestControllerTest {

    private MockMvc mockMvc;

    @Resource
    DepartmentRestController departmentRestController;

    @Autowired
    DepartmentService departmentServiceMock;

    @After
    public void down(){
        reset(departmentServiceMock);
    }

    @Before
    public void setUp() {
        this.mockMvc = standaloneSetup(departmentRestController).
                setMessageConverters(createJacksonMessageConverter()).build();
    }

    /**
     * Create message converters for mock tests
     * @return MappingJackson2HttpMessageConverter with custom object mapper
     */
    private static MappingJackson2HttpMessageConverter createJacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        messageConverter.setObjectMapper(mapper);

        return messageConverter;
    }

    /**
     * Create object mapper with custom org.joda.time.LocalDate serializer from org.ar0ne.model.LocalDateSerializer
     * @return object mapper with serializer for org.joda.time.LocalDate;
     */
    private static ObjectMapper createObjectMapperWithJacksonConverter() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        mapper.registerModule(simpleModule);
        return mapper;
    }

    private static Department createDeparment() {
        Department department = new Department("Department of Energy", 1L);
        List<Employee> employees = new ArrayList<Employee>(
            Arrays.asList(
                new Employee(1, 1, "Williamson", "Precious", "John", new LocalDate("1990-04-05"), 3500),
                new Employee(2, 1, "Ibarra", "Helen", "Lee", new LocalDate("1987-01-15"), 1500),
                new Employee(3, 1, "Walsh", "Max", "John", new LocalDate("1991-09-12"), 2500),
                new Employee(4, 1, "Thomas", "Isaac", "Houston", new LocalDate("1995-07-07"), 1900),
                new Employee(5, 1, "Mike", "Gordon", "Iris", new LocalDate("1992-09-03"), 2000)
            )
        );
        department.setEmployees(employees);
        return department;
    }

    /**
     * Test getDepartmentById: check equals results from service and REST service
     * @throws Exception
     */
    @Test
    public void getDepartmentById() throws Exception {

        departmentServiceMock.getDepartmentById(1L);
        expectLastCall().andReturn(createDeparment());
        replay(departmentServiceMock);

        Department department = createDeparment();

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        this.mockMvc.perform(
                get("/department/id/" + 1)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(department)));

    }

    /**
     * Test getDepartmentByIdWithIncorrectId: check equals results from service and REST service with incorrect data
     * @throws Exception
     */
    @Test
    public void getDepartmentByIdWithIncorrectId() throws Exception {
        departmentServiceMock.getDepartmentById(20000L);
        expectLastCall().andThrow(new IllegalArgumentException("Department not found for id=20000, error:Department can't be NULL",null));

        replay(departmentServiceMock);

        this.mockMvc.perform(
            get("/department/id/"  + 20000)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().string("\"Department not found for id=20000, error:Department can't be NULL\""));

    }

    /**
     * Test getDepartmentByIdWithIncorrectIdNegativeId: check equals results from service and REST service with incorrect data
     * @throws Exception
     */
    @Test
    public void getDepartmentByIdWithIncorrectIdNegativeId() throws Exception {
        departmentServiceMock.getDepartmentById(-2L);
        expectLastCall().andThrow(new IllegalArgumentException("Department not found for id=-2, error:Department can't be NULL",null));

        replay(departmentServiceMock);

        this.mockMvc.perform(
                get("/department/id/"  + -2)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Department not found for id=-2, error:Department can't be NULL\""));

    }

    /**
     * Test getDepartmentByIdWithIncorrectIdZeroId: check equals results from service and REST service with incorrect data
     * @throws Exception
     */
    @Test
    public void getDepartmentByIdWithIncorrectIdZeroId() throws Exception {
        departmentServiceMock.getDepartmentById(0L);
        expectLastCall().andThrow(new IllegalArgumentException("Department not found for id=0, error:Department can't be NULL",null));

        replay(departmentServiceMock);

        this.mockMvc.perform(
                get("/department/id/"  + 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"Department not found for id=0, error:Department can't be NULL\""));

    }

    /**
     * Test addDepartmentWithIncorrectData: check equals results from service and REST service with incorrect data
     * @throws Exception
     */
    @Test
    public void addDepartmentWithIncorrectData() throws Exception {

        departmentServiceMock.addDepartment(new Department(null, 1L));
        expectLastCall().andThrow(new IllegalArgumentException());
        replay(departmentServiceMock);

        this.mockMvc.perform(
            post("/department/create")
                .param("name", "")
            )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    /**
     * Test getDepartmentByName: check equals results from service and REST service
     * @throws Exception
     */
    @Test
    public void getDepartmentByName() throws Exception {

        departmentServiceMock.getDepartmentByName("Department of Energy");
        expectLastCall().andReturn(createDeparment());

        replay(departmentServiceMock);

        Department department = createDeparment();

        ObjectMapper mapper = createObjectMapperWithJacksonConverter();

        this.mockMvc.perform(
            get("/department/name/" + "Department of Energy")
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(department)));

    }

    @Test
    public void getDepartmentByNameWithIncorrectName() throws Exception {
        departmentServiceMock.getDepartmentByName("NOT EXISTED");
        expectLastCall().andThrow(new IllegalArgumentException("", null));

        replay(departmentServiceMock);

        this.mockMvc.perform(
            get("/department/name/")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(content().string(""));

    }

//    @Test
//    public void removeDepartmentById() throws Exception {
//
//        departmentServiceMock.removeDepartment(1L);
//        expectLastCall();
//        replay(departmentServiceMock);
//
//        this.mockMvc.perform(
//            delete("/department/delete/" + 1)
//        )
//            .andDo(print())
//            .andExpect(status().isOk());
//
//    }

    @Test
    public void removeDepartmentByIncorrectId() throws Exception {

        departmentServiceMock.removeDepartment(100000L);
        expectLastCall();
        replay(departmentServiceMock);

        this.mockMvc.perform(
              delete("/department/delete/" + 100000)
            )
            .andDo(print())
            .andExpect(status().isNotFound());
    }



    @Test
    public void updateDepartment() throws Exception {

        Department department = createDeparment();

        departmentServiceMock.updateDepartment(department);
        expectLastCall();
        replay(departmentServiceMock);

        String employeesJson = createObjectMapperWithJacksonConverter().writeValueAsString(department.getEmployees());

        this.mockMvc.perform(
            post("/department/update/")
                .param("id", String.valueOf(department.getId()))
                .param("name", department.getName())
                .param("employees", employeesJson)
        )
            .andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    public void updateDepartmentWithIncorrectData() throws Exception {

        Department department = createDeparment();
        department.setName(null);

        departmentServiceMock.updateDepartment(department);
        expectLastCall();
        replay(departmentServiceMock);

        this.mockMvc.perform(
            post("/department/update")
                .param("id", String.valueOf(department.getId()))
                .param("name", department.getName())
        )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }





}
