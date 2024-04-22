-- DROP SCHEMA "SofikBD";

CREATE SCHEMA "SofikBD" AUTHORIZATION postgres;

-- DROP SEQUENCE "SofikBD".bars_id_seq;

CREATE SEQUENCE "SofikBD".bars_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE "SofikBD".player_bar_links_id_seq;

CREATE SEQUENCE "SofikBD".player_bar_links_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE "SofikBD".player_task_links_id_seq;

CREATE SEQUENCE "SofikBD".player_task_links_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE "SofikBD".players_id_seq;

CREATE SEQUENCE "SofikBD".players_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE "SofikBD".tasks_id_seq;

CREATE SEQUENCE "SofikBD".tasks_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;-- "SofikBD".bars definition

-- Drop table

-- DROP TABLE "SofikBD".bars;

CREATE TABLE "SofikBD".bars (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	"name" varchar NOT NULL,
	address varchar NOT NULL,
	CONSTRAINT bars_pk PRIMARY KEY (id),
	CONSTRAINT bars_un UNIQUE (name)
);


-- "SofikBD".game definition

-- Drop table

-- DROP TABLE "SofikBD".game;

CREATE TABLE "SofikBD".game (
	stage varchar NOT NULL,
	current_circle int4 NOT NULL DEFAULT 0
);


-- "SofikBD".players definition

-- Drop table

-- DROP TABLE "SofikBD".players;

CREATE TABLE "SofikBD".players (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	login varchar NOT NULL,
	"password" varchar NOT NULL,
	issofik int4 NOT NULL DEFAULT 0,
	score int4 NOT NULL DEFAULT 0,
	readystate int4 NOT NULL DEFAULT 0,
	istestuser int4 NOT NULL DEFAULT 0,
	CONSTRAINT players_pk PRIMARY KEY (id),
	CONSTRAINT players_un UNIQUE (login)
);


-- "SofikBD".tasks definition

-- Drop table

-- DROP TABLE "SofikBD".tasks;

CREATE TABLE "SofikBD".tasks (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	"name" varchar NOT NULL,
	"desc" varchar NOT NULL,
	"cost" int4 NOT NULL,
	CONSTRAINT tasks_pk PRIMARY KEY (id),
	CONSTRAINT tasks_un UNIQUE (name)
);


-- "SofikBD".circles definition

-- Drop table

-- DROP TABLE "SofikBD".circles;

CREATE TABLE "SofikBD".circles (
	id int4 NOT NULL,
	state varchar NOT NULL,
	CONSTRAINT circles_pk PRIMARY KEY (id)
);


-- "SofikBD".player_bar_links definition

-- Drop table

-- DROP TABLE "SofikBD".player_bar_links;

CREATE TABLE "SofikBD".player_bar_links (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	player_id int4 NOT NULL,
	bar_id int4 NOT NULL,
	is_visited int4 NOT NULL DEFAULT 0,
	CONSTRAINT player_bar_links_pk PRIMARY KEY (id),
	CONSTRAINT player_bar_links_un UNIQUE (player_id, bar_id),
	CONSTRAINT player_bar_links_fk FOREIGN KEY (player_id) REFERENCES "SofikBD".players(id),
	CONSTRAINT player_bar_links_fk_1 FOREIGN KEY (bar_id) REFERENCES "SofikBD".bars(id)
);


-- "SofikBD".player_task_links definition

-- Drop table

-- DROP TABLE "SofikBD".player_task_links;

CREATE TABLE "SofikBD".player_task_links (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	player_id int4 NOT NULL,
	task_id int4 NOT NULL,
	is_completed int4 NULL DEFAULT 0,
	CONSTRAINT player_task_links_pk PRIMARY KEY (id),
	CONSTRAINT player_task_links_un UNIQUE (player_id, task_id),
	CONSTRAINT player_task_links_fk FOREIGN KEY (player_id) REFERENCES "SofikBD".players(id),
	CONSTRAINT player_task_links_fk_1 FOREIGN KEY (task_id) REFERENCES "SofikBD".tasks(id)
);



