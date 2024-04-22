select b.id,b.name,b.address from "SofikBD".player_bar_links pbl
join "SofikBD".bars b on b.id=pbl.bar_id where player_id = %s and is_visited = 1