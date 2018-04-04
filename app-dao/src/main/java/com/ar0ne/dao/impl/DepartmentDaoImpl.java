package com.ar0ne.dao.impl;

import com.ar0ne.dao.DepartmentDao;
import com.ar0ne.model.Department;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ar0ne.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDaoImpl implements DepartmentDao
{

    private static final Logger logger = LogManager.getLogger(DepartmentDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private KeyHolder keyHolder = new GeneratedKeyHolder();

    @Value("${parameters.id}")
    private String PARAMETER_ID;

    @Value("${parameters.name}")
    private String PARAMETER_NAME;

    @Value("${department.DEPARTMENT_ID_COLUMN}")
    private String DEPARTMENT_ID;

    @Value("${department.DEPARTMENT_NAME_COLUMN}")
    private String DEPARTMENT_NAME;

    @Value("${department.ADD_DEPARTMENT}")
    private String ADD_DEPARTMENT;

    @Value("${department.UPDATE_DEPARTMENT}")
    private String UPDATE_DEPARTMENT;

    @Value("${department.GET_ALL_DEPARTMENTS}")
    private String GET_ALL_DEPARTMENTS;

    @Value("${department.DELETE_DEPARTMENT_BY_ID}")
    private String DELETE_DEPARTMENT_BY_ID;

    @Value("${department.GET_DEPARTMENT_BY_ID}")
    private String GET_DEPARTMENT_BY_ID;

    @Value("${department.GET_DEPARTMENT_BY_NAME}")
    private String GET_DEPARTMENT_BY_NAME;

    @Value("${department.GET_ALL_DEPARTMENTS_WITHOUT_EMPLOYEES}")
    private String GET_ALL_DEPARTMENTS_WITHOUT_EMPLOYEES;

    @Value("${employee.EMPLOYEE_ID_COLUMN}")
    private String EMPLOYEE_ID;

    @Value("${employee.EMPLOYEE_NAME_COLUMN}")
    private String EMPLOYEE_NAME;

    @Value("${employee.EMPLOYEE_DEPARTMENT_ID_COLUMN}")
    private String EMPLOYEE_DEPARTMENT_ID;

    @Value("${employee.EMPLOYEE_SURNAME_COLUMN}")
    private String EMPLOYEE_SURNAME;

    @Value("${employee.EMPLOYEE_PATRONYMIC_COLUMN}")
    private String EMPLOYEE_PATRONYMIC;

    @Value("${employee.EMPLOYEE_DATE_OF_BIRTHDAY_COLUMN}")
    private String EMPLOYEE_DATE_OF_BIRTHDAY;

    @Value("${employee.EMPLOYEE_SALARY_COLUMN}")
    private String EMPLOYEE_SALARY;

    @Value("${employee.DELETE_EMPLOYEE_BY_DEPARTMENT_ID}")
    private String DELETE_EMPLOYEE_BY_DEPARTMENT_ID;


    /**
     * Mapper for NamedParameterJdbcTemplate for union tables of Departments and Employees.
     * In case when we can have only one entity of Department we return department from @department field.
     * Every iteration we check if departmentId exist in map, and add it if not. We do it because in union tables we
     * get few row with one Id of departments but different from Employee part.
     */
    public class DepartmentMapper implements RowMapper<Department> {

        private Department department;
        private Map<Long, Department> map = new HashMap<>();

        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {

            Long id = rs.getLong(DEPARTMENT_ID);

            department = map.get(id);

            if (department == null) {
                this.department = new Department ();
                department.setId(rs.getLong(DEPARTMENT_ID));
                department.setName(rs.getString(DEPARTMENT_NAME));
                map.put( id, department );
            }

            if (rs.getString(EMPLOYEE_NAME) != null ||
                    rs.getLong(EMPLOYEE_ID) != 0 ||
                    rs.getLong(EMPLOYEE_DEPARTMENT_ID) != 0) {

                Employee employee = new Employee();
                employee.setId(rs.getLong(EMPLOYEE_ID));
                employee.setDepartmentId(rs.getLong(EMPLOYEE_DEPARTMENT_ID));
                employee.setName( rs.getString(EMPLOYEE_NAME));
                employee.setSurname(rs.getString(EMPLOYEE_SURNAME));
                employee.setPatronymic(rs.getString(EMPLOYEE_PATRONYMIC));
                employee.setDateOfBirthday(new LocalDate(rs.getDate(EMPLOYEE_DATE_OF_BIRTHDAY)));
                employee.setSalary( rs.getLong( EMPLOYEE_SALARY ) );

                this.department.addEmployee(employee);
            }

            return null;
        }


        /**
         * In case when we have only one entity of Department(ex. getById()) we use this function.
         * @return Department object with field of List<Employee> from ResultSet of SQL query.
         */
        public final Department getDepartment() {
            return department;
        }

        /**
         * In case when we can have more then one entity of Department(ex. getAll()) we use this function.
         * @return List of Departments with field of List<Employee> from ResultSet of SQL query.
         */
        public final List<Department> getAllDepartments() {
            return new ArrayList<>(map.values());
        }

    }

    /**
     * Mapper for NamedParameterJdbcTemplate only for Departments entity without employees
     */
    public class DepartmentOnlyMapper implements RowMapper<Department> {

        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setId(rs.getLong(DEPARTMENT_ID));
            department.setName(rs.getString(DEPARTMENT_NAME));
            department.setEmployees(new ArrayList<>());
            return department;
        }

    }


    /**
     * Insert specified department to the database
     * @param department department to be inserted to the database
     * @return id of department in database
     */
    public long addDepartment(Department department) {

        logger.debug("addDepartment(department = {})", department);

        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("name", department.getName());

        namedParameterJdbcTemplate.update( ADD_DEPARTMENT, parameterSource, keyHolder);

        long id = keyHolder.getKey().longValue();

        logger.debug("addDepartment(department): id = {}", id);

        return id;
    }

    /**
     * Remove department from database and specified employees for this department
     * @param id of department
     */
    public void removeDepartment(long id) {
        logger.debug("removeDepartment(id) : id = {}", id);

        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_ID, id);

        namedParameterJdbcTemplate.update( DELETE_DEPARTMENT_BY_ID, parameters);
        namedParameterJdbcTemplate.update( DELETE_EMPLOYEE_BY_DEPARTMENT_ID, parameters);
    }

    /**
     * Replaces the department in the database with the specified department.
     * @param department to be updated in the database
     */
    public void updateDepartment(Department department) {
        logger.debug("updateDepartment(department = {})", department);

        final Map<String, Object> parameters = new HashMap<>(2);
        parameters.put(PARAMETER_ID,    department.getId());
        parameters.put(PARAMETER_NAME,  department.getName());

        namedParameterJdbcTemplate.update( UPDATE_DEPARTMENT, parameters );
    }

    /**
     * Returns a list containing all of the departments with specified employees in the database.
     * @return a list containing all of the departments with specified employees in the database
     */
    public List<Department> getAllDepartments() {
        logger.debug("getAllDepartments()");

        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query( GET_ALL_DEPARTMENTS, mapper);

        return mapper.getAllDepartments();
    }


    /**
     * Returns a list containing all of the departments, but without specified employees in the database.
     * @return a list containing all of the departments, but without specified employees in the database
     */
    public List<Department> getAllDepartmentsWithoutEmployees() {
        logger.debug("getAllDepartmentsWithoutEmployees()");

        return namedParameterJdbcTemplate.query( GET_ALL_DEPARTMENTS_WITHOUT_EMPLOYEES, new DepartmentOnlyMapper());
    }

    /**
     * Returns the department with the specified departmentId from database.
     * @param id id of the department to return
     * @return the department with the specified departmentId from the database
     */
    public Department getDepartmentById(long id) {
        logger.debug("getDepartmentById(id = {})", id);

        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_ID, id);
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query( GET_DEPARTMENT_BY_ID, parameters, mapper);

        logger.debug("getDepartmentById(id): department = {}", mapper.getDepartment());
        return mapper.getDepartment();
    }

    /**
     * Returns the department with the specified departmentName from database.
     * @param name name of the department to return
     * @return the department with the specified departmentName from the database
     */
    public Department getDepartmentByName(String name) {
        logger.debug("getDepartmentByName(name = {})", name);

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_NAME, name);

        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(GET_DEPARTMENT_BY_NAME, parameters, mapper);

        logger.debug("getDepartmentByName(name) : department = {}", mapper.getDepartment());
        return mapper.getDepartment();
    }
}
