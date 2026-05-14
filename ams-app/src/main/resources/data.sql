INSERT OR IGNORE INTO roles (id, role_name, role_code) VALUES (1, '超级管理员', 'SUPER_ADMIN');
INSERT OR IGNORE INTO roles (id, role_name, role_code) VALUES (2, '管理员', 'ADMIN');
INSERT OR IGNORE INTO roles (id, role_name, role_code) VALUES (3, '普通用户', 'USER');

-- 默认密码: admin123 (bcrypt)
INSERT OR IGNORE INTO users (id, username, password, role_id) VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 1);
