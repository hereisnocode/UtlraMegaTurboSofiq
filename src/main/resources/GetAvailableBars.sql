select * from "SofikBD".bars b
where id not in
(select bar_id from "SofikBD".player_bar_links pbl
where player_id=%s and is_visited=1)