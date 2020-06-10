(ns agiliway-test.assignment-test
  (:require [clojure.test :refer :all]
            [agiliway-test.assignment :refer :all]))

(deftest evaluate-test
  (testing "Evaluate function."
    (is (= (evaluate {} '(* 2 (+ 1 1))) 4)))
  (testing "Evaluate function with options passed as params."
    (is (= (evaluate {:x 10} '(* x x)) 100))))

(deftest ->javascript-test
  (testing "->javascript function. Arguments list expected."
    (is (= (->javascript "example" '(+ 1 (* x x))) "function example(x) { return (1 + (x * x)); }")))
  (testing "->javascript function. Different name and no args expected."
    (is (= (->javascript "example-2" '(* 5 (- 10 5))) "function example-2() { return (5 * (10 - 5)); }"))))

(deftest optimize-test
  (testing "Multiply by zero. Variant #1.")
  (is (= (optimize '(+ 5 (* x 0))) "'(+ 5 0)"))
  (testing "Multiply by zero. Variant #2.")
  (is (= (optimize '(* 5 (* 0 x))) "'(* 5 0)"))
  (testing "Multiply by one. Variant #1.")
  (is (= (optimize '(* 42 (* 1 x))) "'(* 42 x)"))
  (testing "Multiply by one. Variant #2.")
  (is (= (optimize '(+ 34 (* x 1))) "'(+ 34 x)"))
  (testing "Increment by zero. Variant #1.")
  (is (= (optimize '(* 22 (+ x 0))) "'(* 22 x)"))
  (testing "Increment by zero. Variant #2.")
  (is (= (optimize '(* 22 (+ 0 x))) "'(* 22 x)"))
  (testing "Decrement by zero.")
  (is (= (optimize '(+ 5 (- x 0))) "'(+ 5 x)"))
  (testing "Divide by one.")
  (is (= (optimize '(+ 42 (/ y 1))) "'(+ 42 y)")))
