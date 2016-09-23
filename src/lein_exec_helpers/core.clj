(ns lein-exec-helpers.core
 (:require [clojure.java.io :refer [reader writer]]))




(defmacro in2out->>
  [in out & forms]
  `(with-open [rdr# (reader ~in)]
    (if-not (string? ~out)
      (let [res# (->> (line-seq rdr#) ~out ~@forms)]
        (if (sequential? res#)
          (doseq [line# res#]
            (println line#))
          (println res#))))))
(macroexpand-1 (in2out->> "dev-resources/txt.txt" 
                          (map read-string)
                          (map #(* 10 %))
                          (reduce +)
                          ))
