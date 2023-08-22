--liquibase formatted sql

--changeset GMarina14:1
create index student_names_index on student(name);

--changeset GMarina14:2
create index faculties_name_and_color_index on faculty(name, color);
