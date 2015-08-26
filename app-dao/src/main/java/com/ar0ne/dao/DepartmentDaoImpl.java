package com.ar0ne.dao;

import com.ar0ne.model.Department;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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
        @Override
        public Department mapRow(ResultSet rs, int i) throws SQLException {
            Department department = new Department();
            department.setId(rs.getLong("ID"));
            department.setName(rs.getString("NAME"));
            return department;
        }
    }

    @Override
    public void addDepartment(Department department) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(department);
        String sql = "INSERT INTO departments ( NAME ) VALUES ( :name )";
        namedParameterJdbcTemplate.update( sql, parameterSource, keyHolder);
    }

    @Override
    public void removeDepartment(long id) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "DELETE FROM departments  WHERE ID = :id";
        namedParameterJdbcTemplate.update( sql, parameters);
    }

    @Override
    public void updateDepartment(Department department) {
        Map<String, Object> parameters = new HashMap<>(2);

        parameters.put("id",    department.getId());
        parameters.put("name",  department.getName());

        String sql = "UPDATE departments SET NAME = :name WHERE ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<Department> getAllDepartments() {
        String sql = "SELECT * FROM departments";
        return namedParameterJdbcTemplate.query(sql, new DepartmentMapper());
    }

    @Override
    public Department getDepartmentById(long id) {
        Map<String, Object> parameters = new HashMap(1);
        parameters.put("id", id);
        String sql = "SELECT * FROM departments WHERE ID = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, new DepartmentMapper());
    }

    @Override
    public Department getDepartmentByName(String name) {
        Map<String, Object> parameters = new HashMap(1);
        parameters.put("name", name);
        String sql = "SELECT * FROM departments WHERE NAME = :name";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, new DepartmentMapper());
    }
}
