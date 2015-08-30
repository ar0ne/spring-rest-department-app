package com.ar0ne.dao;

import com.ar0ne.model.Department;

import java.sql.Array;
import org.joda.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ar0ne.model.Employee;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DepartmentDaoImpl implements DepartmentDao {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    KeyHolder keyHolder = new GeneratedKeyHolder();

    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public class DepartmentMapper implements RowMapper<Department> {

        private Department department;
        private Map<Long, Department> map = new HashMap<>();

        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {

            Long id = rs.getLong("DEPARTMENT_ID");

            department = map.get(id);

            if (department == null) {
                this.department = new Department ();
                department.setId(rs.getLong("DEPARTMENT_ID"));
                department.setName(rs.getString("DEPARTMENT_NAME"));
                map.put(id, department);
            }

            if (rs.getString("EMPLOYEE_NAME") != null       ||
                    rs.getLong("EMPLOYEE_ID") != 0          ||
                    rs.getLong("EMPLOYEE_DEPARTMENT_ID") != 0) {

                Employee employee = new Employee();
                employee.setId(rs.getLong("EMPLOYEE_ID"));
                employee.setDepartmentId(rs.getLong("EMPLOYEE_DEPARTMENT_ID"));
                employee.setName(rs.getString("EMPLOYEE_NAME"));
                employee.setSurname(rs.getString("EMPLOYEE_SURNAME"));
                employee.setPatronymic(rs.getString("EMPLOYEE_PATRONYMIC"));
                employee.setDateOfBirthday(new LocalDateTime(rs.getDate("EMPLOYEE_DATE_OF_BIRTHDAY")));
                employee.setSalary(rs.getLong("EMPLOYEE_SALARY"));

                this.department.addEmployee(employee);
            }
            return null;
        }

        public final Department getDepartment() {
            return department;
        }

        public final List<Department> getAllDepartments() {
            return new ArrayList<Department>(map.values());
        }

    }


    @Override
    public long addDepartment(Department department) {

        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("name", department.getName());
        String sql = "INSERT INTO departments ( DEPARTMENT_NAME ) VALUES ( :name )";
        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void removeDepartment(long id) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "DELETE FROM departments WHERE DEPARTMENT_ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public void updateDepartment(Department department) {
        Map<String, Object> parameters = new HashMap<>(2);

        parameters.put("id",    department.getId());
        parameters.put("name",  department.getName());

        String sql = "UPDATE departments SET DEPARTMENT_NAME = :name WHERE DEPARTMENT_ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<Department> getAllDepartments() {
        String sql = "SELECT departments.*, employees.* FROM departments LEFT JOIN employees ON departments.DEPARTMENT_ID = employees.EMPLOYEE_DEPARTMENT_ID";
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(sql, mapper);

        return mapper.getAllDepartments();
    }

    @Override
    public Department getDepartmentById(long id) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "SELECT departments.*, employees.* FROM departments LEFT JOIN employees ON departments.DEPARTMENT_ID = employees.EMPLOYEE_DEPARTMENT_ID WHERE departments.DEPARTMENT_ID = :id";
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(sql, parameters, mapper);
        return mapper.getDepartment();
    }

    @Override
    public Department getDepartmentByName(String name) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", name);
        String sql = "SELECT departments.*, employees.* FROM departments LEFT JOIN employees ON departments.DEPARTMENT_ID = employees.EMPLOYEE_DEPARTMENT_ID WHERE departments.DEPARTMENT_NAME = :name";
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(sql, parameters, mapper);
        return mapper.getDepartment();
    }
}
