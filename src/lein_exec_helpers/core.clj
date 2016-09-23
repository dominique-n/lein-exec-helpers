(ns lein-exec-helpers.core
 (:require [clojure.java.io :refer [reader writer]]))

(defmacro test1 [& x]
  (letfn [(f [x] `(println ~x))]
    (f "hllo")))
(test1 "hwllp")

(defmacro in2out->>
  [in & forms]
  (letfn [(aux [x forms] 
            (loop  [x x, forms forms]
              (if forms
                (let  [form  (first forms)
                       threaded  (if  (seq? form)
                                   (with-meta `(~(first form) ~@(next form)  ~x)  (meta form))
                                   (list form x))]
                  (recur threaded  (next forms)))
                x)))]
    (with-open [rdr (reader in)]
      (if-not (string? (first forms))
        (doseq [line (aux (line-seq rdr) forms)]
          (println line))))))

(in2out->> "dev-resources/txt.txt" 
                        (map read-string)
           (map #(* 10 %))
           (reduce +)
           )
