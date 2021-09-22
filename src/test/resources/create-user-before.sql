delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
(1, 'admin', '$2a$08$UqciZ/o30mWgCbCPmFTeWOHpEfTJhSnBLCnFgKoXRBJgM2SUFbHNK', true),
(2, 'testUSER', '$2a$08$e4ri5dfxD6bEbNms69PCm.ZCZ7B6b8MvsNLKHv8RNQW.ew50i7GIm', true);

insert into user_role(user_id, roles) values
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER');