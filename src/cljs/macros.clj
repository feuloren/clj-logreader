(ns logreader.macros)

;; defcomponent is just a defn
;; I use it to distinguish regular business functions and component-creating function
(defmacro defcomponent [& body] (conj body 'defn))
