package com.ar0ne.service.impl;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;
import com.ar0ne.model.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @TODO: refactor this dumb test cases
 */
public class DepartmentServiceImplTest
{
    private final static String DEPT_NAME = "Test dept.";
    private final static String DEPT_FIRST_NAME = "Department of Energy";
    private final static int DEPT_INIT_SIZE = 4;
    private final static long NEGATIVE_LONG = -2L;
    private final static long BIG_LONG = 20000000L;
    private final static long ZERO_LONG = 0L;
    private final static String BIG_TEXT = new String(new char[400]).replace('\0', 'A');

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentDao departmentDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks( this );
        List<Department> initDepartments = Arrays.asList(new Department(), new Department(), new Department(), new Department());
        Mockito.when(departmentDao.getDepartmentById( Mockito.eq( 1L ) )).thenReturn( new Department( 1L, DEPT_FIRST_NAME, new ArrayList<>() ) );
        Mockito.when(departmentDao.getAllDepartmentsWithoutEmployees()).thenReturn(initDepartments);
        Mockito.when(departmentDao.getAllDepartments()).thenReturn(initDepartments);
    }

    @Test
    public void getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        Assert.assertNotNull(departments);
        Assert.assertEquals(DEPT_INIT_SIZE, departments.size());
    }

    @Test
    public void getAllDepartmentsWithoutEmployees() {
        List<Department> departments = departmentService.getAllDepartmentsWithoutEmployees();
        Assert.assertNotNull(departments);
        Assert.assertEquals(DEPT_INIT_SIZE, departments.size());
    }

    @Test
    public void getAllDepartmentsWithoutEmployeesEmptyEmployees() {
        List<Department> departments = departmentService.getAllDepartmentsWithoutEmployees();
        for(Department department: departments) {
            Assert.assertNotNull(department.getEmployees());
            Assert.assertEquals(department.getEmployees().size(), 0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByIdWithIllegalNegativeId() {
        Department department = departmentService.getDepartmentById(NEGATIVE_LONG);
        Assert.assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByIdWithIllegalPositiveId() {
        Department department = departmentService.getDepartmentById(BIG_LONG);
        Assert.assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByIdWithIllegalZeroId() {
        Department department = departmentService.getDepartmentById(ZERO_LONG);
        Assert.assertNull(department);
    }

    @Test
    public void getDepartmentById() {
        Department retDepartment = departmentService.getDepartmentById(1L);
        Assert.assertNotNull(retDepartment);
        Assert.assertEquals(retDepartment.getId(), 1L);
        Assert.assertEquals(retDepartment.getName(), DEPT_FIRST_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithNullName() {
        Department department = new Department(null, 1L);
        departmentService.addDepartment(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithEmptyName() {
        Department department = new Department("", 1L);
        departmentService.addDepartment(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithBigText() {

        Department department = new Department(BIG_TEXT, 1L);
        departmentService.addDepartment(department);
    }


    @Test
    public void addDepartment() {
        Department department = new Department(DEPT_NAME, 1L);
        Long id = departmentService.addDepartment(department);
        Assert.assertNotNull(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDepartmentWithWrongNegativeId() {
        departmentService.removeDepartment(NEGATIVE_LONG);
        Assert.assertEquals(DEPT_INIT_SIZE, departmentService.getAllDepartments().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDepartmentWithWrongPositiveId() {
        departmentService.removeDepartment(BIG_LONG);
        Assert.assertEquals(DEPT_INIT_SIZE, departmentService.getAllDepartments().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDepartmentWithWrongZeroId() {
        departmentService.removeDepartment(ZERO_LONG);
        Assert.assertEquals(DEPT_INIT_SIZE, departmentService.getAllDepartments().size());
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
        Assert.assertNotNull(ret_department);
        Assert.assertNotNull(ret_department.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateDepartmentWithNotExistedId() {
        Department department = new Department(DEPT_NAME, BIG_LONG);
        departmentService.updateDepartment(department);
    }

    @Test
    public void updateDepartment() {
        Department department = departmentService.getDepartmentById(1L);
        Assert.assertNotNull(department);
        department.setName(DEPT_NAME);
        departmentService.updateDepartment(department);
        Department ret_department = departmentService.getDepartmentById(1L);
        Assert.assertEquals(ret_department, department);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByNameWithWrongDataNullName() {
        Department department = departmentService.getDepartmentByName(null);
        Assert.assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByNameWithWrongDataEmptyName() {
        Department department = departmentService.getDepartmentByName("");
        Assert.assertNull(department);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDepartmentByNameWithWrongDataBigName() {
        Department department = departmentService.getDepartmentByName(BIG_TEXT);
        Assert.assertNull(department);
    }

}
