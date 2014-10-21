(ns logreader.core
   (:require [cljs.reader :as reader]
             [goog.events :as events]
             [goog.dom :as gdom]
             [reagent.core :as reagent])
   (:import [goog.net XhrIo]
            goog.net.EventType
            [goog.events EventType])
   (:require-macros [logreader.macros :refer [defcomponent]]))

(enable-console-print!)

(println "Hello world!")

(def app (reagent/atom {:files [{:path "/var/log/error.php" :name "PHP Florent"}
                                {:path "/var/log/truc" :name "Mobops-Web"}]}))

(defcomponent file-menu-item [text]
  [:li [:a {:href "#"} text]
   [:a {:title "Stop following this file" :class "close" :href "#"} "X"]]
  )

(defn add-file []
  (swap! app update-in [:files] #(conj % {:name "Added !" :path "/blabla"}))
  )

(defcomponent files-menu [files]
  [:ul {:id "tabs"}
   [:li [:a {:href "#" :on-click add-file} "Follow new file"]]
   [:li "Followed Files"]
   (for [file files]
     ^{:key (:path file)} [file-menu-item (:name file)])])

(defcomponent app-menu [app]
  [:div {:class "pure-menu pure-menu-open"}
   [:a {:class "pure-menu-heading"} "Log Reader"]
   [files-menu (:files @app)]
   ])

(reagent/render-component [app-menu app] (.getElementById js/document "menu"))

;; TODO
;; Rewrite dropdown and collapsing menu
;; Write communication with server
;; 
