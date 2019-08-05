create database if not exists test_system;

use test_system;

create table if not exists `role`(
	id bigint unsigned not null auto_increment primary key,
    name varchar(20) not null,
    `read` bool,
    pass_test bool,
    create_test bool,
    super_user bool
);

create table if not exists `group`(
	id bigint unsigned not null auto_increment primary key,
    name varchar(20) not null
);

create table if not exists user(
	id bigint unsigned not null auto_increment primary key,
    name varchar(20) not null,
    surname varchar(20) not null,
    login varchar(30) not null,
    password varchar(50) not null,
    role_id bigint unsigned,
    group_id bigint unsigned,

    foreign key (role_id) references role(id)
    on update cascade
    on delete set null,

    foreign key (group_id) references `group`(id)
    on update cascade
    on delete set null
);

create table if not exists subject(
	id bigint unsigned not null auto_increment primary key,
    name varchar(70) not null
);

create table if not exists test(
	id bigint unsigned not null auto_increment primary key,
    name varchar(70) not null,
    description varchar(255),
    subject_id bigint unsigned,

    foreign key(subject_id) references subject(id)
    on update cascade
    on delete set null
);

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
);

create table if not exists question(
	id bigint unsigned not null auto_increment primary key,
    name varchar(255) not null,
    test_id bigint unsigned,

    foreign key(test_id) references test(id)
    on update cascade
    on delete cascade
);

create table if not exists answer(
	id bigint unsigned not null auto_increment primary key,
    name varchar(255) not null,
    question_id bigint unsigned,
	`right` bool,

    foreign key(question_id) references question(id)
    on update cascade
    on delete cascade
);

create table if not exists answer_log(
	id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned,
    question_id bigint unsigned,
    answer_id bigint unsigned,

	foreign key(user_id) references user(id)
    on update cascade
    on delete cascade,

    foreign key(question_id) references question(id)
    on update cascade
    on delete cascade,

    foreign key(answer_id) references answer(id)
    on update cascade
    on delete cascade
);
