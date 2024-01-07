INSERT
INTO
  customer
  (first_name, last_name, email, username, password, image_url, role, authorities, customer_id, is_active, is_not_locked)
VALUES
  ('Tomasz', 'Borowski', 'tomeklfc@o2.pl', 'tomeee121', '$2a$10$Ap5kth52zKDx2Y4.oANBg.Ng9pE8s6KWHID3Ickiq0mwC4/hpZ5M2', 'http://localhost:8080/customer/image/profile/tomeee121', 'ROLE_ADMIN', 'user:read user:create user:delete user:update', 'a9407984-43bd-4db0-841a-b2aa70b43d21', 1, 1);