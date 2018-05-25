insert into #multipleQueries(status_id, status_name) values (99, 'first insert');
insert into #multipleQueries(status_id, status_name) values (98, 'second insert');
insert into #multipleQueries(status_id, status_name) values (97, 'third insert');

select * from #multipleQueries where status_id = 99;
select * from #multipleQueries where status_id = 98;
select * from #multipleQueries where status_id = 97;
