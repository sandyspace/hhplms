insert into hhplms.ana_employee
(login_name, real_name, password, email, mobile, gender, title, status, created_by, created_time, version_num)
values
('boss', '大老板', '$2a$10$XY3C1DFdd69ndPdUYoupJe4ffFwjs3E/Lho6Af8Bcs.G.8Lj2tc.u', '19009790@qq.com', '13971560750', 'male', '董事长', 'active', 'default', now(), 1);

insert into hhplms.ana_role
(code, name, category, type, company_info_sid, status, created_by, created_time, version_num)
values
('admin', '管理员', 'employee', 'pre-assigned', -1, 'enabled', 'default', now(), '1');

insert into hhplms.ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('employeeMgmt', '雇员管理', '/employee', '/employee/list', NULL, 'Y', 'N', 'Y', 'user', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('roleMgmt', '角色管理', '/role', '/role/list', NULL, 'Y', 'N', 'Y', 'lock', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('accountMgmt', '账号管理', '/account', '/account/list', NULL, 'Y', 'N', 'Y', 'people', '1', 'page', 'enabled', NULL, 'default', now(), '1');

insert into hhplms.ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('employeeList', '雇员列表', 'list', NULL, 'employee/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('employeeCreation', '雇员创建', 'create', NULL, 'employee/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('employeeEdit', '雇员编辑', 'edit/:id', NULL, 'employee/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('employeeDetail', '雇员详情', 'detail/:id', NULL, 'employee/detail', 'Y', 'Y', 'Y', 'documentation', '2', 'page', 'enabled', '1', 'default', now(), '1'),
('roleList', '角色列表', 'list', NULL, 'role/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('roleCreation', '角色创建', 'create', NULL, 'role/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('roleEdit', '角色编辑', 'edit/:id', NULL, 'role/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '2', 'default', now(), '1'),
('accountList', '账号列表', 'list', NULL, 'account/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('accountCreation', '账号创建', 'create', NULL, 'account/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('accountEdit', '账号编辑', 'edit/:id', NULL, 'account/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '3', 'default', now(), '1'),
('accountDetail', '账号详情', 'detail/:id', NULL, 'account/detail', 'Y', 'Y', 'Y', 'documentation', '2', 'page', 'enabled', '3', 'default', now(), '1');

insert into hhplms.ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('addEmployee', '创建雇员', '/ana/employees', NULL, '', NULL, NULL, NULL, NULL, '1', 'api', 'enabled', NULL, 'default', now(), '1'),
('editEmployee', '修改雇员', '/ana/employees/:id', NULL, '', NULL, NULL, NULL, NULL, '1', 'api', 'enabled', NULL, 'default', now(), '1');

insert into hhplms.ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('pendingItemMgmt', '代办事项管理', '/pendingItem', '/pendingItem/list', NULL, 'Y', 'N', 'Y', 'excel', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('preferentialMsgMgmt', '优惠信息管理', '/preferentialMsg', '/preferentialMsg/list', NULL, 'Y', 'N', 'Y', 'message', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('companyInfoMgmt', '企业管理', '/companyInfo', '/companyInfo/list', NULL, 'Y', 'N', 'Y', 'peoples', '1', 'page', 'enabled', NULL, 'default', now(), '1'),
('feedbackMgmt', '留言管理', '/feedback', '/feedback/list', NULL, 'Y', 'N', 'Y', 'star', '1', 'page', 'enabled', NULL, 'default', now(), '1');

insert into hhplms.ana_permission
(name, title, path, redirect_path, component_url, no_cache_flag, hidden_flag, always_show_flag, icon, level, type, status, parent_sid, created_by, created_time, version_num)
values
('pendingItemList', '代办事项列表', 'list', NULL, 'pendingItem/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '17', 'default', now(), '1'),
('preferentialMsgList', '优惠息息列表', 'list', NULL, 'preferentialMsg/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '18', 'default', now(), '1'),
('preferentialMsgCreation', '优惠息息创建', 'create', NULL, 'preferentialMsg/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '18', 'default', now(), '1'),
('preferentialMsgEdit', '优惠息息编辑', 'edit/:id', NULL, 'preferentialMsg/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '18', 'default', now(), '1'),
('companyInfoList', '企业列表', 'list', NULL, 'companyInfo/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '19', 'default', now(), '1'),
('companyInfoCreation', '企业创建', 'create', NULL, 'companyInfo/create', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '19', 'default', now(), '1'),
('companyInfoEdit', '企业编辑', 'edit/:id', NULL, 'companyInfo/edit', 'Y', 'Y', 'Y', 'edit', '2', 'page', 'enabled', '19', 'default', now(), '1'),
('feedbackList', '留言列表', 'list', NULL, 'feedback/list', 'Y', 'N', 'Y', 'list', '2', 'page', 'enabled', '20', 'default', now(), '1');

insert into hhplms.ana_employee_r2_role
(employee_sid, role_sid, created_by, created_time, version_num)
values
(1, 1, 'default', now(), '1');

insert into hhplms.ana_role_r2_permission
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
(1, 28, 'default', now(), '1');


insert into hhplms.wf_process
(code, name, `desc`, created_by, created_time, version_num)
values
('P-QYXXSQ', '企业信息上传流程', '企业信息上传流程', 'default', now(), 1);

insert into hhplms.wf_step
(code, name, `desc`, created_by, created_time, version_num)
values
('S-CJQYXX', '创建企业信息', '创建企业信息', 'default', now(), 1);

insert into hhplms.wf_route
(process_sid, from_step_sid, to_step_sid, assigned_type, assigned_to, start_flag, related_view, view_on_checking, attached_biz, created_by, created_time, version_num)
values
(1, 1, null, 'role', 'admin', 'Y', 'company-info-display', 'check-company-info', 'ccic', 'default', now(), 1);





