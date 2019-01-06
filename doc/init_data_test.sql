insert into ana_employee
(login_name, real_name, password, email, mobile, gender, title, status, created_by, created_time, version_num)
values
('cbgj', '船舶国际', '$2a$10$XY3C1DFdd69ndPdUYoupJe4ffFwjs3E/Lho6Af8Bcs.G.8Lj2tc.u', 'shansong@outlook.com', '18502701010', 'male', '超级用户', 'active', 'default', now(), 1);

insert into ana_role
(code, name, category, type, company_info_sid, status, memo, created_by, created_time, version_num)
values
('super_admin', '超级管理员', 'employee', 'pre-assigned', null, 'enabled', '拥有最高系统权限的角色', 'default', now(), '1');

insert into ana_template_role
(code, name, type, status, memo, created_by, created_time, version_num)
values
('company_admin', '企业业主', 'owner', 'enabled', '企业业主角色模板，用于在企业信息审批通过以后自动创建企业的企业主角色', 'default', now(), '1'),
('company_manager', '企业经理', 'manager', 'enabled', '企业经理角色模板，用于在企业信息审批通过以后自动创建企业的经理角色', 'default', now(), '1'),
('company_employee', '企业员工', 'normal', 'enabled', '企业员工角色模板，用于在企业信息审批通过以后自动创建企业的员工角色', 'default', now(), '1');

