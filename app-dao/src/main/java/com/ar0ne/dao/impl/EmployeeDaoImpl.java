package com.ar0ne.dao.impl;

import com.ar0ne.dao.EmployeeDao;
import com.ar0ne.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDaoImpl implements EmployeeDao
{

    private static final Logger logger = LogManager.getLogger(DepartmentDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private KeyHolder keyHolder = new GeneratedKeyHolder();

    @Value("${parameters.id}")
    private String PARAMETER_ID;

    @Value("${parameters.name}")
    private String PARAMETER_NAME;

    @Value("${parameters.surname}")
    private String PARAMETER_SURNAME;

    @Value("${parameters.department_id}")
    private String PARAMETER_DEPARTMENT_ID;

    @Value("${parameters.patronymic}")
    private String PARAMETER_PATRONYMIC;

    @Value("${parameters.date_of_birthday}")
    private String PARAMETER_DATE_OF_BIRTHDAY;

    @Value("${parameters.date}")
    private String PARAMETER_DATE;

    @Value("${parameters.date_to}")
    private String PARAMETER_DATE_TO;

    @Value("${parameters.date_from}")
    private String PARAMETER_DATE_FROM;

    @Value("${parameters.salary}")
    private String PARAMETER_SALARY;

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

    @Value("${employee.ADD_EMPLOYEE}")
    private String ADD_EMPLOYEE;

    @Value("${employee.DELETE_EMPLOYEE_BY_ID}")
    private String DELETE_EMPLOYEE_BY_ID;

    @Value("${employee.UPDATE_EMPLOYEE}")
    private String UPDATE_EMPLOYEE;

    @Value("${employee.GET_ALL_EMPLOYEES}")
    private String GET_ALL_EMPLOYEES;

    @Value("${employee.GET_EMPLOYEE_BY_ID}")
    private String GET_EMPLOYEE_BY_ID;

    @Value("${employee.GET_EMPLOYEE_BY_DATE_OF_BIRTHDAY}")
    private String GET_EMPLOYEE_BY_DATE_OF_BIRTHDAY;

    @Value("${employee.GET_EMPLOYEE_BETWEEN_DATES_OF_BIRTHDAY}")
    private String GET_EMPLOYEE_BETWEEN_DATES_OF_BIRTHDAY;

    /**
     * RowMapper for NamedParameterJdbcTemplate for table of Employees.
     */
    public class EmployeeMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int i) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getLong(EMPLOYEE_ID));
            employee.setDepartmentId(rs.getLong(EMPLOYEE_DEPARTMENT_ID));
            employee.setSurname(rs.getString(EMPLOYEE_SURNAME));
            employee.setName(rs.getString(EMPLOYEE_NAME));
            employee.setPatronymic(rs.getString(EMPLOYEE_PATRONYMIC));
            employee.setDateOfBirthday(new LocalDate(rs.getTimestamp(EMPLOYEE_DATE_OF_BIRTHDAY)));
            employee.setSalary( rs.getLong( EMPLOYEE_SALARY ) );
            return employee;
        }
    }

    /**
     * Insert specified employee to the database
     * @param employee employee to be inserted to the database
     * @return id of employee in database
     */
    public long addEmployee(Employee employee) {

        logger.debug("addEmployee(employee = {})", employee);

        MapSqlParameterSource parameterSource= new MapSqlParameterSource();
        parameterSource.addValue(PARAMETER_DEPARTMENT_ID,       employee.getDepartmentId());
        parameterSource.addValue(PARAMETER_SURNAME,             employee.getSurname());
        parameterSource.addValue(PARAMETER_NAME,                employee.getName());
        parameterSource.addValue(PARAMETER_PATRONYMIC,          employee.getPatronymic());
        parameterSource.addValue(PARAMETER_SALARY,              employee.getSalary());
        parameterSource.addValue(PARAMETER_DATE_OF_BIRTHDAY,    employee.getDateOfBirthday().toString());

        namedParameterJdbcTemplate.update(ADD_EMPLOYEE, parameterSource, keyHolder);

        long id = keyHolder.getKey().longValue();
        logger.debug("addEmployee(employee): id = {}", id);
        return id;
    }

    /**
     * Remove employee from database
     * @param id of employee
     */
    public void removeEmployee(long id) {
        logger.debug("removeEmployee(id = {})", id);

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_ID, id);

        namedParameterJdbcTemplate.update( DELETE_EMPLOYEE_BY_ID, parameters);
    }

    /**
     * Replaces the employee in the database with the specified employee.
     * @param employee to be employee in the database
     */
    public void updateEmployee(Employee employee) {
        logger.debug("updateEmployee(employee = {})", employee);

        Map<String, Object> parameters = new HashMap<>(2);

        parameters.put(PARAMETER_ID,                employee.getId());
        parameters.put(PARAMETER_DEPARTMENT_ID,     employee.getDepartmentId());
        parameters.put(PARAMETER_SURNAME,           employee.getSurname());
        parameters.put(PARAMETER_NAME,              employee.getName());
        parameters.put(PARAMETER_PATRONYMIC,        employee.getPatronymic());
        parameters.put(PARAMETER_DATE_OF_BIRTHDAY,  employee.getDateOfBirthday().toString());
        parameters.put(PARAMETER_SALARY,            employee.getSalary());

        namedParameterJdbcTemplate.update(UPDATE_EMPLOYEE, parameters);
    }

    /**
     * Returns a list containing all of the employees in the database.
     * @return a list containing all of the employees in the database
     */
    public List<Employee> getAllEmployees() {
        logger.debug("getAllEmployees()");
        return namedParameterJdbcTemplate.query(GET_ALL_EMPLOYEES, new EmployeeMapper());
    }

    /**
     * Returns the employee with the specified employeeId from database.
     * @param id id of the employee to return
     * @return the employee with the specified employeeId from the database
     */
    public Employee getEmployeeById(long id) {
        logger.debug("getEmployeeById(id = {})", id);

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_ID, id);
        Employee employee = namedParameterJdbcTemplate.queryForObject(GET_EMPLOYEE_BY_ID, parameters, new EmployeeMapper());

        logger.debug("getEmployeeById(id) : employee = {}" , employee);
        return employee;
    }

    /**
     * Returns list of employees with the specified dateOfBirthday from database
     * @param date Date of Birthday of the employees to return
     * @return list of the employees in the database
     */
    public List<Employee> getEmployeeByDateOfBirthday(LocalDate date) {
        logger.debug("getEmployeeByDateOfBirthday(date = {})", date);

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_DATE, date.toString());
        EmployeeMapper mapper = new EmployeeMapper();

        return namedParameterJdbcTemplate.query(GET_EMPLOYEE_BY_DATE_OF_BIRTHDAY, parameters, mapper);
    }

    /**
     * Returns list of employees with the specified dateOfBirthday in interval from-to from database
     * @param dateFrom start date of interval for searching
     * @param dateTo end date of interval for searching
     * @return list of the employees in the database
     */
    public List<Employee> getEmployeeBetweenDatesOfBirtday(LocalDate dateFrom, LocalDate dateTo ) {
        logger.debug("getEmployeeBetweenDatesOfBirtday(date_from = {}, date_to = {}", dateFrom, dateTo );

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(PARAMETER_DATE_FROM, dateFrom.toString());
        parameters.put(PARAMETER_DATE_TO, dateTo.toString());
        EmployeeMapper mapper = new EmployeeMapper();

        return namedParameterJdbcTemplate.query(GET_EMPLOYEE_BETWEEN_DATES_OF_BIRTHDAY, parameters, mapper);
    }
}
