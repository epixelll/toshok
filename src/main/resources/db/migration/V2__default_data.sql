insert into roles(name)
values ('ADMIN'),
('OPERATOR'),
('MEMBER');

insert into regions(name)
values ('Бишкек шаары'),
('Ош шаары'),
('Баткен'),
('Жалал-Абад'),
('Нарын'),
('Чүй'),
('Талас'),
('Ош'),
('Ыссык-Көл');

insert into roles_permissions(role_id, permission) values
  (1, 'ACCOUNT_VIEW'),
  (1, 'ACCOUNT_CREATE'),
  (1, 'ACCOUNT_UPDATE'),
  (1, 'ACCOUNT_DELETE'),
  (1, 'APPROVE_ACCOUNT'),
  (1, 'USER_MANAGEMENT'),
  (1, 'GIFT_MANAGEMENT'),
  (1, 'REPORTS');

insert into roles_permissions(role_id, permission) values
  (2, 'ACCOUNT_VIEW'),
  (2, 'ACCOUNT_CREATE'),
  (2, 'ACCOUNT_UPDATE'),
  (2, 'GIFT_MANAGEMENT');