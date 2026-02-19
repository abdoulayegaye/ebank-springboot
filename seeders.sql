INSERT INTO `roles` (`id`, `created_at`, `updated_at`, `user_created`, `user_updated`, `description`, `name`) VALUES
                                                                                                                     (1, NULL, NULL, NULL, NULL, 'Super Administrateur', 'SUPER_ADMIN'),
                                                                                                                     (2, NULL, NULL, NULL, NULL, 'Administrateur', 'ADMIN'),
                                                                                                                     (3, NULL, NULL, NULL, NULL, 'Utilisateur', 'USER');

INSERT INTO `users` (`id`, `created_at`, `is_enabled`, `updated_at`, `user_created`, `user_updated`, `account_is_enabled`, `account_is_not_expired`, `account_is_not_locked`, `credential_not_expired`, `email`, `firstname`, `is_admin`, `lastname`, `password`, `phone`, `username`, `role_id`) VALUES
    (1, '2023-07-10 00:59:03.000000', b'1', '2023-07-10 00:59:03.000000', NULL, NULL, b'1', b'1', b'1', b'1', 'layegaye001@gmail.com', 'Abdoulaye', b'1', 'GAYE', '$2a$10$eUCiaM3s4SdWAJQ9CXyFS.bRpQ0m.GZhKwAuv.3KHKA0SRKS9sjuy', '771800510', 'admin', 1);