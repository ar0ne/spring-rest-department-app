CREATE TABLE IF NOT EXISTS departments (
    DEPARTMENT_ID BIGINT IDENTITY,
    DEPARTMENT_NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS employees (
    EMPLOYEE_ID BIGINT IDENTITY,
    EMPLOYEE_DEPARTMENT_ID BIGINT NOT NULL,
    EMPLOYEE_SURNAME VARCHAR(255) NOT NULL,
    EMPLOYEE_NAME VARCHAR(255) NOT NULL,
    EMPLOYEE_PATRONYMIC VARCHAR(255) NOT NULL,
    EMPLOYEE_DATE_OF_BIRTHDAY DATE NOT NULL,
    EMPLOYEE_SALARY BIGINT NOT NULL
);