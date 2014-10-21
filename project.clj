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
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [org.clojure/tools.cli "0.3.1"]
                 [reagent "0.4.2"]
                 [ring/ring "1.2.1"]]
  
  :plugins [[lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]

  :main logreader.core
  
  :source-paths ["src/clj"]
  :resource-paths ["resources"]
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :jar true
                        :compiler {:output-to "resources/js/main.js"
                                   :output-dir "resources/js/out"
                                   :optimizations :whitespace
                                   :source-map "resources/js/main.js.source"
                                   :preamble ["reagent/react.js"]}}
                       ]})
