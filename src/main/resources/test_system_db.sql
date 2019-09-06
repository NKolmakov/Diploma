drop database if exists test_system;

create database if not exists test_system;

use test_system;

create table if not exists `role`(
	id bigint unsigned not null auto_increment primary key,
    name varchar(20) not null
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists user_group(
	id bigint unsigned not null auto_increment primary key,
    name varchar(20) not null
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists user(
	id bigint unsigned not null auto_increment primary key,
    name varchar(20) not null,
    surname varchar(20) not null,
    login varchar(30) not null,
    password varchar(60) not null,
    role_id bigint unsigned,
    user_group_id bigint unsigned,

    foreign key (role_id) references role(id)
    on update cascade
    on delete set null,

    foreign key (user_group_id) references user_group(id)
    on update cascade
    on delete set null
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists subject(
	id bigint unsigned not null auto_increment primary key,
    name varchar(70) not null
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists test(
	id bigint unsigned not null auto_increment primary key,
    name varchar(70) not null,
    description varchar(255),
    subject_id bigint unsigned,
    allotted_time tinyint,
    user_id bigint unsigned,

    foreign key(subject_id) references subject(id)
    on update cascade
    on delete set null,

    foreign key(user_id) references user(id)
    on update cascade
    on delete set null
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists passing_test(
	id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned,
    test_id bigint unsigned,
    `date` date,
    start_time time,
    end_time time,
    common_question_amount smallint,
    correct_question_amount smallint,
    uncorrect_question_amount smallint,

    foreign key(user_id) references user(id)
    on update cascade
    on delete cascade,

    foreign key(test_id) references test(id)
    on update cascade
    on delete cascade
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists question(
	id bigint unsigned not null auto_increment primary key,
    name varchar(255) not null,
    test_id bigint unsigned,

    foreign key(test_id) references test(id)
    on update cascade
    on delete cascade
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists answer(
	id bigint unsigned not null auto_increment primary key,
    name varchar(255) not null,
    question_id bigint unsigned,
	correct bool,

    foreign key(question_id) references question(id)
    on update cascade
    on delete cascade
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

create table if not exists answer_log(
	id bigint unsigned not null auto_increment primary key,
    passing_test_id bigint unsigned,
    question_id bigint unsigned,
    answer_id bigint unsigned,
    is_right bool,

	foreign key(passing_test_id) references passing_test(id)
    on update cascade
    on delete cascade,

    foreign key(question_id) references question(id)
    on update cascade
    on delete cascade,

    foreign key(answer_id) references answer(id)
    on update cascade
    on delete cascade
)
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
