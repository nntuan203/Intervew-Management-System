# CREATE SCHEMA `dev_ims_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;
# set password 123
# CREATE DATABASE IF NOT EXISTS dev_ims_db;
-- admin/admin

INSERT INTO highestlevel(highest_desc)
VALUES ('High school'),
       ('Bachelor’s Degree'),
       ('Master Degree, PhD');


INSERT INTO skill (skill_desc)
VALUES ('Java'),
       ('Nodejs'),
       ('.net'),
       ('C++'),
       ('Business analysis'),
       ('Communication');


INSERT INTO `position` (position_name)
VALUES ('Backend Developer'),
       ('Business Analyst'),
       ('Tester'),
       ('HR'),
       ('Project manager');


INSERT INTO department (department_name)
VALUES ('IT'),
       ('HR'),
       ('Finance'),
       ('Communication'),
       ('Marketing'),
       ('Accounting');


INSERT INTO joblevel (level_desc)
VALUES  ('Fresher'),
        ('Junior 2.1'),
        ('Junior 2.2'),
        ('Senior 3.1'),
        ('Senior 3.2'),
        ('Delivery'),
        ('Leader'),
        ('Manager'),
        ('Vice Head');

INSERT INTO candidatestatus (status_name,stage)
VALUES ('Open','candidate'),
       ('Waiting for interview','interview'),
       ('In-progress','interview'),
       ('Cancelled interview','interview'),
       ('Passed Interview', 'interview'),
       ('Failed interview', 'interview'),
       ('Waiting for approval','offer'),
       ('Approved offer','offer'),
       ('Rejected offer','offer'),
       ('Waiting for response','offer'),
       ('Accepted offer','offer'),
       ('Declined offer','offer'),
       ('Cancelled offer','offer'),
       ('Banned','candidate');

INSERT INTO `role`(role_desc)
VALUES ('ROLE_ADMIN'),
       ('ROLE_RECRUITER'),
       ('ROLE_INTERVIEWER'),
       ('ROLE_MANAGER');

INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(1, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'anNV@gmail.com', 'Nguyen Van An', 'Male', 'anNV', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0123456789', 'Activated', 1, 1, '17 Duy Tan');
INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(2, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'binhNV@gmail.com', 'Nguyen Van Binh', 'Male', 'binhNV', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 2, 2, '17 Duy Tan');
INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(3, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'dungNV@gmail.com', 'Nguyen Van Dung', 'Male', 'dungNV', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 3, 3, '17 Duy Tan');
INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(4, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'caoNV@gmail.com', 'Nguyen Van Cao', 'Male', 'caoNV', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 4, 4, '17 Duy Tan');
INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(5, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'ninhNV@gmail.com', 'Nguyen Van Ninh', 'Male', 'ninhNV', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 2, 2, '18 Duy Tan');



########################################
# Add data to demo

# Add user theo role
INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(6, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'tuanNN@gmail.com', 'Nguyen Nhu Tuan admin', 'Male', 'admin', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 2, 1, '18 Duy Tan');

INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(7, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'huyNN@gmail.com', 'Nguyen Quang Huy recruiter', 'Male', 'recruiter', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 2, 2, '18 Duy Tan');

INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(8, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'dungNN@gmail.com', 'Duong Tan Dung interviewer', 'Male', 'interviewer', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 2, 3, '18 Duy Tan');

INSERT INTO user
(user_id, created_by, created_date, last_modified_by, last_modified_date, deleted, user_date_birth, user_email, user_fullname, user_gender, user_username, user_note, user_password, user_phone, user_status, department_id, role_id, user_address)
VALUES(9, 'System', '2022-09-25 00:00:00', 'System', '2022-09-25 00:00:00', 0, '2020-12-09', 'datNN@gmail.com', 'Tran Thanh Dat manager', 'Male', 'manager', NULL, '$2a$10$tp0EsLH4FWcrn20HxN63zu9hfinFGxngZXDSwBwL1wwuH7IiFj0s6', '0987654321', 'Activated', 2, 4, '18 Duy Tan');

#Add candidate

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('1', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address1', 'cdung178@gmail.com/IMG_2452.jpg', 'candidate1@gmail.com', 'Nguyen Van Candidate1', 'Male', 'Candidate note 1', '0123456789', '1', '1', '3', '1', '2');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('2', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address2', 'dung178@gmail.com/IMG_2452.jpg', 'candidate2@gmail.com', 'Nguyen Van Candidate2', 'Male', 'Candidate note 2', '0123456789', '1', '1', '3', '1', '2');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('3', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address3', 'dung178@gmail.com/IMG_2452.jpg', 'candidate3@gmail.com', 'Nguyen Van Candidate3', 'Male', 'Candidate note 3', '0123456789', '1', '1', '3', '1', '2');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('4', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address4', 'dung178@gmail.com/IMG_2452.jpg', 'candidate4@gmail.com', 'Nguyen Van Candidate4', 'Male', 'Candidate note 4', '0123456789', '1', '1', '1', '1', '7');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('5', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address4', 'dung178@gmail.com/IMG_2452.jpg', 'candidate5@gmail.com', 'Nguyen Van Candidate5', 'Male', 'Candidate note 5', '0123456789', '1', '1', '1', '1', '7');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('6', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address4', 'dung178@gmail.com/IMG_2452.jpg', 'candidate6@gmail.com', 'Nguyen Van Candidate6', 'Male', 'Candidate note 6', '0123456789', '1', '1', '1', '1', '7');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('7', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address4', 'dung178@gmail.com/IMG_2452.jpg', 'candidate7@gmail.com', 'Nguyen Van Candidate7', 'Male', 'Candidate note 7', '0123456789', '1', '1', '1', '1', '7');

INSERT INTO `dev_ims_db`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `candidate_address`, `candidate_cv`, `candidate_email`, `candidate_fullname`, `candidate_gender`, `candidate_note`, `candidate_phone_number`, `candidate_yoe`, `status_id`, `highest_id`, `position_id`, `user_id`)
VALUES ('8', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0, 'address8', 'dung178@gmail.com/IMG_2452.jpg', 'candidate8@gmail.com', 'Nguyen Van Candidate8', 'Male', 'Candidate note 8', '0123456789', '1', '1', '1', '1', '7');

#Add job

INSERT INTO `dev_ims_db`.`joblist`
(`job_id`,`created_by`,`created_date`,`last_modified_by`,`last_modified_date`,`deleted`,`job_address`,`job_description`,`job_end_date`,`job_salary_from`,`job_salary_to`,`job_start_date`,`job_status`,`job_title`)
VALUES ('1','anNV','2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0,'job address1','job description 1','2023-12-12','10','100','2023-06-12','OPEN','job title 1');

INSERT INTO `dev_ims_db`.`joblist`
(`job_id`,`created_by`,`created_date`,`last_modified_by`,`last_modified_date`,`deleted`,`job_address`,`job_description`,`job_end_date`,`job_salary_from`,`job_salary_to`,`job_start_date`,`job_status`,`job_title`)
VALUES ('2','anNV','2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0,'job address2','job description 2','2023-12-12','10','100','2023-06-12','OPEN','job title 2');

INSERT INTO `dev_ims_db`.`joblist`
(`job_id`,`created_by`,`created_date`,`last_modified_by`,`last_modified_date`,`deleted`,`job_address`,`job_description`,`job_end_date`,`job_salary_from`,`job_salary_to`,`job_start_date`,`job_status`,`job_title`)
VALUES ('3','anNV','2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0,'job address3','job description 3','2023-12-12','100','1000','2023-06-12','OPEN','job title 3');

INSERT INTO `dev_ims_db`.`joblist`
(`job_id`,`created_by`,`created_date`,`last_modified_by`,`last_modified_date`,`deleted`,`job_address`,`job_description`,`job_end_date`,`job_salary_from`,`job_salary_to`,`job_start_date`,`job_status`,`job_title`)
VALUES ('4','anNV','2023-04-12 11:07:25', 'anNV', '2023-04-12 11:07:25', 0,'job address4','job description 4','2023-12-12','100','1000','2023-06-12','OPEN','job title 4');

INSERT INTO job_skills
(job_id,skill_id)
VALUES (1,1);

INSERT INTO job_skills
(job_id,skill_id)
VALUES (1,2);

INSERT INTO job_skills
(job_id,skill_id)
VALUES (2,2);

INSERT INTO job_skills
(job_id,skill_id)
VALUES (3,1);

INSERT INTO job_skills
(job_id,skill_id)
VALUES (3,3);

INSERT INTO job_skills
(job_id,skill_id)
VALUES (4,3);

#Add interview
INSERT INTO `dev_ims_db`.`interview_schedule` (`schedule_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `interview_end_time`, `schedule_location`, `schedule_meeting_id`, `schedule_notes`, `schedule_result`, `interview_start_time`, `schedule_status`, `schedule_title`, `candidate_id`, interview_date)
VALUES ('1', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-05-12 11:07:25', 0, '2023-01-20 11:07:25', '1', '1', 'interview note 1', 'OPEN', '2023-01-20 10:07:25', 'WAITING_FOR_INTERVIEW', 'interview title 1', '1','2023-01-20');

INSERT INTO `dev_ims_db`.`interview_schedule` (`schedule_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `interview_end_time`, `schedule_location`, `schedule_meeting_id`, `schedule_notes`, `schedule_result`, `interview_start_time`, `schedule_status`, `schedule_title`, `candidate_id`, interview_date)
VALUES ('2', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-05-12 11:07:25', 0, '2023-05-20 11:07:25', '1', '1', 'interview note 2', 'OPEN', '2023-05-20 10:07:25', 'WAITING_FOR_INTERVIEW', 'interview title 2', '2','2023-05-20');

INSERT INTO `dev_ims_db`.`interview_schedule` (`schedule_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `deleted`, `interview_end_time`, `schedule_location`, `schedule_meeting_id`, `schedule_notes`, `schedule_result`, `interview_start_time`, `schedule_status`, `schedule_title`, `candidate_id`, interview_date)
VALUES ('3', 'anNV', '2023-04-12 11:07:25', 'anNV', '2023-05-12 11:07:25', 0, '2023-05-20 11:07:25', '1', '1', 'interview note 2', 'OPEN', '2023-05-20 10:07:25', 'WAITING_FOR_INTERVIEW', 'interview title 2', '3','2023-05-20');

INSERT INTO `dev_ims_db`.`interview_member` (`schedule_id`, `user_id`)
VALUES ('1', '3');

INSERT INTO `dev_ims_db`.`interview_member` (`schedule_id`, `user_id`)
VALUES ('1', '8');

INSERT INTO `dev_ims_db`.`interview_member` (`schedule_id`, `user_id`)
VALUES ('2', '8');

INSERT INTO `dev_ims_db`.`interview_member` (`schedule_id`, `user_id`)
VALUES ('3', '8');