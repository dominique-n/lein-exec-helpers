(ns lein-exec-helpers.core-test
  (:require [midje.sweet :refer :all]
            [lein-exec-helpers.core :refer :all]
            [clojure.java.io :refer [reader writer]]))


(facts "About `in2out->>"
       (with-out-str (doall (in2out->> "dev-resources/txt.txt" 
                                       (map read-string) (map #(* 10 %)) (reduce +))))
       => "60\n"
       )
