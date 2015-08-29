package com.ar0ne.service.test;

import static org.junit.Assert.*;

import com.ar0ne.model.Department;
import com.ar0ne.service.DepartmentService;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final static int INIT_SIZE = 4;

    @Test
    public void getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        assertEquals(INIT_SIZE, departments.size());
    }


}
