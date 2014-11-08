(ns logreader.macros)

;; defcomponent is just a defn
;; I use it to distinguish regular business functions and component-creating function
(defmacro defcomponent [& body] (conj body 'defn))

;; Act like reset! but change the value deep down inside a clojure structure instead of changing the value of the actors
;; path may be a vector of keys or a single key
(defmacro reset-in! [atom path value]
  (let [path (if (vector? path) path [path])]
    `(swap! ~atom update-in ~path #(do % ~value))))
