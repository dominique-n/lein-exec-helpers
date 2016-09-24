(ns lein-exec-helpers.core
 (:require [clojure.java.io :refer [reader writer]]))



(defn print-case 
  ([output] (print-case nil output))
  ([wtr output]
   (let [f (if wtr #(.write wtr (str % "\n")) println)]
     (if (sequential? output)
       (doseq [line output]
         (f line))
       (f output)))))

(defmacro in2out->>
  [in out & forms]
  `(with-open [rdr# (reader ~in)]
     (if-not (string? ~out)
       (print-case (->> rdr# ~out ~@forms))
       (with-open [wtr# (writer ~out)]
         (print-case wtr# (->> rdr# ~@forms))))))