CREATE OR REPLACE FUNCTION "SofikBD".assign_task(_player_id integer, _task_id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	declare 
	v_task integer;
	v_player integer;
	BEGIN
		select coalesce(ps.id,-1488),coalesce(t.id,-1001) into v_player,v_task from (select _player_id as pl_id, _task_id as b_id) a
		left join "SofikBD".players p on p.id=a.pl_id
		left join "SofikBD".tasks t on t.id = a.b_id
		left join "SofikBD".players ps on ps.id=a.pl_id;
			if v_player<>-1488 then
				if v_task<>-1001 then 
					merge into "SofikBD".player_task_links t
					using (select _player_id as pl_id, _task_id as b_id) a
					on t.player_id = a.pl_id and t.task_id = a.b_id
					WHEN MATCHED THEN
  					UPDATE SET is_completed=t.is_completed
					WHEN NOT MATCHED THEN
  					INSERT (player_id, task_id)
  					VALUES (a.pl_id, a.b_id);
				else return v_task;
				end if;
			else return v_player;
			end if;
		return 0;
	END;
$function$
;

CREATE OR REPLACE FUNCTION "SofikBD".complete_task(_player_id integer, _task_id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	BEGIN
	declare 
	v_task integer;
	v_player integer;
	v_complete integer;
	v_cost integer;
	BEGIN
		select coalesce(ps.id,-1488),coalesce(p.task_id,-1001),coalesce(p.is_completed,-1000),coalesce(t."cost",0) into v_player,v_task,v_complete,v_cost from (select _player_id as pl_id, _task_id as b_id) a
		left join "SofikBD".player_task_links p on p.player_id=a.pl_id and p.task_id = a.b_id
		left join "SofikBD".players ps on ps.id=a.pl_id
		left join "SofikBD".tasks t on t.id=a.b_id;
			if v_player<>-1488 then
			select coalesce(p.task_id,-1001),coalesce(p.is_completed,-1000),coalesce(t."cost",0) into v_task,v_complete,v_cost from (select _player_id as pl_id, _task_id as b_id) a
			left join "SofikBD".player_task_links p on p.player_id=a.pl_id and p.task_id = a.b_id
			left join "SofikBD".tasks t on t.id=a.b_id;
				if v_task<>-1001 then 
					if v_complete<>1 then 
						update "SofikBD".player_task_links set is_completed = 1 where task_id=_task_id and player_id=_player_id;
						update "SofikBD".players set score=score+v_cost where id=v_player;
					return 0;
					else return -102;
					end if;
				else return v_bar;
				end if;
			else return v_player;
			end if;
	END;
	END;
$function$
;

CREATE OR REPLACE FUNCTION "SofikBD".loginorcreatenew(vlogin character varying, vpassword character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	DECLARE 
	v_id integer;
	vpass varchar;
	BEGIN
		select coalesce(p.id,-999),p."password" into v_id,vpass from (select vlogin as login,vpassword as "password") a
		left join "SofikBD".players p on a.login=p.login;
		if v_id>=0 then
			if vpass=vpassword 
				then return(v_id);
				else return(-100);
			end if;
		else 
			insert into "SofikBD".players (login,"password") values (vlogin,vpassword);
			select coalesce(id,-999) into v_id from (select vlogin as login,vpassword as "password") a
				left join "SofikBD".players p on a.login=p.login and a."password" = p."password";
			return(v_id);
		end if;
	END;
$function$
;

CREATE OR REPLACE FUNCTION "SofikBD".reset_bars_for_all_players()
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	declare 
	pl_rec   record;
	bar_rec   record;
	begin
	truncate table "SofikBD".player_bar_links;
	for pl_rec in (select id from "SofikBD".players) loop
		for bar_rec in (select id from "SofikBD".bars) loop
			insert into "SofikBD".player_bar_links (player_id,bar_id) values (pl_rec.id,bar_rec.id);
		end loop;		
	end loop;
	return 0;
	END;
$function$
;

CREATE OR REPLACE FUNCTION "SofikBD".set_score(v_id integer, v_score integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	declare
	t_id integer;
	BEGIN
		select coalesce(p.id,-1488) into t_id from (select v_id as id) a 
		left join "SofikBD".players p on p.id=a.id
		where a.id=v_id;
		if t_id>=0 then update "SofikBD".players set score=v_score where id=v_id; end if;
	return v_score;
	END;
$function$
;

CREATE OR REPLACE FUNCTION "SofikBD".visit_bar(_player_id integer, _bar_id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
	declare 
	v_bar integer;
	v_player integer;
	v_visited integer;
	BEGIN
		select coalesce(ps.id,-1488),coalesce(p.bar_id,-1337),coalesce(p.is_visited,-100) into v_player,v_bar,v_visited from (select _player_id as pl_id, _bar_id as b_id) a
		left join "SofikBD".player_bar_links p on p.player_id=a.pl_id and p.bar_id = a.b_id
		left join "SofikBD".players ps on ps.id=a.pl_id;
			if v_player<>-1488 then
			select coalesce(p.bar_id,-1337),coalesce(p.is_visited,-100) into v_bar,v_visited from (select _player_id as pl_id, _bar_id as b_id) a
			left join "SofikBD".player_bar_links p on p.player_id=a.pl_id and p.bar_id = a.b_id;
				if v_bar<>-1337 then 
					if v_visited<>1 then
						update "SofikBD".player_bar_links set is_visited = 1 where bar_id=_bar_id and player_id=_player_id;
						update "SofikBD".players set readystate = 1 where id=_player_id; --илья хочет чтобы плеер приготовился когда посетит бар
						return 0;
					else return -101;
					end if;
				else return v_bar;
				end if;
			else return v_player;
			end if;
	END;
$function$
;