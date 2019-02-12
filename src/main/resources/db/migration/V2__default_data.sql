insert into roles(name)
values ('ADMIN'),
('OPERATOR'),
('MEMBER');

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

insert into regions(name)
values
('Баткен шаары'),
('Исфана'),
('Кадамжай'),
('Кызыл-Кыя'),

('Жалал-Абад шаары'),
('Ала-Бука'),
('Ташкомур'),
('Токтогул'),
('Чаткал'),
('Базар-Коргон'),
('Майлу-Суу'),
('Кочкор-Ата'),

('Кара-Кол шаары'),
('Ак-Суу'),
('Жети-Огуз'),
('Туп'),
('Тон'),

('Нарын шаары'),
('Ак-Таала'),
('Ат-Башы'),
('Жумгал'),
('Кочкор'),
('Чаек'),

('Ош шаары'),
('Алай'),
('Ноокат'),
('Озгон'),
('Чон-Алай'),

('Талас шаары'),
('Бакай-Ата'),
('Манас'),
('Кара-Буура'),

('Бишкек шаары'),
('Токмок'),
('Кара-Балта'),

('Москва шаары (Россия)'),
('Новосибирск шаары');