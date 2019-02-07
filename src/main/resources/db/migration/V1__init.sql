create table roles(
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

create table roles_permissions(
  role_id BIGINT NOT NULL REFERENCES roles,
  permission VARCHAR NOT NULL
);

create table regions(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL
);

create table accounts(
  id SERIAL PRIMARY KEY,
  status VARCHAR(100) NOT NULL,
  fullname VARCHAR(150) NOT NULL,
  address VARCHAR(150),
  check_number VARCHAR,
  passport_number VARCHAR(150),
  phone_number VARCHAR(30),
  registered_date DATE,
  region_id BIGINT REFERENCES regions,
  parent_id BIGINT REFERENCES accounts,
  level INT NOT NULL default 1,
  gift_given_for_level INT NOT NULL default 0
);

create table users(
  id SERIAL PRIMARY KEY,
  username VARCHAR(30) NOT NULL UNIQUE,
  password VARCHAR(150) NOT NULL,
  role_id BIGINT NOT NULL REFERENCES roles,
  account_id BIGINT REFERENCES accounts
);