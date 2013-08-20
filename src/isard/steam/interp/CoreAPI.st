(def defmacro (macro (name subs body) 
    (def name (macro subs body))))

(defmacro + (a b) 
    (do (def n1 a)
    	(def n2 b)
	(java (code _RET_ = n1.add(n2)))));