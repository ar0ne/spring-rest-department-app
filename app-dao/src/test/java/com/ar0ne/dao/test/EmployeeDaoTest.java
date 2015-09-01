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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
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

    @Test
    public void getAllEmployees() {
        List<Employee> employeeList = employeeDao.getAllEmployees();
        assertEquals(EMPL_INIT_SIZE, employeeList.size());

        employeeDao.removeEmployee(1L);
        assertEquals(EMPL_INIT_SIZE - 1, employeeDao.getAllEmployees().size());
    }

    @Test
    public void getEmployeeById() {
        Employee employee = createAndAddToDataBaseEmployee();

        Employee ret_employee = employeeDao.getEmployeeById(employee.getId());

        assertNotNull(ret_employee);
        assertEquals(employee, ret_employee);
    }

    @Test
    public void addEmployee() {

        int size_before = employeeDao.getAllEmployees().size();

        Employee employee = createAndAddToDataBaseEmployee();

        assertNotNull(employee);

        int size_after  = employeeDao.getAllEmployees().size();

        assertEquals(size_before + 1, size_after);
        assertEquals(EMPL_INIT_SIZE, size_before);
    }


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

    @Test
    public void updateDepartment() {

        final String new_name = "Other name";

        Employee employee = createAndAddToDataBaseEmployee();

        employee.setName(new_name);

        employeeDao.updateEmployee(employee);

        Employee ret_employee = employeeDao.getEmployeeById(employee.getId());

        assertEquals(ret_employee.getName(), new_name);

    }



}