insert into ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('employeeMgmt', '物业人员管理', '/employee', '/employee/list', NULL, 'Y', 'N', 'Y', 'user', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('accountMgmt', '企业人员管理', '/account', '/account/list', NULL, 'Y', 'N', 'Y', 'people', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('roleMgmt', '角色管理', '/role', '/role/list', NULL, 'Y', 'N', 'Y', 'lock', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('companyInfoMgmt', '企业管理', '/companyInfo', '/companyInfo/list', NULL, 'Y', 'N', 'Y', 'peoples', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('pendingItemMgmt', '代办事项管理', '/pendingItem', '/pendingItem/list', NULL, 'Y', 'N', 'Y', 'excel', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('preferentialMsgMgmt', '优惠信息管理', '/preferentialMsg', '/preferentialMsg/list', NULL, 'Y', 'N', 'Y', 'message', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('feedbackMgmt', '留言管理', '/feedback', '/feedback/list', NULL, 'Y', 'N', 'Y', 'star', '1', 'page', 'enabled', NULL, 'default', now(), '1');

insert into ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('employeeList', '物业人员列表', 'list', NULL, 'employee/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('employeeCreation', '物业人员创建', 'create', NULL, 'employee/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('employeeEdit', '物业人员编辑', 'edit/:id', NULL, 'employee/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('employeeDetail', '物业人员详情', 'detail/:id', NULL, 'employee/detail', 'Y', 'Y', 'Y', 'documentation', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('accountList', '企业人员列表', 'list', NULL, 'account/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('accountCreation', '企业人员创建', 'create', NULL, 'account/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('accountEdit', '企业人员编辑', 'edit/:id', NULL, 'account/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('accountDetail', '企业人员详情', 'detail/:id', NULL, 'account/detail', 'Y', 'Y', 'Y', 'documentation', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('roleList', '角色权限列表', 'list', NULL, 'role/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('roleCreation', '角色创建', 'create', NULL, 'role/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('roleEdit', '角色编辑', 'edit/:id', NULL, 'role/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('tempRoleList', '角色模板列表', 'list', NULL, 'tempRole/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('tempRoleCreation', '角色模板创建', 'create', NULL, 'tempRole/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('tempRoleEdit', '角色模板编辑', 'edit/:id', NULL, 'tempRole/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('companyInfoList', '企业列表', 'list', NULL, 'companyInfo/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '4', 'default', now(), '1'),
('companyInfoCreation', '企业创建', 'create', NULL, 'companyInfo/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '4', 'default', now(), '1'),
('companyInfoEdit', '企业编辑', 'edit/:id', NULL, 'companyInfo/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '4', 'default', now(), '1'),
('pendingItemList', '代办事项列表', 'list', NULL, 'pendingItem/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '5', 'default', now(), '1'),
('preferentialMsgList', '优惠息息列表', 'list', NULL, 'preferentialMsg/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '6', 'default', now(), '1'),
('preferentialMsgCreation', '优惠息息创建', 'create', NULL, 'preferentialMsg/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '6', 'default', now(), '1'),
('preferentialMsgEdit', '优惠息息编辑', 'edit/:id', NULL, 'preferentialMsg/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '6', 'default', now(), '1'),
('feedbackList', '留言列表', 'list', NULL, 'feedback/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '7', 'default', now(), '1');

insert into ana_role_r2_permission
(role_sid, permission_sid, created_by, created_time, version_num)
values
(1, 1, 'default', now(), '1'),
(1, 2, 'default', now(), '1'),
(1, 3, 'default', now(), '1'),
(1, 4, 'default', now(), '1'),
(1, 5, 'default', now(), '1'),
(1, 6, 'default', now(), '1'),
(1, 7, 'default', now(), '1'),
(1, 8, 'default', now(), '1'),
(1, 9, 'default', now(), '1'),
(1, 10, 'default', now(), '1'),
(1, 11, 'default', now(), '1'),
(1, 12, 'default', now(), '1'),
(1, 13, 'default', now(), '1'),
(1, 14, 'default', now(), '1'),
(1, 15, 'default', now(), '1'),
(1, 16, 'default', now(), '1'),
(1, 17, 'default', now(), '1'),
(1, 18, 'default', now(), '1'),
(1, 19, 'default', now(), '1'),
(1, 20, 'default', now(), '1'),
(1, 21, 'default', now(), '1'),
(1, 22, 'default', now(), '1'),
(1, 23, 'default', now(), '1'),
(1, 24, 'default', now(), '1'),
(1, 25, 'default', now(), '1'),
(1, 26, 'default', now(), '1'),
(1, 27, 'default', now(), '1'),
(1, 28, 'default', now(), '1'),
(1, 29, 'default', now(), '1');

insert into ana_template_role_r2_permission
(template_role_sid, permission_sid, created_by, created_time, version_num)
values
(1, 2, 'default', now(), '1'),
(1, 3, 'default', now(), '1'),
(1, 5, 'default', now(), '1'),
(1, 6, 'default', now(), '1'),
(1, 12, 'default', now(), '1'),
(1, 13, 'default', now(), '1'),
(1, 14, 'default', now(), '1'),
(1, 15, 'default', now(), '1'),
(1, 16, 'default', now(), '1'),
(1, 17, 'default', now(), '1'),
(1, 18, 'default', now(), '1'),
(1, 25, 'default', now(), '1'),
(1, 26, 'default', now(), '1'),
(1, 27, 'default', now(), '1'),
(1, 28, 'default', now(), '1');

insert into ana_template_role_r2_permission
(template_role_sid, permission_sid, created_by, created_time, version_num)
values
(2, 2, 'default', now(), '1'),
(2, 5, 'default', now(), '1'),
(2, 6, 'default', now(), '1'),
(2, 12, 'default', now(), '1'),
(2, 13, 'default', now(), '1'),
(2, 14, 'default', now(), '1'),
(2, 15, 'default', now(), '1'),
(2, 25, 'default', now(), '1'),
(2, 26, 'default', now(), '1'),
(2, 27, 'default', now(), '1'),
(2, 28, 'default', now(), '1');

insert into ana_employee_r2_role
(employee_sid, role_sid, created_by, created_time, version_num)
values
(1, 1, 'default', now(), '1');

insert into wf_process
(code, name, `desc`, owner, created_by, created_time, version_num)
values
('P-QYXXSQ', '企业信息上传流程', '用于审批注册用户上传的企业信息', 'employee', 'default', now(), 1),
('P-JRQY', '加入企业流程', '企业用于审批加入的用户', 'account', 'default', now(), 1);

insert into wf_step
(code, name, `desc`, created_by, created_time, version_num)
values
('S-CJQYXX', '企业信息上传', '企业信息上传', 'default', now(), 1),
('S-SHAJYH', '用户加入企业', '用户加入企业', 'default', now(), 1);

insert into wf_route
(process_sid, from_step_sid, to_step_sid, assigned_type, assigned_to, start_flag, related_view, view_on_checking, attached_biz, created_by, created_time, version_num)
values
(1, 1, null, 'role', 'process-checker', 'Y', 'company-info-display', 'check-company-info', 'ccic', 'default', now(), 1),
(2, 2, null, 'role', 'company-process-checker', 'Y', 'joining-account', 'check-joining-account', 'ccj', 'default', now(), 1);