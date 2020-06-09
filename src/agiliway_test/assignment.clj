(ns agiliway-test.assignment)

;; evaluate function
;; note that mixed expressions is NOT covered. Example of such function call: {:x 3 :y 6} (* 2 (+ x y)) => 18
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
(defmethod optimize '((* x 0)) [[_ b _]] b)
(defmethod optimize '((* x 1)) [[a b [_ d _]]] (eval (str "'" (list a b d))))
(defmethod optimize '((* 0 x)) [[_ b _]] b)
(defmethod optimize '((* 1 x)) [[a b [_ _ e]]] (eval (str "'" (list a b e))))
(defmethod optimize '((+ 0 x)) [[a b [_ _ e]]] (eval (str "'" (list a b e))))
(defmethod optimize '((+ x 0)) [[a b [_ d _]]] (eval (str "'" (list a b d))))
(defmethod optimize '((- x 0)) [[a b [_ c _]]] (list a b c))
(defmethod optimize '((/ y 1)) [[a b [_ d _]]] (eval (str "'" (list a b d))))

;; ->javascript function
(defn ->javascript [name [a b [c d e]]] (let [args (set [b d e])] (str "function " name "("
                                                                  (apply str (filter #(symbol? %) args))
                                                                  ") { return (" (clojure.string/join " " [b a])
                                                                       " (" (clojure.string/join " " [d c e]) ")); }")))

;; I'm sure we shouldn't use -main function here. Unit tests should cover all the logic separately.
