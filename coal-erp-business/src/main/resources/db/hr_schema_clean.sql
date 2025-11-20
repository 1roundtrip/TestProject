CREATE TABLE hr_department (
  dept_id bigint PRIMARY KEY,
  dept_code varchar(50) NOT NULL,
  dept_type varchar(20) NOT NULL,
  establish_date date,
  budget_count int DEFAULT 0,
  actual_count int DEFAULT 0,
  cost_center varchar(50),
  is_production tinyint DEFAULT 0,
  is_safety_critical tinyint DEFAULT 0
);

CREATE TABLE hr_position (
  position_id bigint AUTO_INCREMENT PRIMARY KEY,
  position_code varchar(50) NOT NULL,
  position_name varchar(100) NOT NULL,
  dept_id bigint NOT NULL,
  position_level varchar(20) NOT NULL,
  position_category varchar(50) NOT NULL,
  is_special_operation tinyint DEFAULT 0,
  special_operation_type varchar(50),
  job_description text,
  requirements text,
  min_salary decimal(10,2),
  max_salary decimal(10,2),
  status tinyint DEFAULT 1,
  create_time datetime,
  update_time datetime,
  UNIQUE KEY uk_position_code (position_code)
);

CREATE TABLE hr_employee (
  employee_id bigint AUTO_INCREMENT PRIMARY KEY,
  user_id bigint NOT NULL,
  employee_code varchar(50) NOT NULL,
  id_card varchar(20) NOT NULL,
  gender varchar(10) NOT NULL,
  birth_date date,
  nationality varchar(50),
  marital_status varchar(20),
  political_status varchar(50),
  native_place varchar(100),
  household_type varchar(50),
  current_address varchar(200),
  emergency_contact varchar(50),
  emergency_phone varchar(20),
  position_id bigint NOT NULL,
  hire_date date NOT NULL,
  work_status varchar(20) DEFAULT 'PROBATION',
  employment_type varchar(20),
  work_shift varchar(20),
  is_underground_worker tinyint DEFAULT 0,
  is_special_operator tinyint DEFAULT 0,
  create_time datetime,
  update_time datetime,
  UNIQUE KEY uk_employee_code (employee_code),
  UNIQUE KEY uk_user_id (user_id),
  UNIQUE KEY uk_id_card (id_card)
);

INSERT INTO hr_position (position_code, position_name, dept_id, position_level, position_category, is_special_operation) VALUES
('MNG-001', 'Mine Director', 1, 'Executive', 'Management', 0),
('MNG-002', 'Production Deputy Director', 1, 'Executive', 'Management', 0),
('MNG-003', 'Safety Deputy Director', 1, 'Executive', 'Management', 0),
('TEC-001', 'Chief Engineer', 2, 'Middle', 'Technical', 0),
('SAF-001', 'Safety Officer', 3, 'Basic', 'Safety', 0),
('MIN-001', 'Coal Miner', 4, 'Basic', 'Production', 0),
('MIN-002', 'Tunneling Worker', 4, 'Basic', 'Production', 0),
('ELE-001', 'Electrician', 5, 'Basic', 'Electrical', 1),
('ELE-002', 'Maintenance Electrician', 5, 'Basic', 'Electrical', 1),
('WEL-001', 'Welder', 6, 'Basic', 'Electrical', 1);