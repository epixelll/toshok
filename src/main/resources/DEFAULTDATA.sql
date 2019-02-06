insert into users (username, password, role_id) values ('admin', '$2a$10$nh8bym652FGvklMMgCu.Eu0KIxpTpJtLhBvtYfp798eE/mBMuZ7FC', 1)

insert into roles_permissions(role_id, permission) values
  (1, 'ACCOUNT_VIEW'),
  (1, 'ACCOUNT_CREATE'),
  (1, 'ACCOUNT_UPDATE'),
  (1, 'ACCOUNT_DELETE'),
  (1, 'APPROVE_ACCOUNT'),
  (1, 'USER_MANAGEMENT'),
  (1, 'REPORTS');

insert into roles_permissions(role_id, permission) values
  (2, 'ACCOUNT_VIEW'),
  (2, 'ACCOUNT_CREATE'),
  (2, 'ACCOUNT_UPDATE');