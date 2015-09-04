package com.ar0ne.dao;

import com.ar0ne.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    KeyHolder keyHolder = new GeneratedKeyHolder();

    private static final Logger LOGGER = LogManager.getLogger(DepartmentDaoImpl.class);

    /**
     * RowMapper for NamedParameterJdbcTemplate for table of Employees.
     */
    public class EmployeeMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int i) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getLong("EMPLOYEE_ID"));
            employee.setDepartmentId(rs.getLong("EMPLOYEE_DEPARTMENT_ID"));
            employee.setSurname(rs.getString("EMPLOYEE_SURNAME"));
            employee.setName(rs.getString("EMPLOYEE_NAME"));
            employee.setPatronymic(rs.getString("EMPLOYEE_PATRONYMIC"));
            employee.setDateOfBirthday(new LocalDate(rs.getTimestamp("EMPLOYEE_DATE_OF_BIRTHDAY")));
            employee.setSalary(rs.getLong("EMPLOYEE_SALARY"));
            return employee;
        }
    }

    /**
     * Insert specified employee to the database
     * @param employee employee to be inserted to the database
     * @return id of employee in database
     */
    @Override
    public long addEmployee(Employee employee) {

        LOGGER.debug("addEmployee(employee = {})", employee);

        String sql = "INSERT INTO employees ( EMPLOYEE_DEPARTMENT_ID, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC, EMPLOYEE_DATE_OF_BIRTHDAY, EMPLOYEE_SALARY ) VALUES ( :departmentId, :surname, :name, :patronymic, :dateOfBirthday, :salary )";

        MapSqlParameterSource parameterSource= new MapSqlParameterSource();
        parameterSource.addValue("departmentId", employee.getDepartmentId());
        parameterSource.addValue("surname", employee.getSurname());
        parameterSource.addValue("name", employee.getName());
        parameterSource.addValue("patronymic", employee.getPatronymic());
        parameterSource.addValue("salary", employee.getSalary());
        parameterSource.addValue("dateOfBirthday", employee.getDateOfBirthday().toString());

        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

        long id = keyHolder.getKey().longValue();
        LOGGER.debug("addEmployee(employee): id = {}", id);
        return id;
    }

    /**
     * Remove employee from database
     * @param id of employee
     */
    @Override
    public void removeEmployee(long id) {
        LOGGER.debug("removeEmployee(id = {})", id);
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "DELETE FROM employees  WHERE EMPLOYEE_ID = :id";
        namedParameterJdbcTemplate.update( sql, parameters);
    }

    /**
     * Replaces the employee in the database with the specified employee.
     * @param employee to be employee in the database
     */
    @Override
    public void updateEmployee(Employee employee) {
        LOGGER.debug("updateEmployee(employee = {})", employee);

        Map<String, Object> parameters = new HashMap<>(2);

        parameters.put("id",            employee.getId());
        parameters.put("department_id", employee.getDepartmentId());
        parameters.put("surname",       employee.getSurname());
        parameters.put("name",          employee.getName());
        parameters.put("patronymic",    employee.getPatronymic());
        parameters.put("date_of_birthday", employee.getDateOfBirthday().toString());
        parameters.put("salary",        employee.getSalary());

        String sql = "UPDATE employees SET EMPLOYEE_DEPARTMENT_ID = :department_id, EMPLOYEE_SURNAME = :surname, EMPLOYEE_NAME = :name, EMPLOYEE_PATRONYMIC = :patronymic, EMPLOYEE_DATE_OF_BIRTHDAY = :date_of_birthday, EMPLOYEE_SALARY = :salary WHERE EMPLOYEE_ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    /**
     * Returns a list containing all of the employees in the database.
     * @return a list containing all of the employees in the database
     */
    @Override
    public List<Employee> getAllEmployees() {
        LOGGER.debug("getAllEmployees()");
        String sql = "SELECT * FROM employees";
        return namedParameterJdbcTemplate.query(sql, new EmployeeMapper());
    }

    /**
     * Returns the employee with the specified employeetId from database.
     * @param id id of the employee to return
     * @return the employee with the specified employeeId from the database
     */
    @Override
    public Employee getEmployeeById(long id) {
        LOGGER.debug("getEmployeeById(id = {})", id);
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "SELECT * FROM employees WHERE EMPLOYEE_ID = :id";
        Employee employee = namedParameterJdbcTemplate.queryForObject(sql, parameters, new EmployeeMapper());
        LOGGER.debug("getEmployeeById(id) : employee = {}" , employee);
        return employee;
    }

    /**
     * Returns list of employees with the specified dateOfBirthday from database
     * @param date Date of Birthday of the employees to return
     * @return list of the employees in the database
     */
    @Override
    public List<Employee> getEmployeeByDateOfBirthday(LocalDate date) {
        LOGGER.debug("getEmployeeByDateOfBirthday(date = {})", date);
        Map<String, Object> parametrs = new HashMap<>(1);
        parametrs.put("date", date.toString());
        String sql = "SELECT * FROM employees WHERE EMPLOYEE_DATE_OF_BIRTHDAY = :date";
        EmployeeMapper mapper = new EmployeeMapper();
        return namedParameterJdbcTemplate.query(sql, parametrs, mapper);
    }

    /**
     * Returns list of employees with the specified dateOfBirthday in interval from-to from database
     * @param date_from start date of interval for searching
     * @param date_to end date of interval for searching
     * @return list of the employees in the database
     */
    @Override
    public List<Employee> getEmployeeBetweenDatesOfBirtday(LocalDate date_from, LocalDate date_to) {
        LOGGER.debug("getEmployeeBetweenDatesOfBirtday(date_from = {}, date_to = {}", date_from, date_to);
        Map<String, Object> parametrs = new HashMap<>(1);
        parametrs.put("date_from", date_from.toString());
        parametrs.put("date_to", date_to.toString());
        String sql = "SELECT * FROM employees WHERE EMPLOYEE_DATE_OF_BIRTHDAY BETWEEN :date_from AND :date_to";
        EmployeeMapper mapper = new EmployeeMapper();
        return namedParameterJdbcTemplate.query(sql, parametrs, mapper);
    }
}
