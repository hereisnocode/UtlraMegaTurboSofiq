select p.id,login,score,readystate "is_ready",issofik "is_sofik",c.id "current_circle", c.state "is_completed" from "SofikBD".players p
join "SofikBD".game g on 1=1
join "SofikBD".circles c on g.current_circle=c.id
order by p.id