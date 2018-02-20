INSERT INTO `role` (`name`) VALUES
("ADMIN"),
("MANAGER"),
("STAFF");

INSERT INTO `permission` (`name`) VALUES
("CREATE_USER"),
("LIST_DATA"),
("APPROVE");

INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(1, 1),(1, 2),(1, 3),
(2, 2),(2, 3),
(3, 2);
