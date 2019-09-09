CREATE DATABASE javaCrawlerDb;

create table keywords (id bigint NOT NULL primary key, 
                       keyword text);

create table webpage (id bigint not null primary key, 
                      keyword_id bigint not null references keywords(id), 
                      html text, 
                      text1 text, 
                      url text,
                      seen timestamp without time zone NOT NULL);
                      
insert into keywords values (1, 'C++');
insert into keywords values  (2, 'Scala');                      
