(ns cider-ci.open-session.bcrypt-test 
  (:require 
    [clojure.test :refer :all]
    [cider-ci.open-session.bcrypt :refer :all]
    ))


(deftest roundtrip []
  (is (= true (checkpw "secret" (hashpw "secret")))))

(deftest fail-checkpw 
  (is (= false
         (checkpw "Foo" (hashpw "Bar")))))

(def ruby-bcrypt-secret "$2a$10$ajmFHB.aoDQZVvEX6cGmyOMrvmesmBnYK7kyriUJGLpEle0aQOTJ.")

(deftest ruby-becrypt-compat 
  (is (= true (checkpw "secret" ruby-bcrypt-secret))))


