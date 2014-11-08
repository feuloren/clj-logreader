(ns logreader.core
  (:require [org.httpkit.server :as http-kit]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as s]
            [logreader.app :refer [app]]
            [logreader.util :refer [valid-ip?]])
  (:gen-class :main true))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 8888
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
    ["-i" "--http-ip PORT" "IP address to bind"
     :default "127.0.0.1"
     :validate [valid-ip? "Must be a valid IP address"]]
    ["-h" "--help" :default false :flag true]])

(defonce server (atom nil))

(defn start-server [port ip]
  "Start the server"
  (reset! server (http-kit/run-server #'app {:port port :ip ip})))

(defn stop-server []
  (swap! server #(when-not (nil? %) (%))))

(defn -main [& args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (if (empty? errors)
      (if (:help options)
        (println summary)
        (do (println "*Starting server*")
            (start-server (:port options) (:http-ip options))))
      (do (println "Error : ")
          (println (s/join "\n" errors))
          (println "Usage : ")
          (println summary)))))
