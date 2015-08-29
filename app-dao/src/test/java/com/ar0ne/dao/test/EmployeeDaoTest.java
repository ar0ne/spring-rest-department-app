package com.ar0ne.dao.test;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static int INIT_SIZE = 15;

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void getAllEmployees() {
        List<Employee> employeeList = employeeDao.getAllEmployees();
        assertEquals(INIT_SIZE, employeeList.size());
    }


}
