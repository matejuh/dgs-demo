DROP TABLE IF EXISTS planes;
CREATE TABLE planes (
    sign VARCHAR(255) PRIMARY KEY,
    type VARCHAR(255) NOT NULL
);

INSERT INTO planes values ('OK-7761', 'Duo Discus');
INSERT INTO planes values ('OK-JSC', 'Z-42');
INSERT INTO planes values ('OK-PMT', 'MS-893 Morane');
INSERT INTO planes values ('OK-ZCA', 'Z-526L');
INSERT INTO planes values ('OK-3118', 'LS-8-18');
INSERT INTO planes values ('OK-1272', 'ASW-15B');
INSERT INTO planes values ('OK-7241', 'Std. Cirrus');
INSERT INTO planes values ('OK-6400', 'Std. Cirrus');
INSERT INTO planes values ('OK-PUS19', 'WT-9 Dynamic');
INSERT INTO planes values ('OK-4215', 'L-23');
INSERT INTO planes values ('OK-3800', 'L-13');

DROP TABLE IF EXISTS flights;
CREATE TABLE flights (
                         id SERIAL PRIMARY KEY,
                         plane VARCHAR(255) NOT NULL REFERENCES planes(sign),
                         start_time TIMESTAMP NOT NULL,
                         landing_time TIMESTAMP NOT NULL
);

INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-6400', '2023-11-21 01:09:50', '2023-11-21 06:09:50');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-1272', '2023-05-01 04:36:05', '2023-05-01 09:08:05');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-04-20 12:20:58', '2023-04-20 14:11:58');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-4215', '2023-10-25 16:10:31', '2023-10-25 16:35:31');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-3800', '2023-04-21 08:37:47', '2023-04-21 09:10:47');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-09-17 06:13:00', '2023-09-17 11:43:00');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-11-28 01:37:29', '2023-11-28 06:00:29');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-02-12 10:09:15', '2023-02-12 14:33:15');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-12-10 03:58:16', '2023-12-10 05:18:16');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-6400', '2023-07-22 19:46:14', '2023-07-22 21:26:14');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-04-10 14:35:27', '2023-04-10 19:23:27');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-ZCA', '2023-08-19 02:01:22', '2023-08-19 07:09:22');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PMT', '2023-10-12 10:24:15', '2023-10-12 12:14:15');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-11-12 16:42:25', '2023-11-12 17:51:25');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-09-24 09:20:07', '2023-09-24 13:42:07');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-06-25 20:10:14', '2023-06-26 00:43:14');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-1272', '2023-03-19 23:00:02', '2023-03-20 01:57:02');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-07-28 15:58:11', '2023-07-28 20:10:11');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-6400', '2023-07-07 16:58:50', '2023-07-07 17:07:50');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-03-29 19:24:50', '2023-03-29 20:35:50');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-4215', '2023-11-25 02:55:27', '2023-11-25 06:28:27');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-4215', '2023-08-17 10:21:21', '2023-08-17 15:37:21');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-12-18 16:25:30', '2023-12-18 16:41:30');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-12-20 03:13:40', '2023-12-20 06:17:40');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-3118', '2023-09-25 16:43:18', '2023-09-25 18:59:18');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-11-19 20:52:07', '2023-11-19 22:56:07');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-3800', '2023-12-24 19:34:15', '2023-12-24 20:09:15');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-JSC', '2023-04-29 01:46:59', '2023-04-29 06:43:59');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PMT', '2023-10-24 16:31:03', '2023-10-24 22:03:03');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-03-26 16:12:16', '2023-03-26 17:44:16');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-01-10 02:33:21', '2023-01-10 06:25:21');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-1272', '2023-12-18 01:26:16', '2023-12-18 02:04:16');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-02-24 17:53:05', '2023-02-24 18:53:05');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PMT', '2023-06-01 06:26:57', '2023-06-01 06:50:57');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-02-02 14:46:01', '2023-02-02 19:40:01');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-10-30 18:25:54', '2023-10-30 22:33:54');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-12-02 08:04:17', '2023-12-02 09:44:17');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-3118', '2023-09-26 04:36:29', '2023-09-26 06:34:29');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-04-20 19:23:45', '2023-04-20 21:31:45');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-3118', '2023-03-16 15:13:43', '2023-03-16 19:51:43');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-01-24 03:54:54', '2023-01-24 04:54:54');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PUS19', '2023-06-13 12:33:30', '2023-06-13 15:31:30');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7241', '2023-12-14 03:52:12', '2023-12-14 04:15:12');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-7761', '2023-08-19 00:56:45', '2023-08-19 05:39:45');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-PMT', '2023-12-13 15:53:57', '2023-12-13 17:48:57');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-6400', '2023-03-16 21:28:02', '2023-03-17 02:00:02');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-JSC', '2023-03-17 02:11:35', '2023-03-17 02:47:35');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-JSC', '2023-12-23 12:33:23', '2023-12-23 14:11:23');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-ZCA', '2023-07-14 19:06:47', '2023-07-14 21:02:47');
INSERT INTO flights (plane, start_time, landing_time) VALUES ('OK-1272', '2023-04-13 09:31:16', '2023-04-13 12:30:16');
