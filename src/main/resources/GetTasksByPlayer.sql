select t.id,t."name",t."desc",t."cost",ptl.is_completed  from "SofikBD".player_task_links ptl
join "SofikBD".tasks t on t.id=ptl.task_id where player_id=%s;