select id,"name","desc","cost" from "SofikBD".tasks t
where id not in
(select task_id from "SofikBD".player_task_links ptl
join "SofikBD".players p on ptl.player_id=p.id
where p.id=%s)
