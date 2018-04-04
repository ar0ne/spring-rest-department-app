package com.ar0ne.service.impl;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;


import java.util.ArrayList;
import java.util.List;

/**
 * @TODO: redo this dumb test cases
 */
public class EmployeeServiceTest {

    private final static int EMPLOYEE_INIT_SIZE = 15;
    private final static String EMPLOYEE_SURNAME = "Surname";
    private final static String EMPLOYEE_NAME = "Name";
    private final static String EMPLOYEE_PATRONYMIC = "Patronymic";
    private final static long EMPLOYEE_SALARY = 100L;
    private final static long NEGATIVE_LONG = -2L;
    private final static long BIG_LONG = 20000000L;
    private final static long ZERO_LONG = 0L;
    private final static String BIG_TEXT = new String(new char[400]).replace('\0', 'A');

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeDao employeeDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks( this );
        Mockito.when( employeeDao.getEmployeeById(0L) ).thenThrow( new EmptyResultDataAccessException(0) );
        Mockito.when( employeeDao.getEmployeeById(BIG_LONG) ).thenThrow( new EmptyResultDataAccessException(0) );
        Mockito.when( employeeDao.getEmployeeById(NEGATIVE_LONG) ).thenThrow( new EmptyResultDataAccessException(0) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullName() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, null, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataEmptyName() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, "", EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullSurname() {

        Employee employee = new Employee(1L, 1L, null, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataEmptySurname() {

        Employee employee = new Employee(1L, 1L, "", EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullPatronymic() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, null,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataEmptyPatronymic() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, "",
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNullDateOfBirthday() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                null, EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataBigSurname() {

        Employee employee = new Employee(1L, 1L, BIG_TEXT, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataBigName() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, BIG_TEXT, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataBigPatronymic() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, BIG_TEXT,
                new LocalDate("1980-02-02"), EMPLOYEE_SALARY);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmployeesWithIncorrectDataNegativeSalary() {

        Employee employee = new Employee(1L, 1L, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC,
                new LocalDate("1980-02-02"), NEGATIVE_LONG);

        employeeService.addEmployee(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeWithWrongNegativeID() {
        employeeService.removeEmployee(NEGATIVE_LONG);
        Assert.assertEquals(EMPLOYEE_INIT_SIZE, employeeService.getAllEmployees().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeWithWrongPositiveID() {
        employeeService.removeEmployee(BIG_LONG);
        Assert.assertEquals(EMPLOYEE_INIT_SIZE, employeeService.getAllEmployees().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeWithWrongZeroID() {
        employeeService.removeEmployee(ZERO_LONG);
        Assert.assertEquals(EMPLOYEE_INIT_SIZE, employeeService.getAllEmployees().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmployeeCorrectData() {
        employeeService.removeEmployee(1L);
        Assert.assertEquals(EMPLOYEE_INIT_SIZE - 1, employeeService.getAllEmployees().size());
        Employee employee = employeeService.getEmployeeById(1L);
        Assert.assertNull(employee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByIdWrongIdNegative() {
        employeeService.getEmployeeById(NEGATIVE_LONG);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByIdWrongIdPositive() {
        employeeService.getEmployeeById(BIG_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByIdWrongIdZero() {
        employeeService.getEmployeeById(ZERO_LONG);
    }


    @Test
    public void getEmployeeByDoBCorrect() {
        final LocalDate localDate = new LocalDate( "1990-04-05" );
        Mockito.when( employeeDao.getEmployeeByDateOfBirthday( Mockito.eq( localDate ) ) )
          .thenReturn( new ArrayList<>() );

        final List<Employee> employeeList = employeeService.getEmployeeByDateOfBirthday(localDate);

        Assert.assertNotNull(employeeList);
        Assert.assertTrue(employeeList.size() == 0);

        Mockito.verify( employeeDao ).getEmployeeByDateOfBirthday( Mockito.eq( localDate ) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeByDoBIncorrectNull() {
        employeeService.getEmployeeByDateOfBirthday(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesNull1() {
        LocalDate date = new LocalDate("1995-01-01");
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(date, null);

        Assert.assertNull(employeeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesNull2() {
        LocalDate date = new LocalDate("1995-01-01");
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(null, date);

        Assert.assertNull(employeeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmployeeBetweenTwoDatesWrongDatesNull3() {
        List<Employee> employeeList = employeeService.getEmployeeBetweenDatesOfBirtday(null, null);

        Assert.assertNull(employeeList);
    }

}
