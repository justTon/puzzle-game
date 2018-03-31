CREATE SCHEMA puzzle;

create table game_field
(
	x int not null,
	y int not null,
	img_id int not null,
	open tinyint(1) default '0' not null,
	completed tinyint(1) default '0' not null
)
;

create table game_info
(
	horizontal int not null,
	vertical int not null,
	theme varchar(20) not null
)
;

