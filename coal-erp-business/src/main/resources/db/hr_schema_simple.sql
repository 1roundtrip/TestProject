-- HR Module Database Schema

-- Department extension table
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

-- Position table
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

-- Employee main table
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

-- Education history table
CREATE TABLE hr_education (
  education_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  education_level varchar(50) NOT NULL,
  school_name varchar(100) NOT NULL,
  major varchar(100),
  start_date date,
  end_date date,
  degree varchar(50),
  is_full_time tinyint DEFAULT 1,
  graduation_cert_no varchar(100),
  degree_cert_no varchar(100),
  create_time datetime
);

-- Work experience table
CREATE TABLE hr_work_experience (
  experience_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  company_name varchar(100) NOT NULL,
  position varchar(100) NOT NULL,
  start_date date NOT NULL,
  end_date date,
  job_description text,
  reference_name varchar(50),
  reference_phone varchar(20),
  create_time datetime
);

-- Family relation table
CREATE TABLE hr_family_relation (
  relation_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  relation_type varchar(20) NOT NULL,
  full_name varchar(50) NOT NULL,
  id_card varchar(20),
  phone varchar(20),
  work_unit varchar(100),
  position varchar(100),
  is_emergency_contact tinyint DEFAULT 0,
  create_time datetime
);

-- Labor contract table
CREATE TABLE hr_contract (
  contract_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  contract_no varchar(100) NOT NULL,
  contract_type varchar(50) NOT NULL,
  sign_date date NOT NULL,
  start_date date NOT NULL,
 end_date date,
  contract_period int,
  trial_period int DEFAULT 0,
  status varchar(20) DEFAULT 'ACTIVE',
  salary_amount decimal(10,2),
  work_location varchar(200),
  job_position varchar(100),
  remark text,
  attachment_path varchar(500),
  create_time datetime,
  update_time datetime,
  UNIQUE KEY uk_contract_no (contract_no)
);

-- Certificate management table
CREATE TABLE hr_certificate (
  certificate_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  cert_type varchar(50) NOT NULL,
  cert_name varchar(100) NOT NULL,
  cert_number varchar(100) NOT NULL,
  issue_authority varchar(200),
  issue_date date,
  expire_date date,
  cert_level varchar(50),
  special_operation_type varchar(50),
  attachment_path varchar(500),
  status varchar(20) DEFAULT 'VALID',
  review_date date,
  create_time datetime,
  update_time datetime
);

-- Safety training record table
CREATE TABLE hr_safety_training (
  training_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  training_type varchar(50) NOT NULL,
  training_name varchar(200) NOT NULL,
  training_date date NOT NULL,
  training_hours int NOT NULL,
  training_institution varchar(200),
  trainer_name varchar(50),
  training_content text,
  assessment_result varchar(50),
  certificate_no varchar(100),
  valid_until date,
  attachment_path varchar(500),
  create_time datetime
);

-- Occupational health record table
CREATE TABLE hr_occupational_health (
  health_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  check_date date NOT NULL,
  check_type varchar(50) NOT NULL,
  check_institution varchar(200),
  check_result varchar(500),
  diagnosis_result varchar(500),
  suggestion text,
  next_check_date date,
  attachment_path varchar(500),
  create_time datetime
);

-- Employee change record table
CREATE TABLE hr_employee_change (
  change_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  change_type varchar(50) NOT NULL,
  change_date date NOT NULL,
  old_value varchar(500),
  new_value varchar(500),
  reason text,
  approver_id bigint,
  approval_status varchar(20) DEFAULT 'PENDING',
  approval_time datetime,
  remark text,
  create_time datetime
);

-- Initialize basic position data
INSERT INTO hr_position (position_code, position_name, dept_id, position_level, position_category, is_special_operation) VALUES
('MNG-001', '矿长', 1, '高层', '管理', 0),
('MNG-002', '生产副矿长', 1, '高层', '管理', 0),
('MNG-003', '安全副矿长', 1, '高层', '管理', 0),
('TEC-001', '总工程师', 2, '中层', '技术', 0),
('SAF-001', '安全员', 3, '基层', '安全', 0),
('MIN-001', '采煤工', 4, '基层', '生产', 0),
('MIN-002', '掘进工', 4, '基层', '生产', 0),
('ELE-001', '电工', 5, '基层', '机电', 1),
('ELE-002', '维修电工', 5, '基层', '机电', 1),
('WEL-001', '焊工', 6, '基层', '机电', 1);
