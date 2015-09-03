package com.ar0ne.service.test;

import com.ar0ne.model.Employee;
import com.ar0ne.service.EmployeeService;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-service-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class EmployeeServiceTest {

    private final static int EMPLOYEE_INIT_SIZE = 15;
    private final static String EMPLOYEE_SURNAME = "Surname";
    private final static String EMPLOYEE_NAME = "Name";
    private final static String EMPLOYEE_PATRONYMIC = "Patronymic";
    private final static long EMPLOYEE_SALARY = 100l;
    private final String BIG_TEXT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";


    @Autowired
    private EmployeeService employeeService;

    @Test
    public void getAllEmployees() {
        List<Employee> employeeList = employeeService.getAllEmployees();
        assertEquals(EMPLOYEE_INIT_SIZE, employeeList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullName() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, null, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataEmptyName() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, "", EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullSurname() {

        Employee employee = new Employee(1L, 1L, null, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataEmptySurname() {

        Employee employee = new Employee(1L, 1L, "", EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullPatronymic() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, null,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataEmptyPatronymic() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, "",
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullDateOfBirthday() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                null, EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataBigSurname() {

        Employee employee = new Employee(1L, 1L, BIG_TEXT, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataBigName() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, BIG_TEXT, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataBigPatronymic() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, BIG_TEXT,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNegativeSalary() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), -2l);

        Long id = employeeService.addEmployee(employee);
        assertNull(id);
    }

    @Test
    public void addEmployeesWithCorrectData() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        int size_before = employeeService.getAllEmployees().size();

        Long id = employeeService.addEmployee(employee);

        int size_after = employeeService.getAllEmployees().size();

        assertNotNull(id);
        assertEquals(size_after - 1, size_before);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataNullName() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setName(null);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataEmptyName() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setName("");
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataBigName() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setName(BIG_TEXT);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataNullSurname() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setSurname(null);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataEmptySurname() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setSurname("");
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataBigSurname() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setSurname(BIG_TEXT);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataNullPatr() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setPatronymic(null);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataEmptyPatr() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setPatronymic("");
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataBigPatronymic() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setPatronymic(BIG_TEXT);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataNullDoB() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setDateOfBirthday(null);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataWrongId() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setId(100000L);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateEmployeeWithIncorrectDataNegativeSalary() {

        Employee employee = employeeService.getEmployeeById(1L);
        employee.setSalary(-5l);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);
    }


    @Test
    public void updateEmployeeCorrectDate() {
        Employee employee = employeeService.getEmployeeById(1L);
        employee.setName(EMPLOYEE_NAME);
        employee.setPatronymic(EMPLOYEE_PATRONYMIC);
        employee.setSalary(EMPLOYEE_SALARY);
        employeeService.updateEmployee(employee);
        Employee ret_employee = employeeService.getEmployeeById(1L);
        assertEquals(employee, ret_employee);
    }


    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeWithWrongNegativeID() {
        employeeService.removeEmployee(-1L);
        assertEquals(EMPLOYEE_INIT_SIZE, employeeService.getAllEmployees().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeWithWrongPositiveID() {
        employeeService.removeEmployee(10000L);
        assertEquals(EMPLOYEE_INIT_SIZE, employeeService.getAllEmployees().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeWithWrongZeroID() {
        employeeService.removeEmployee(0L);
        assertEquals(EMPLOYEE_INIT_SIZE, employeeService.getAllEmployees().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeCorrectData() {
        employeeService.removeEmployee(1L);
        assertEquals(EMPLOYEE_INIT_SIZE - 1, employeeService.getAllEmployees().size());
        Employee employee = employeeService.getEmployeeById(1L);
        assertNull(employee);
    }

    @Test
    public void getEmployeeByIdCorrectData() {
        Employee employee = new Employee(1l, 1l, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1990-02-02"), EMPLOYEE_SALARY);

        Long id = employeeService.addEmployee(employee);
        assertNotNull(id);

        employee.setId(id);
        Employee ret_employee = employeeService.getEmployeeById(id);
        assertEquals(employee, ret_employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByIdWrongIdNegative() {
        Employee employee = employeeService.getEmployeeById(-1l);
        assertNull(employee);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByIdWrongIdPositive() {
        Employee employee = employeeService.getEmployeeById(10000l);
        assertNull(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByIdWrongIdZero() {
        Employee employee = employeeService.getEmployeeById(0l);
        assertNull(employee);
    }


    @Test
    public void getEmployeeByDoBCorrect() {
        List<Employee> employeeList = employeeService.getEmployeeByDateOfBirthday(new LocalDate("1990-04-05"));
        assertNotNull(employeeList);
        assertNotEquals(employeeList, new ArrayList<Employee>());
        assertTrue(employeeList.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByDoBIncorrect() {
        List<Employee> employeeList = employeeService.getEmployeeByDateOfBirthday(new LocalDate("9999-04-05"));
        assertNull(employeeList);
        assertEquals(employeeList, new ArrayList<Employee>());
        assertTrue(employeeList.size() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByDoBIncorrectNull() {
        List<Employee> employeeList = employeeService.getEmployeeByDateOfBirthday(null);
        assertNull(employeeList);
        assertEquals(employeeList, new ArrayList<Employee>());
        assertTrue(employeeList.size() == 0);
    }


    @Test
    public void getEmployeeBetweenTwoDatesCorrect() {
        LocalDate start = new LocalDate("1995-01-01");
        LocalDate end = new LocalDate("2005-01-01");
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(start, end);

        assertNotNull(employeeList);
        assertNotEquals(employeeList, new ArrayList<Employee>());

        for(Employee employee: employeeList) {
            assertNotNull(employee.getDateOfBirthday());
            assertTrue(start.isBefore(employee.getDateOfBirthday()));
            assertTrue(end.isAfter(employee.getDateOfBirthday()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesNull1() {
        LocalDate date = new LocalDate("1995-01-01");
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(date, null);

        assertNull(employeeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesNull2() {
        LocalDate date = new LocalDate("1995-01-01");
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(null, date);

        assertNull(employeeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesNull3() {
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(null, null);

        assertNull(employeeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesEndBeforeStart() {
        LocalDate start = new LocalDate("1995-01-01");
        LocalDate end = new LocalDate("2005-01-01");
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(end, start);

        assertNull(employeeList);
    }


}
