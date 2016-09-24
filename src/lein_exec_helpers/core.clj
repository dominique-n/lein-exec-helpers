(ns lein-exec-helpers.core
 (:require [clojure.java.io :refer [reader writer]]))



(defn print-case 
  ([output] (print-case nil output))
  ([wtr output]
   (let [f (if wtr (partial #(fn [output] (.write % output)) wtr) println)]
     (if (sequential? output)
       (doseq [line output]
         (f line))
       (f output)))))

(defmacro in2out->>
  [in out & forms]
  `(with-open [rdr# (reader ~in)]
     (if-not (string? ~out)
       (let [res# (->> rdr# ~out ~@forms)]
         (if (sequential? res#)
           (doseq [line# res#]
             (println line#))
           (println res#)))
       (with-open [wtr# (writer ~out)]
         (let [res# (->> rdr# ~@forms)]
           (if (sequential? res#)
             (doseq [line# res#]
               (.write wtr# (str line# "\n")))
             (.write wtr# (str res# "\n"))))))))
