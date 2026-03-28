USE HR;

DROP TABLE IF EXISTS regions;
CREATE TABLE regions (
  region_id INT NOT NULL AUTO_INCREMENT,
  region_name VARCHAR(25),
  PRIMARY KEY (region_id)
);

DROP TABLE IF EXISTS countries;
CREATE TABLE countries (
  country_id CHAR(4) NOT NULL,
  country_name VARCHAR(60),
  region_id INT,
  PRIMARY KEY (country_id),
  FOREIGN KEY (region_id)
    REFERENCES regions (region_id)
);

DROP TABLE IF EXISTS locations;
CREATE TABLE locations (
  location_id INT NOT NULL AUTO_INCREMENT,
  street_address VARCHAR(40),
  postal_code VARCHAR(12),
  city VARCHAR(30) NOT NULL,
  state_province VARCHAR(25),
  country_id CHAR(4),
  PRIMARY KEY (location_id),
  FOREIGN KEY (country_id)
    REFERENCES countries (country_id)
);

DROP TABLE IF EXISTS departments;
CREATE TABLE departments (
  department_id INT NOT NULL AUTO_INCREMENT,
  department_name VARCHAR(30) NOT NULL,
  manager_id INT,
  location_id INT,
  PRIMARY KEY (department_id),
  FOREIGN KEY (location_id)
    REFERENCES locations (location_id)
);

DROP TABLE IF EXISTS jobs;
CREATE TABLE jobs (
  job_id VARCHAR(10) NOT NULL,
  job_title VARCHAR(35) NOT NULL,
  min_salary DECIMAL(6, 0),
  max_salary DECIMAL(6, 0),
  PRIMARY KEY (job_id)
);

DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
  employee_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(20),
  last_name VARCHAR(25) NOT NULL,
  email VARCHAR(25) NOT NULL,
  phone_number VARCHAR(20),
  hire_date DATE NOT NULL,
  job_id VARCHAR(10) NOT NULL,
  salary DECIMAL(8, 2),
  commission_pct DECIMAL(2, 2),
  manager_id INT,
  department_id INT,
  PRIMARY KEY (employee_id),
  UNIQUE (email),
  FOREIGN KEY (job_id)
    REFERENCES jobs (job_id),
  FOREIGN KEY (manager_id)
    REFERENCES employees (employee_id),
  FOREIGN KEY (department_id)
    REFERENCES departments (department_id)
);

DROP TABLE IF EXISTS job_history;
CREATE TABLE job_history (
  employee_id INT NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  job_id VARCHAR(10) NOT NULL,
  department_id INT,
  FOREIGN KEY (employee_id)
    REFERENCES employees (employee_id),
  FOREIGN KEY (job_id)
    REFERENCES jobs (job_id),
  FOREIGN KEY (department_id)
    REFERENCES departments (department_id),
  PRIMARY KEY (employee_id, start_date)
);