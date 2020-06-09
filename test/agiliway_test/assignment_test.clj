(ns agiliway-test.assignment-test
  (:require [clojure.test :refer :all]
            [agiliway-test.assignment :refer :all]))

(deftest evaluate-test
  (testing "Evaluate function."
    (is (= (evaluate {} '(* 2 (+ 1 1))) 4))))

(deftest evaluate-with-options-test
  (testing "Evaluate function with options passed as params."
    (is (= (evaluate {:x 10} '(* x x)) 100))))

(deftest ->javascript-test
  (testing "->javascript function. Arguments expected."
    (is (= (->javascript "example" '(+ 1 (* x x))) "function example(x) { return (1 + (x * x)); }"))))

(deftest ->javascript-no-args-test
  (testing "->javascript function. Different name and no args expected."
    (is (= (->javascript "example-2" '(* 5 (- 10 5))) "function example-2() { return (5 * (10 - 5)); }"))))
