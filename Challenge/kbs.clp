(defrule votar 
(age ?a ?n)
(name ?n)
(country ?c ?n)
(current ?v ?n) 
(test (> ?a 18))
(test (eq ?c mex))
(test (> ?v 21))
=>
(println ?n " "?a " "?c " "?v)
) 
