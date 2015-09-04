package com.ar0ne.service.test;

import static org.junit.Assert.*;

import com.ar0ne.model.Department;
import com.ar0ne.service.DepartmentService;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-service-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    private final static String DEPT_NAME = "Test dept.";
    private final String DEPT_FIRST_NAME = "Department of Energy";
    private final static int DEPT_INIT_SIZE = 4;
    private final static long NEGATIVE_LONG = -2L;
    private final static long BIG_LONG = 20000000L;
    private final static long ZERO_LONG = 0l;
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


    @Test
    public void getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        assertNotNull(departments);
        assertEquals(DEPT_INIT_SIZE, departments.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByIdWithIllegalNegativeId() {
        Department department = departmentService.getDepartmentById(NEGATIVE_LONG);
        assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByIdWithIllegalPositiveId() {
        Department department = departmentService.getDepartmentById(BIG_LONG);
        assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByIdWithIllegalZeroId() {
        Department department = departmentService.getDepartmentById(ZERO_LONG);
        assertNull(department);
    }

    @Test
    public void getDepartmentByIdWithCorrectId() {
        Department department = departmentService.getDepartmentById(1);
        assertNotNull(department);
        assertEquals(department.getId(), 1L);
        assertEquals(department.getName(), DEPT_FIRST_NAME);
        assertNotNull(department.getEmployees());
    }

    @Test
    public void getDepartmentById() {
        Department department = new Department(DEPT_NAME, 1L);
        long id = departmentService.addDepartment(department);
        Department ret_department = departmentService.getDepartmentById(id);
        assertNotNull(ret_department);
        assertEquals(ret_department.getId(), id);
        assertEquals(ret_department.getName(), department.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithNullName() {
        Department department = new Department(null, 1L);
        Long id = departmentService.addDepartment(department);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithEmptyName() {
        Department department = new Department("", 1L);
        Long id = departmentService.addDepartment(department);
        assertNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithBigText() {

        Department department = new Department(BIG_TEXT, 1L);
        Long id = departmentService.addDepartment(department);
        assertNull(id);
    }


    @Test
    public void addDepartment() {
        Department department = new Department(DEPT_NAME, 1L);
        Long id = departmentService.addDepartment(department);
        assertNotNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDepartmentWithWrongNegativeId() {
        departmentService.removeDepartment(NEGATIVE_LONG);
        assertEquals(DEPT_INIT_SIZE, departmentService.getAllDepartments().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDepartmentWithWrongPositiveId() {
        departmentService.removeDepartment(BIG_LONG);
        assertEquals(DEPT_INIT_SIZE, departmentService.getAllDepartments().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDepartmentWithWrongZeroId() {
        departmentService.removeDepartment(ZERO_LONG);
        assertEquals(DEPT_INIT_SIZE, departmentService.getAllDepartments().size());
    }

    @Test
    public void removeDepartmentCorrectData() {
        Department department = new Department(DEPT_NAME, 1L);

        Long id = departmentService.addDepartment(department);

        assertNotNull(id);

        int size_before = departmentService.getAllDepartments().size();

        departmentService.removeDepartment(id);

        int size_after = departmentService.getAllDepartments().size();

        assertEquals(size_after + 1, size_before);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateDepartmentWithIncorrectDataNull() {
        departmentService.updateDepartment(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateDepartmentWithIncorrectName() {
        Department department = departmentService.getDepartmentById(1L);
        department.setName(null);
        departmentService.updateDepartment(department);
        Department ret_department = departmentService.getDepartmentById(1L);
        assertNotNull(ret_department);
        assertNotNull(ret_department.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateDepartmentWithNotExistedId() {
        Department department = new Department(DEPT_NAME, BIG_LONG);
        departmentService.updateDepartment(department);
    }

    @Test
    public void updateDepartment() {
        Department department = departmentService.getDepartmentById(1L);
        assertNotNull(department);
        department.setName(DEPT_NAME);
        departmentService.updateDepartment(department);
        Department ret_department = departmentService.getDepartmentById(1L);
        assertEquals(ret_department, department);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByNameWithWrongDataNullName() {
        Department department = departmentService.getDepartmentByName(null);
        assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByNameWithWrongDataEmptyName() {
        Department department = departmentService.getDepartmentByName("");
        assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByNameWithWrongDataBigName() {
        Department department = departmentService.getDepartmentByName(BIG_TEXT);
        assertNull(department);
    }



}
