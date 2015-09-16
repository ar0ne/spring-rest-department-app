package com.ar0ne.dao.test;

import static org.junit.Assert.*;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
@Transactional(transactionManager="transactionManager")
public class DepartmentDaoTest {

    @Autowired
    private DepartmentDao departmentDao;

    private final static String DEPT_NAME = "Test dept.";
    private final static int DEPT_INIT_SIZE = 4;

    private Department createAndAddToDataBaseDepartment() {

        Department department = new Department(DEPT_NAME, 1L);

        long id = departmentDao.addDepartment(department);
        department.setId(id);

        return department;
    }

    /**
     * Test getAllDepartments: Check count of departments before and after delete
     */
    @Test
    public void getAllDepartments() {
        List<Department> departments = departmentDao.getAllDepartments();
        assertEquals(DEPT_INIT_SIZE, departments.size());

        departmentDao.removeDepartment(1L);
        assertEquals(DEPT_INIT_SIZE - 1, departmentDao.getAllDepartments().size());
    }

    /**
     * Count of all departments in database must to be the same after executing function and demo init,
     * and after remove one element
     */
    @Test
    public void getAllDepartmentsWithoutEmployees() {
        List<Department> departments = departmentDao.getAllDepartmentsWithoutEmployees();
        assertEquals(DEPT_INIT_SIZE, departments.size());

        departmentDao.removeDepartment(1L);
        assertEquals(DEPT_INIT_SIZE - 1, departmentDao.getAllDepartments().size());
    }

    /**
     * Lists of Employees must to be empty
     */
    @Test
    public void getAllDepartmentsWithoutEmployeesNullEmployee() {
        List<Department> departments = departmentDao.getAllDepartmentsWithoutEmployees();
        for(Department department: departments) {
            assertNotNull(department.getEmployees());
            assertEquals(department.getEmployees().size(), 0);
        }
    }

    /**
     * Test addDepartment: Check count of departments before and after add
     */
    @Test
    public void addDepartment() {

        int size_before = departmentDao.getAllDepartments().size();

        Department department = createAndAddToDataBaseDepartment();

        assertNotNull(department);

        int size_after  = departmentDao.getAllDepartments().size();

        assertEquals(size_before + 1, size_after);
        assertEquals(DEPT_INIT_SIZE, size_before);
    }

    /**
     * Test getDepartmentById: Check equals of departments
     */
    @Test
    public void getDepartmentById() {

        Department department = createAndAddToDataBaseDepartment();

        Department ret_department = departmentDao.getDepartmentById(department.getId());

        assertNotNull(ret_department);
        assertEquals(department, ret_department);
    }

    /**
     * Test getDepartmentByName: Check equals of departments
     */
    @Test
    public void getDepartmentByName(){

        Department department = createAndAddToDataBaseDepartment();

        Department ret_department = departmentDao.getDepartmentByName(DEPT_NAME);
        assertEquals(department, ret_department);
    }

    /**
     * Test removeDepartment: Check count of departments before and after delete
     */
    @Test
    public void removeDepartment() {

        Department department = createAndAddToDataBaseDepartment();

        int size_before = departmentDao.getAllDepartments().size();

        departmentDao.removeDepartment(department.getId());

        int size_after = departmentDao.getAllDepartments().size();

        assertEquals(size_before - 1, size_after);

        department = departmentDao.getDepartmentById(department.getId());

        assertNull(department);
    }

    /**
     * Test updateDepartment: check changed name after update
     */
    @Test
    public void updateDepartment() {

        String new_name = "Other name";

        Department department = createAndAddToDataBaseDepartment();

        department.setName(new_name);

        departmentDao.updateDepartment(department);

        Department ret_department = departmentDao.getDepartmentById(department.getId());

        assertEquals(ret_department.getName(), new_name);

    }

}
