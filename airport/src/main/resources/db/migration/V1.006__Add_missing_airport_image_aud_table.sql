create table airport_image_aud (rev int4 not null, airport_id int8 not null, id int8 not null, revtype int2, primary key (rev, airport_id, id));
alter table if exists airport_image_aud add constraint FK57vbhpo86af1sgnb6wifqrsc foreign key (rev) references revinfo;