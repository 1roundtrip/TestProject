-- HR人力资源模块数据库表结构

-- 部门扩展表（继承自sys_dept）
CREATE TABLE hr_department (
  dept_id bigint PRIMARY KEY COMMENT '部门ID，与sys_dept主键关联',
  dept_code varchar(50) NOT NULL COMMENT '部门编码',
  dept_type varchar(20) NOT NULL COMMENT '部门类型（ADMIN-行政,PRODUCTION-生产,SAFETY-安全,LOGISTICS-后勤）',
  establish_date date COMMENT '成立日期',
  budget_count int DEFAULT 0 COMMENT '编制人数',
  actual_count int DEFAULT 0 COMMENT '实有人数',
  cost_center varchar(50) COMMENT '成本中心',
  is_production tinyint DEFAULT 0 COMMENT '是否生产部门',
  is_safety_critical tinyint DEFAULT 0 COMMENT '是否安全关键部门',
  FOREIGN KEY (dept_id) REFERENCES sys_dept(dept_id)
) COMMENT='部门扩展表';

-- 岗位表
CREATE TABLE hr_position (
  position_id bigint AUTO_INCREMENT PRIMARY KEY,
  position_code varchar(50) NOT NULL COMMENT '岗位编码',
  position_name varchar(100) NOT NULL COMMENT '岗位名称',
  dept_id bigint NOT NULL COMMENT '所属部门',
  position_level varchar(20) NOT NULL COMMENT '岗位级别',
  position_category varchar(50) NOT NULL COMMENT '岗位类别',
  is_special_operation tinyint DEFAULT 0 COMMENT '是否特种作业岗位',
  special_operation_type varchar(50) COMMENT '特种作业类型',
  job_description text COMMENT '职位描述',
  requirements text COMMENT '任职要求',
  min_salary decimal(10,2) COMMENT '最低薪资',
  max_salary decimal(10,2) COMMENT '最高薪资',
  status tinyint DEFAULT 1 COMMENT '状态（1-启用，0-停用）',
  create_time datetime,
  update_time datetime,
  UNIQUE KEY uk_position_code (position_code),
  FOREIGN KEY (dept_id) REFERENCES sys_dept(dept_id)
) COMMENT='岗位表';

-- 员工主表（与sys_user关联）
CREATE TABLE hr_employee (
  employee_id bigint AUTO_INCREMENT PRIMARY KEY,
  user_id bigint NOT NULL COMMENT '用户ID，与sys_user关联',
  employee_code varchar(50) NOT NULL COMMENT '员工工号',
  id_card varchar(20) NOT NULL COMMENT '身份证号',
  gender varchar(10) NOT NULL COMMENT '性别',
  birth_date date COMMENT '出生日期',
  nationality varchar(50) COMMENT '民族',
  marital_status varchar(20) COMMENT '婚姻状况',
  political_status varchar(50) COMMENT '政治面貌',
  native_place varchar(100) COMMENT '籍贯',
  household_type varchar(50) COMMENT '户籍类型',
  current_address varchar(200) COMMENT '现住地址',
  emergency_contact varchar(50) COMMENT '紧急联系人',
  emergency_phone varchar(20) COMMENT '紧急联系电话',
  position_id bigint NOT NULL COMMENT '岗位ID',
  hire_date date NOT NULL COMMENT '入职日期',
  work_status varchar(20) DEFAULT 'PROBATION' COMMENT '在职状态',
  employment_type varchar(20) COMMENT '用工类型',
  work_shift varchar(20) COMMENT '班制',
  is_underground_worker tinyint DEFAULT 0 COMMENT '是否井下作业人员',
  is_special_operator tinyint DEFAULT 0 COMMENT '是否特种作业人员',
  create_time datetime,
  update_time datetime,
  UNIQUE KEY uk_employee_code (employee_code),
  UNIQUE KEY uk_user_id (user_id),
  UNIQUE KEY uk_id_card (id_card),
  FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
  FOREIGN KEY (position_id) REFERENCES hr_position(position_id)
) COMMENT='员工主表';

