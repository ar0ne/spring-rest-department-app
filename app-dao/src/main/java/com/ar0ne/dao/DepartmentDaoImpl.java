package com.ar0ne.dao;

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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class DepartmentDaoImpl implements DepartmentDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    KeyHolder keyHolder = new GeneratedKeyHolder();

    private static final Logger LOGGER = LogManager.getLogger(DepartmentDaoImpl.class);

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
                employee.setDateOfBirthday(new LocalDate(rs.getDate("EMPLOYEE_DATE_OF_BIRTHDAY")));
                employee.setSalary(rs.getLong("EMPLOYEE_SALARY"));

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
            return new ArrayList<Department>(map.values());
        }

    }

    /**
     * Insert specified department to the database
     * @param department department to be inserted to the database
     * @return id of department in database
     */
    public long addDepartment(Department department) {

        LOGGER.debug("addDepartment(department = {})", department);

        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("name", department.getName());
        String sql = "INSERT INTO departments ( DEPARTMENT_NAME ) VALUES ( :name )";
        namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

        long id = keyHolder.getKey().longValue();

        LOGGER.debug("addDepartment(department): id = {}", id);

        return id;
    }

    /**
     * Remove department from database
     * @param id of department
     */
    public void removeDepartment(long id) {
        LOGGER.debug("removeDepartment(id) : id = {}", id);
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "DELETE FROM departments WHERE DEPARTMENT_ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    /**
     * Replaces the department in the database with the specified department.
     * @param department to be updated in the database
     */
    public void updateDepartment(Department department) {
        LOGGER.debug("updateDepartment(department = {})", department);
        Map<String, Object> parameters = new HashMap<>(2);

        parameters.put("id",    department.getId());
        parameters.put("name",  department.getName());

        String sql = "UPDATE departments SET DEPARTMENT_NAME = :name WHERE DEPARTMENT_ID = :id";
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    /**
     * Returns a list containing all of the departments in the database.
     * @return a list containing all of the departments in the database
     */
    public List<Department> getAllDepartments() {
        LOGGER.debug("getAllDepartments()");
        String sql = "SELECT departments.*, employees.* FROM departments LEFT JOIN employees ON departments.DEPARTMENT_ID = employees.EMPLOYEE_DEPARTMENT_ID";
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(sql, mapper);

        return mapper.getAllDepartments();
    }

    /**
     * Returns the department with the specified departmentId from database.
     * @param id id of the department to return
     * @return the department with the specified departmentId from the database
     */
    public Department getDepartmentById(long id) {
        LOGGER.debug("getDepartmentById(id = {})", id);
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("id", id);
        String sql = "SELECT departments.*, employees.* FROM departments LEFT JOIN employees ON departments.DEPARTMENT_ID = employees.EMPLOYEE_DEPARTMENT_ID WHERE departments.DEPARTMENT_ID = :id";
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(sql, parameters, mapper);

        LOGGER.debug("getDepartmentById(id): department = {}", mapper.getDepartment());
        return mapper.getDepartment();
    }

    /**
     * Returns the department with the specified departmentName from database.
     * @param name name of the department to return
     * @return the department with the specified departmentName from the database
     */
    public Department getDepartmentByName(String name) {
        LOGGER.debug("getDepartmentByName(name = {})", name);
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", name);
        String sql = "SELECT departments.*, employees.* FROM departments LEFT JOIN employees ON departments.DEPARTMENT_ID = employees.EMPLOYEE_DEPARTMENT_ID WHERE departments.DEPARTMENT_NAME = :name";
        DepartmentMapper mapper = new DepartmentMapper();
        namedParameterJdbcTemplate.query(sql, parameters, mapper);
        LOGGER.debug("getDepartmentByName(name) : department = {}", mapper.getDepartment());
        return mapper.getDepartment();
    }
}
