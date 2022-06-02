create table inquiry (
    id BIGINT AUTO_INCREMENT not null,
    customer_id varchar2(255) not null,
    title varchar2(255) not null,
    content varchar2(65535) not null,
    created_datetime datetime default current_timestamp(),
    primary key (id)
);
create index board_idx_01 on inquiry (customer_id, created_datetime);

create table user (
    id BIGINT AUTO_INCREMENT not null,
    user_email_addr varchar2(255) not null,
    password varchar2(255) not null,
    user_nm varchar2(99) not null,
    created_datetime datetime default current_timestamp(),
    primary key (id)
);
create index user_idx_01 on user (user_email_addr);
create index user_idx_02 on user (user_nm);

create table answer (
    id BIGINT AUTO_INCREMENT not null,
    inquiry_id bigint not null,
    user_id bigint not null,
    title varchar2(255) NULL ,
    content varchar2(65535) null,
    is_finished varchar2(1) default 'N',
    accepted_datetime datetime default current_timestamp(),
    finished_datetime datetime,
    primary key (id),
    foreign key (inquiry_id) references inquiry(id),
    foreign key (user_id) references user(id)
);
create index answer_idx_01 on answer (inquiry_id);
create index answer_idx_02 on answer (user_id);