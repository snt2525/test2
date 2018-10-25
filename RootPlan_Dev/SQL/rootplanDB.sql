create user `rootplan`@`%` identified by 'rootplan';
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = 'Asia/Seoul';

create database rootplan;

CREATE TABLE `customer` (
  `id` varchar(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table route(
rid varchar(500) not null,
size int,
cid varchar(20) not null,
name varchar(100),
address1 varchar(100),
lat1 float(50),
lng1 float(50),
address2 varchar(100),
lat2 float(50),
lng2 float(50),
address3 varchar(100),
lat3 float(50),
lng3 float(50),
address4 varchar(100),
lat4 float(50),
lng4 float(50),
address5 varchar(100),
lat5 float(50),
lng5 float(50),
address6 varchar(100),
lat6 float(50),
lng6 float(50),
address7 varchar(100),
lat7 float(50),
lng7 float(50),
primary key(rid),
foreign key(cid) references customer (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table route2(
rid varchar(500) not null,
cid varchar(20) not null,
pt_order varchar(100),
car_order varchar(100),
size int,
start int,
last int,
primary key(rid, cid) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

COMMIT;