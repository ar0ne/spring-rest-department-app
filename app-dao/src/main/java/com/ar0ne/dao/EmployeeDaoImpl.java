package com.ar0ne.dao;

import com.ar0ne.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDateTime;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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
            employee.setId(rs.getLong("ID"));
            employee.setDepartmentId(rs.getLong("DEPARTMENT_ID"));
            employee.setSurname(rs.getString("SURNAME"));
            employee.setName(rs.getString("NAME"));
            employee.setPatronymic(rs.getString("PATRONYMIC"));
            employee.setDateOfBirthday(new LocalDateTime(rs.getTimestamp("DATE_OF_BIRTHDAY")));
            employee.setSalary(rs.getLong("SALARY"));
            return employee;
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(employee);
        String sql = "INSERT INTO employees ( DEPARTMENT_ID, SURNAME, NAME, PATRONYMIC, DATE_OF_BIRTHDAY, SALARY ) VALUES ( :department_id, :surname, :name, :patronymic, :date_of_birthday, :salary )";
        namedParameterJdbcTemplate.update( sql, parameterSource, keyHolder);
    }

    @Override
    public void removeEmployee(long id) {
        Map<String, Object> parameters = new HashMap(1);
        parameters.put("id", id);
        String sql = "DELETE FROM employees  WHERE ID = :id";
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
        parameters.put("date_of_birthday", employee.getDateOfBirthday());
        parameters.put("salary",        employee.getSalary());

        String sql = "UPDATE employees SET DEPARTMENT_ID = :department_id, SURNAME = :surname, NAME = :name, PATRONYMIC = :patronymic, DATE_OF_BIRTHDAY = :date_of_birthday, SALARY = :salary WHERE ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM employee";
        return namedParameterJdbcTemplate.query(sql, new EmployeeMapper());
    }

    @Override
    public Employee getEmployeeById(long id) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "SELECT * FROM employee WHERE ID = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, new EmployeeMapper());
    }
}
