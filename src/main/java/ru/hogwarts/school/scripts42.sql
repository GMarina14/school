alter table student add constraint age_constraint CHECK (age>=16);
alter table student alter column name set not null;
alter table student add constraint name UNIQUE(name);
alter table faculty add constraint name_color  UNIQUE (name, color);
alter table student alter column age set default 20;

