package com.ar0ne.dao;

import com.ar0ne.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class EmployeeDaoImpl implements EmployeeDao {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    KeyHolder keyHolder = new GeneratedKeyHolder();

    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

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

    @Override
    public long addEmployee(Employee employee) {
        String sql = "INSERT INTO employees ( EMPLOYEE_DEPARTMENT_ID, EMPLOYEE_SURNAME, EMPLOYEE_NAME, EMPLOYEE_PATRONYMIC, EMPLOYEE_DATE_OF_BIRTHDAY, EMPLOYEE_SALARY ) VALUES ( :departmentId, :surname, :name, :patronymic, :dateOfBirthday, :salary )";

        MapSqlParameterSource parameterSource= new MapSqlParameterSource();
        parameterSource.addValue("departmentId", employee.getDepartmentId());
        parameterSource.addValue("surname", employee.getSurname());
        parameterSource.addValue("name", employee.getName());
        parameterSource.addValue("patronymic", employee.getPatronymic());
        parameterSource.addValue("salary", employee.getSalary());
        parameterSource.addValue("dateOfBirthday", employee.getDateOfBirthday().toString());

        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void removeEmployee(long id) {
        Map<String, Object> parameters = new HashMap(1);
        parameters.put("id", id);
        String sql = "DELETE FROM employees  WHERE EMPLOYEE_ID = :id";
        namedParameterJdbcTemplate.update( sql, parameters);
    }

    @Override
    public void updateEmployee(Employee employee) {
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

    @Override
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM employees";
        return namedParameterJdbcTemplate.query(sql, new EmployeeMapper());
    }

    @Override
    public Employee getEmployeeById(long id) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "SELECT * FROM employees WHERE EMPLOYEE_ID = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, new EmployeeMapper());
    }

    @Override
    public List<Employee> getEmployeesByDepartmentId(long id){
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "SELECT * FROM employees WHERE EMPLOYEE_DEPARTMENT_ID = :id";
        return namedParameterJdbcTemplate.query(sql, parameters, new EmployeeMapper());
    }
}
