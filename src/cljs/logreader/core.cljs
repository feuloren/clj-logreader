(ns logreader.core
   (:require [cljs.reader :as reader]
             [goog.events :as events]
             goog.dom
             [cljs.core.async :as csp]
             [reagent.core :as r]
             [cljs-storage.core :as dom-storage]
             [cljs-uuid-utils :as uuid-lib]
             [clojure.browser.repl :as repl])
   (:import [goog.net XhrIo]
            goog.net.EventType
            [goog.events EventType])
   (:require-macros [logreader.macros :refer [defcomponent reset-in!]]
                    [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

(def app (r/atom (or (reader/read-string (get (dom-storage/local-storage) :logreader-state))
                     {:files [{:path "/var/truc" :name "Truc"} {:path "/" :name "Root"}] :visible "/"})))
;; Put an observer for atom to save its state to DOM
(add-watch app :_ (fn [k ref old-state new-state]
                    (println "Saving app state to local storage")
                    (assoc! (dom-storage/local-storage) :logreader-state new-state)))

(def main-channel (csp/chan))

(def logs-content (atom [{:path "/var/log/error.php" :lines []}
                         {:path "/var/log/truc" :lines []}]))
;; :lines only grows, one conj at a time, we could use this property to optimize rendering

(defcomponent file-menu-item [options text selected]
  [:li (when selected {:class "pure-menu-selected"})
   [:a {:href "#" :on-click (:on-select-click options)} text]
   [:a {:title "Stop following this file" :class "close" :href "#" :on-click (:on-delete-click options)} "X"]]
  )

(defn btn-add-file-clicked []
  
  ;;(swap! app update-in [:files] #(conj % {:name "Added !" :path "/blabla"}))
  )

(defcomponent new-file-form [value-cursor]
  [:div [:form {:class "pure-form pure-form-stacked"}
         [:fieldset
          [:legend "Follow new file"]
          [:input {:type "text" :value @value-cursor}]
          [:button {:class "pure-button pure-button-primary"} "Follow"]
          [:button {:class "pure-button"} "Cancel"]
          ]]])

(defn add-random-strings [files visible]
  (let [lots-of-string (map (fn [_] (uuid-lib/uuid-string (uuid-lib/make-random-uuid))) (range 1 5000))
        selected-path @visible]
    (swap! files (fn [files]
                   (-> (fn [file]
                         (if (= (:path file) selected-path)
                           ;; return the file with lines added
                           (update-in file [:lines] #(apply conj % lots-of-string))
                           ;; return the file as-is
                           file
                           ))
                       (map files))))))

(defcomponent files-menu [files visible]
  [:ul {:id "tabs"}
   ;; Static stuff
   [:li [:a {:href "#" :on-click #(csp/put! main-channel {:topic :add-file-dislay-dialog})} ;; BAAAD
         "Follow new file"]]
   [:li [:a {:href "#" :on-click #(add-random-strings files visible)} "Add a whole lot of lines"]]
   [:li "Followed Files"]
   ;; Then the list of files
   (let [visible-path @visible
         is-selected? (fn [file] (= visible-path (:path file)))
         remove-file (fn [files file] (remove #(= (:path %) (:path file)) files))]
     (for [file @files]
       ^{:key (:path file)} [file-menu-item
                             {:on-select-click #(reset! visible (:path file))
                              :on-delete-click #(swap! files remove-file file)}
                             (:name file) (is-selected? file)]))
   ])

(defcomponent app-menu [app]
  (let [state (r/atom {:add-file {:show false :path "/"}})]
    (fn []
      [:div {:class "pure-menu pure-menu-open"}
       [:a {:class "pure-menu-heading"} "Log Reader"]
       [files-menu (r/cursor [:files] app) (r/cursor [:visible] app)]
       (when (:show (:add-file @state))
         [new-file-form (r/cursor [:add-file :path] state)])
       ])))

(defn active-file
  ([app-state] (active-file (:files app-state) (:visible app-state)))
  ([files selected-path] (->> files
                              (filter #(= selected-path (:path %)))
                              (first))))

(defcomponent app-logsview [app]
  ;; We may not want to use react here because things gets really slow after a 1000 lines
  ;; And we don't want to save the lines when in dom storage anyway, we can just get them
  ;; from the server after reload
  [:div {:id "logs"}
   (let [app-state @app
         selected-path (:visible app-state)]
     (for [file (:files app-state)]
       ^{:key (:path file)}
       [:div {:style (if (= selected-path (:path file)) {} {:display "none"})}
        (for [line (map #(do {:text %1 :index %2}) (:lines file) (iterate inc 0))]
          ^{:key (:index line)} [:p (:text line)])]))])

(go-loop []
  (prn (csp/<! main-channel))
  (recur))

(r/render-component [app-menu app] (.getElementById js/document "menu"))
; (r/render-component [app-logsview app] (.getElementById js/document "main"))



;; TODO
;; Rewrite dropdown and collapsing menu
;; Write communication with server
;; on receive store the line, then apply the filter/formatter and add to the html
