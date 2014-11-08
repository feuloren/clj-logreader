(defproject clj-logreader "0.0.1-SNAPSHOT"
  :description "Display your log files in your browser as they grow aka. A tail -f in your brower"
  :url "http://feuloren.github.io/clj-logreader"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :uberjar-name "clj-logreader.jar"
  :dependencies [[commons-io/commons-io "2.4"]
                 [compojure "1.1.6"]
                 [domina "1.0.2"]
                 [http-kit "2.1.19"]
                 [jarohen/chord "0.4.2" :exclusions [org.clojure/clojure]]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [org.clojure/tools.cli "0.3.1"]
                 [ring/ring "1.2.1"]
                 [whoops/reagent "0.4.4-alpha"]
                 [cljs-storage "0.1.0-SNAPSHOT"]
                 [com.cemerick/piggieback "0.1.3"]
                 ;; dev
                 [org.clojars.franks42/cljs-uuid-utils "0.1.3"]]
  
  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/austin "0.1.5"]]
  :hooks [leiningen.cljsbuild]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  
  :main logreader.core
  
  :source-paths ["src/clj"]
  :resource-paths ["resources"]
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :jar true
                        :compiler {:output-to "resources/js/dev/main.js"
                                   :output-dir "resources/js/dev"
                                   :optimizations :none
                                   :source-map "resources/js/dev/main.js.source"
                                   :preamble ["reagent/react.js"]}}
                       ]})
