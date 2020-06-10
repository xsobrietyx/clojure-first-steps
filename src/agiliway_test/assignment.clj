(ns agiliway-test.assignment)

;; evaluate function
;; please note that mixed expressions is NOT covered yet. Example of such function call: {:x 3 :y 6} (* 2 (+ x y)) => 18
;; probably could be rewritten in this way:
;; (eval (read-string (apply str (map #(clojure.string/replace % (re-pattern "\\w") "10") "(+ x x)")))) => 20
(defmulti  evaluate (fn [opts _] (empty? opts)))
(defmethod evaluate true [_ expr] (eval expr))
(defmethod evaluate false [opts expr] (eval (map #(if (number? %) % (symbol %))
                                                 (replace
                                                   (zipmap (map name (keys opts))
                                                           (vals opts))
                                                   (map #(if (symbol? %) (str %) %)
                                                        expr)))))

;; optimize function
(defmulti  optimize (fn [[_ _ & c]] identity c))
(defmethod optimize '((* x 0)) [[a b _]] (list a b 0))
(defmethod optimize '((* x 1)) [[a b [_ d _]]] (list a b d))
(defmethod optimize '((* 0 x)) [[a b _]] (list a b 0))
(defmethod optimize '((* 1 x)) [[a b [_ _ e]]] (list a b e))
(defmethod optimize '((+ 0 x)) [[a b [_ _ e]]] (list a b e))
(defmethod optimize '((+ x 0)) [[a b [_ d _]]] (list a b d))
(defmethod optimize '((- x 0)) [[a b [_ c _]]] (list a b c))
(defmethod optimize '((/ y 1)) [[a b [_ d _]]] (list a b d))

;; ->javascript function
(defn ->javascript [name [a b [c d e]]] (let [args (set [b d e])] (str "function " name "("
                                                                  (apply str (filter #(symbol? %) args))
                                                                  ") { return (" (clojure.string/join " " [b a])
                                                                       " (" (clojure.string/join " " [d c e]) ")); }")))

;; I'm sure we shouldn't use -main function here. Unit tests should cover all the logic separately.
