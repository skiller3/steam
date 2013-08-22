(def defmacro (macro (name subs body) 
    (def name (macro subs body))))
    
(defmacro defun (name args body)
	(def name (fun args body)))

(defmacro + (a b) 
    (do (def n1 a)
    	(def n2 b)
	(java (code n1.add(n2);))))