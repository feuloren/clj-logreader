(ns logreader.util
  (:require [clojure.string :as s]))

(defn valid-ip? [input]
  "Check if the string is composed of 4 integers between 0 and 127 separated by dots TODO support IPV6"
  (let [parts (s/split input #"\.")]
    (and (== (count parts) 4)
         (try
           ;; We take each part, parse it and see if it's between 0 and 127 inclusive
           ;; (every? identity ...) is just like (and ...) but we can't just use (apply and ...) because and is a macro
           (every? identity (map #(<= 0 (Integer/parseInt %) 127) parts))
           (catch NumberFormatException e
               false)))))

