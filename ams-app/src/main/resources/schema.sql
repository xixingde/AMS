CREATE TABLE IF NOT EXISTS roles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_name VARCHAR(32) NOT NULL,
    role_code VARCHAR(32) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS systems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sys_master_no VARCHAR(64) NOT NULL UNIQUE,
    sys_no VARCHAR(64) NOT NULL UNIQUE,
    sys_name_cn VARCHAR(128) NOT NULL UNIQUE,
    sys_name_en VARCHAR(128) NOT NULL UNIQUE,
    sys_alias VARCHAR(128),
    sys_type VARCHAR(32) NOT NULL,
    sys_owner VARCHAR(64) NOT NULL,
    sys_desc TEXT NOT NULL,
    split_from_other TINYINT DEFAULT 0,
    data_security_level VARCHAR(32) NOT NULL,
    sensitive_data_involved TINYINT NOT NULL,
    sensitive_data_label VARCHAR(128),
    sys_level VARCHAR(32) NOT NULL,
    protection_level VARCHAR(32) NOT NULL,
    apply_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sub_systems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sub_sys_master_no VARCHAR(64) NOT NULL UNIQUE,
    sub_sys_no VARCHAR(64) NOT NULL UNIQUE,
    sys_no VARCHAR(64) NOT NULL,
    sub_sys_name_cn VARCHAR(128) NOT NULL UNIQUE,
    sub_sys_name_en VARCHAR(128) NOT NULL UNIQUE,
    dev_mode VARCHAR(32) NOT NULL,
    sub_sys_level VARCHAR(32) NOT NULL,
    sub_sys_protection_level VARCHAR(32) NOT NULL,
    sub_sys_status VARCHAR(32) NOT NULL,
    birth_cert_no VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS deploy_sub_systems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    deploy_sys_no VARCHAR(64) NOT NULL UNIQUE,
    sub_sys_no VARCHAR(64) NOT NULL,
    deploy_sys_name_cn VARCHAR(128) NOT NULL UNIQUE,
    deploy_sys_desc TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS release_units (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    unit_no VARCHAR(64) NOT NULL UNIQUE,
    deploy_sys_no VARCHAR(64) NOT NULL,
    unit_name_cn VARCHAR(128) NOT NULL UNIQUE,
    unit_type VARCHAR(64) NOT NULL,
    network_zone VARCHAR(32) NOT NULL,
    deploy_mode VARCHAR(32) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tech_stacks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    stack_no VARCHAR(64) NOT NULL UNIQUE,
    category VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL UNIQUE,
    selection_advice VARCHAR(32) NOT NULL,
    responsible_line VARCHAR(32) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS release_unit_tech_stack (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    unit_no VARCHAR(64) NOT NULL,
    stack_no VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(unit_no, stack_no)
);

CREATE TABLE IF NOT EXISTS release_unit_relations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    deploy_sys_no VARCHAR(64) NOT NULL,
    caller_unit_no VARCHAR(64) NOT NULL,
    callee_unit_no VARCHAR(64) NOT NULL,
    protocol VARCHAR(16) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    UNIQUE(deploy_sys_no, caller_unit_no, callee_unit_no)
);

CREATE TABLE IF NOT EXISTS diagram_layouts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    deploy_sys_no VARCHAR(64) NOT NULL UNIQUE,
    layout_data TEXT NOT NULL,
    remark TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
