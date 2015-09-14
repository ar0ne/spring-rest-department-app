package com.ar0ne.dao.test;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
@Transactional(transactionManager="transactionManager")
public class EmployeeDaoTest {

    private final static int EMPL_INIT_SIZE = 15;

    @Autowired
    private EmployeeDao employeeDao;

    private Employee createAndAddToDataBaseEmployee() {
        Employee employee = new Employee(1L, 1L, "Surname", "Name", "Patronymic",
                new LocalDate("1920-02-02"), 1000L);

        long id = employeeDao.addEmployee(employee);
        employee.setId(id);

        return employee;
    }

    /**
     * Test getAllEmployees: check count of departments before and after delete
     */
    @Test
    public void getAllEmployees() {
        List<Employee> employeeList = employeeDao.getAllEmployees();
        assertEquals(EMPL_INIT_SIZE, employeeList.size());

        employeeDao.removeEmployee(1L);
        assertEquals(EMPL_INIT_SIZE - 1, employeeDao.getAllEmployees().size());
    }

    /**
     * Test getEmployeeById: Check equals of departments
     */
    @Test
    public void getEmployeeById() {
        Employee employee = createAndAddToDataBaseEmployee();

        Employee ret_employee = employeeDao.getEmployeeById(employee.getId());

        assertNotNull(ret_employee);
        assertEquals(employee, ret_employee);
    }

    /**
     * Test addEmployee: check count of departments before and after add
     */
    @Test
    public void addEmployee() {

        int size_before = employeeDao.getAllEmployees().size();

        Employee employee = createAndAddToDataBaseEmployee();

        assertNotNull(employee);

        int size_after  = employeeDao.getAllEmployees().size();

        assertEquals(size_before + 1, size_after);
        assertEquals(EMPL_INIT_SIZE, size_before);
    }

    /**
     * Test removeEmployee: check count of departments before and after delete
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void removeEmployee() {

        Employee employee = createAndAddToDataBaseEmployee();

        int size_before = employeeDao.getAllEmployees().size();

        employeeDao.removeEmployee(employee.getId());

        int size_after = employeeDao.getAllEmployees().size();

        assertEquals(size_before - 1, size_after);

        employee = employeeDao.getEmployeeById(employee.getId());

        assertNull(employee);
    }

    /**
     * Test updateDepartment: check equals of department name before and after update
     */
    @Test
    public void updateDepartment() {

        final String new_name = "Other name";

        Employee employee = createAndAddToDataBaseEmployee();

        employee.setName(new_name);

        employeeDao.updateEmployee(employee);

        Employee ret_employee = employeeDao.getEmployeeById(employee.getId());

        assertEquals(ret_employee.getName(), new_name);

    }

    /**
     * Test getEmployeesByDateOfBirthday: check count of departments for different intervals of dateOfBirtday
     */
    @Test
    public void getEmployeesByDateOfBirthday() {

        LocalDate date_x = new LocalDate("2000-10-10");

        List<Employee> employees = employeeDao.getEmployeeByDateOfBirthday(date_x);

        assertEquals(employees, new ArrayList<Employee>());

        Employee employee = new Employee(1L, 1L, "Surname", "Name", "Patronymic", date_x, 1000L);

        Long id = employeeDao.addEmployee(employee);
        assertNotNull(id);

        employees = employeeDao.getEmployeeByDateOfBirthday(date_x);
        assertNotNull(employees);
        assertNotEquals(employees, new ArrayList<Employee>());
        assertEquals(employees.size(), 1);

        assertEquals(employees.get(0).getDateOfBirthday(), date_x);
    }

    /**
     * Test getEmployeesByDateOfBirthdayFewRow: check count of departments for different intervals of dateOfBirtday
     */
    @Test
    public void getEmployeesByDateOfBirthdayFewRow() {

        LocalDate date_x = new LocalDate("2000-10-10");

        int size = 5;

        List<Employee> employees = employeeDao.getEmployeeByDateOfBirthday(date_x);
        assertNotNull(employees);
        assertEquals(employees, new ArrayList<Employee>());

        for(int i = 0; i < size; i++) {
            Employee employee = new Employee(1L, 1L, "Surname", "Name", "Patronymic", date_x, 1000L);
            Long id = employeeDao.addEmployee(employee);
            assertNotNull(id);
        }

        employees = employeeDao.getEmployeeByDateOfBirthday(date_x);
        assertNotNull(employees);
        assertNotEquals(employees, new ArrayList<Employee>());
        assertEquals(employees.size(), size);

        for(int i = 0; i< size; i++) {
            assertEquals(employees.get(i).getDateOfBirthday(), date_x);
        }
    }

    /**
     * Test getEmployeeBetweenDatesOfBirthday: check count of departments for different intervals of dateOfBirtday
     */
    @Test
    public void getEmployeeBetweenDatesOfBirthday() {

        LocalDate start = new LocalDate("1980-01-01");
        LocalDate end = new LocalDate("1992-01-01");

        int size = 9;

        List<Employee> employees = employeeDao.getEmployeeBetweenDatesOfBirtday(start, end);
        assertNotNull(employees);
        assertNotEquals(employees, new ArrayList<Employee>());
        assertEquals(size, employees.size());

        for(Employee empl: employees) {
            assertTrue(start.isBefore(empl.getDateOfBirthday()));
            assertTrue(end.isAfter(empl.getDateOfBirthday()));
        }

        Employee employee = new Employee(1L, 1L, "Surname", "Name", "Patronymic", new LocalDate("1985-01-01"), 1000L);
        Long id = employeeDao.addEmployee(employee);
        assertNotNull(id);

        employees = employeeDao.getEmployeeBetweenDatesOfBirtday(start, end);
        assertNotNull(employees);
        assertNotEquals(employees, new ArrayList<Employee>());
        assertEquals(size + 1, employees.size());

        for(Employee empl: employees) {
            assertTrue(start.isBefore(empl.getDateOfBirthday()));
            assertTrue(end.isAfter(empl.getDateOfBirthday()));
        }

    }



}
