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
  (1, 'ACCOUNT_UPDATE_APPROVED'),
  (1, 'ACCOUNT_DELETE_APPROVED'),
  (1, 'ACCOUNT_APPROVE'),
  (1, 'USER_VIEW'),
  (1, 'USER_CREATE_ALL'),
  (1, 'USER_UPDATE_ALL'),
  (1, 'USER_DELETE_ALL'),
  (1, 'GIFT_VIEW');

insert into roles_permissions(role_id, permission) values
  (2, 'ACCOUNT_VIEW'),
  (2, 'USER_VIEW'),
  (2, 'GIFT_VIEW');