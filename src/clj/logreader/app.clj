(ns logreader.app
  (:require [ring.util.response :refer [file-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [compojure.route :as route]
            [compojure.core :refer [defroutes GET ANY]]
            [org.httpkit.server :refer :all]
            [clojure.string :as s]))

(defn handle-socket [req]
  (with-channel req channel
    (on-receive channel (fn [data]
                          (send! channel data)))))

(defn serve-file [& parts]
  (file-response (s/join "/" parts) {:root "resources"})
  )

(defroutes routes
  (GET "/" [] (serve-file "html/main.html"))
  (ANY "/socket" req (handle-socket req))
  ;;(GET "/css/:file" [file] (serve-file "css" file))
  ;;(GET "/js/:file" [file] (serve-file "js" file))
  (route/files "/" {:root "resources"}))

(def app routes)
