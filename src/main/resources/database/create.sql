


use HR;

DROP TABLE IF EXISTS regions;
create table regions (
  region_id decimal not null,
  region_name varchar(25),
  primary key (region_id)
);

DROP TABLE IF EXISTS countries;
create table countries (
  country_id char(4) not null,
  country_name varchar(60),
  region_id decimal,
  primary key (country_id),
  foreign key (region_id)
  references regions (region_id)
);

DROP TABLE IF EXISTS locations;
create table locations (
  location_id decimal(4, 0) not null,
  street_address varchar(40),
  postal_code varchar(12),
  city varchar(30) not null,
  state_province varchar(25),
  country_id char(4),
  primary key (location_id),
  foreign key (country_id)
  references countries (country_id)
);

DROP TABLE IF EXISTS departments;
create table departments (
  department_id decimal(4, 0) not null,
  department_name varchar(30) not null,
  manager_id decimal(6, 0),
  location_id decimal(4, 0),
  primary key (department_id),
  foreign key (location_id)
  references locations (location_id)
);

DROP TABLE IF EXISTS jobs;
create table jobs (
  job_id varchar(10) not null,
  job_title varchar(35) not null,
  min_salary decimal(6, 0),
  max_salary decimal(6, 0),
  primary key (job_id)
);

DROP TABLE IF EXISTS employees;
create table employees (
  employee_id decimal(6, 0) not null,
  first_name varchar(20),
  last_name varchar(25) not null,
  email varchar(25) not null,
  phone_number varchar(20),
  hire_date date not null,
  job_id varchar(10) not null,
  salary decimal(8, 2),
  commission_pct decimal(2, 2),
  manager_id decimal(6, 0),
  department_id decimal(4, 0),
  primary key (employee_id),
  unique (email),
  foreign key (job_id)
  references jobs (job_id),
  foreign key (manager_id)
  references employees (employee_id),
  foreign key (department_id)
  references departments (department_id)
);


  
  DROP TABLE IF EXISTS job_history;
  create table job_history (
  employee_id decimal(6, 0) not null,
  start_date date not null,
  end_date date not null,
  job_id varchar(10) not null,
  department_id decimal(4, 0),
  foreign key (employee_id)
  references employees (employee_id),
  foreign key (job_id)
  references jobs (job_id),
  foreign key (department_id)
  references departments (department_id),
  primary key (employee_id, start_date)
);



