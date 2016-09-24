(ns lein-exec-helpers.core-test
  (:require [midje.sweet :refer :all]
            [lein-exec-helpers.core :refer :all]
            [clojure.java.io :refer [reader writer]]))


(facts "About `print-case to stdout"
       (with-out-str (print-case "hello")) => "hello\n"
       (with-out-str (print-case ["hello" "bro"])) => "hello\nbro\n"
       )

(facts "About `in2out->>"
       (with-out-str (doall (in2out->> "dev-resources/txt.txt" line-seq
                                       (map read-string) (map #(* 10 %)) (reduce +))))
       => "60\n"

       (with-out-str (doall (in2out->> "dev-resources/txt.txt" line-seq
                                       (map read-string) (map #(* 10 %)))))
       => "10\n20\n30\n"

       )



(against-background
  [(after :checks (clojure.java.io/delete-file out))]
  (let [out "dev-resources/res.txt"]
    (facts "About `in2out->>"
           (do (in2out->> "dev-resources/txt.txt" out line-seq
                          (map read-string) (map #(* 10 %)) (reduce +))
               (slurp out)
               => "60\n")
           (do (in2out->> "dev-resources/txt.txt" out line-seq
                          (map read-string) (map #(* 10 %)))
               (slurp out)
               => "10\n20\n30\n")
           )))
