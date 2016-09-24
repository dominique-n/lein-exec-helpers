(ns lein-exec-helpers.core-test
  (:require [midje.sweet :refer :all]
            [lein-exec-helpers.core :refer :all]
            [clojure.java.io :refer [reader writer]]))


(facts "About `print-case to stdout"
       (with-out-str (print-case "hello")) => "hello\n"
       (with-out-str (print-case ["hello" "bro"])) => "hello\nbro\n"
       )

(against-background
  [(before :contents (def out "dev-resources/res.txt"))
   (after :checks (clojure.java.io/delete-file out))]

  (facts "About `print-case to file"
                (do (with-open [wtr (writer out)]
                      (print-case wtr "60"))
                      (slurp out)) => "60"
                (do (with-open [wtr (writer out)]
                      (print-case wtr ["10\n" "20\n" "30\n"]))
                      (slurp out)) => "10\n20\n30\n"
                ))


(facts "About `in2out->> to stdout"
       (with-out-str (doall (in2out->> "dev-resources/txt.txt" line-seq
                                       (map read-string) (map #(* 10 %)) (reduce +))))
       => "60\n"

       (with-out-str (doall (in2out->> "dev-resources/txt.txt" line-seq
                                       (map read-string) (map #(* 10 %)))))
       => "10\n20\n30\n"

       )



(against-background
  [(before :contents (def in "dev-resources/txt.txt"))
   (before :contents (def out "dev-resources/res.txt"))
   (after :checks (clojure.java.io/delete-file out))]
    (facts "About `in2out->> to file"
           (do (in2out->> in out line-seq
                          (map read-string) (map #(* 10 %)) (reduce +) str)
               (slurp out)) => "60"
           (do (in2out->> in out line-seq
                          (map read-string) (map #(* 10 %)) (map #(str % "\n")))
               (slurp out)) => "10\n20\n30\n"
           ))