-- 教育经历表
CREATE TABLE hr_education (
  education_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  education_level varchar(50) NOT NULL COMMENT '学历层次',
  school_name varchar(100) NOT NULL COMMENT '学校名称',
  major varchar(100) COMMENT '专业',
  start_date date COMMENT '开始日期',
  end_date date COMMENT '结束日期',
  degree varchar(50) COMMENT '学位',
  is_full_time tinyint DEFAULT 1 COMMENT '是否全日制',
  graduation_cert_no varchar(100) COMMENT '毕业证书编号',
  degree_cert_no varchar(100) COMMENT '学位证书编号',
  create_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='教育经历表';

-- 工作经历表
CREATE TABLE hr_work_experience (
  experience_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  company_name varchar(100) NOT NULL COMMENT '公司名称',
  position varchar(100) NOT NULL COMMENT '职位',
  start_date date NOT NULL COMMENT '开始日期',
  end_date date COMMENT '结束日期',
  job_description text COMMENT '工作描述',
  reference_name varchar(50) COMMENT '证明人姓名',
  reference_phone varchar(20) COMMENT '证明人电话',
  create_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='工作经历表';

-- 家庭关系表
CREATE TABLE hr_family_relation (
  relation_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  relation_type varchar(20) NOT NULL COMMENT '关系类型',
  full_name varchar(50) NOT NULL COMMENT '姓名',
  id_card varchar(20) COMMENT '身份证号',
  phone varchar(20) COMMENT '联系电话',
  work_unit varchar(100) COMMENT '工作单位',
  position varchar(100) COMMENT '职务',
  is_emergency_contact tinyint DEFAULT 0 COMMENT '是否紧急联系人',
  create_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='家庭关系表';

-- 劳动合同表
CREATE TABLE hr_contract (
  contract_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  contract_no varchar(100) NOT NULL COMMENT '合同编号',
  contract_type varchar(50) NOT NULL COMMENT '合同类型',
  sign_date date NOT NULL COMMENT '签订日期',
  start_date date NOT NULL COMMENT '开始日期',
  end_date date COMMENT '结束日期',
  contract_period int COMMENT '合同期限（月）',
  trial_period int DEFAULT 0 COMMENT '试用期（月）',
  status varchar(20) DEFAULT 'ACTIVE' COMMENT '合同状态',
  salary_amount decimal(10,2) COMMENT '合同工资',
  work_location varchar(200) COMMENT '工作地点',
  job_position varchar(100) COMMENT '工作岗位',
  remark text COMMENT '备注',
  attachment_path varchar(500) COMMENT '附件路径',
  create_time datetime,
  update_time datetime,
  UNIQUE KEY uk_contract_no (contract_no),
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='劳动合同表';

-- 证照管理表
CREATE TABLE hr_certificate (
  certificate_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  cert_type varchar(50) NOT NULL COMMENT '证照类型',
  cert_name varchar(100) NOT NULL COMMENT '证照名称',
  cert_number varchar(100) NOT NULL COMMENT '证照号码',
  issue_authority varchar(200) COMMENT '发证机关',
  issue_date date COMMENT '发证日期',
  expire_date date COMMENT '有效期至',
  cert_level varchar(50) COMMENT '证书等级',
  special_operation_type varchar(50) COMMENT '特种作业类型',
  attachment_path varchar(500) COMMENT '附件路径',
  status varchar(20) DEFAULT 'VALID' COMMENT '状态',
  review_date date COMMENT '复审日期',
  create_time datetime,
  update_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='证照管理表';

-- 安全培训记录表
CREATE TABLE hr_safety_training (
  training_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  training_type varchar(50) NOT NULL COMMENT '培训类型',
  training_name varchar(200) NOT NULL COMMENT '培训名称',
  training_date date NOT NULL COMMENT '培训日期',
  training_hours int NOT NULL COMMENT '培训学时',
  training_institution varchar(200) COMMENT '培训机构',
  trainer_name varchar(50) COMMENT '培训讲师',
  training_content text COMMENT '培训内容',
  assessment_result varchar(50) COMMENT '考核结果',
  certificate_no varchar(100) COMMENT '合格证编号',
  valid_until date COMMENT '有效期至',
  attachment_path varchar(500) COMMENT '附件路径',
  create_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='安全培训记录表';

-- 职业病健康档案表
CREATE TABLE hr_occupational_health (
  health_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  check_date date NOT NULL COMMENT '检查日期',
  check_type varchar(50) NOT NULL COMMENT '检查类型',
  check_institution varchar(200) COMMENT '检查机构',
  check_result varchar(500) COMMENT '检查结果',
  diagnosis_result varchar(500) COMMENT '诊断结果',
  suggestion text COMMENT '建议措施',
  next_check_date date COMMENT '下次检查日期',
  attachment_path varchar(500) COMMENT '附件路径',
  create_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id)
) COMMENT='职业病健康档案表';

-- 员工异动记录表
CREATE TABLE hr_employee_change (
  change_id bigint AUTO_INCREMENT PRIMARY KEY,
  employee_id bigint NOT NULL,
  change_type varchar(50) NOT NULL COMMENT '异动类型',
  change_date date NOT NULL COMMENT '异动日期',
  old_value varchar(500) COMMENT '原值',
  new_value varchar(500) COMMENT '新值',
  reason text COMMENT '异动原因',
  approver_id bigint COMMENT '审批人',
  approval_status varchar(20) DEFAULT 'PENDING' COMMENT '审批状态',
  approval_time datetime COMMENT '审批时间',
  remark text COMMENT '备注',
  create_time datetime,
  FOREIGN KEY (employee_id) REFERENCES hr_employee(employee_id),
  FOREIGN KEY (approver_id) REFERENCES sys_user(user_id)
) COMMENT='员工异动记录表';

-- 初始化基础数据
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