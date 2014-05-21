(defproject sanitycheck "0.1.0"
  :description "Sanity check"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hiccup "1.0.2"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [postgresql "9.3-1101.jdbc4"]
                 [compojure "1.1.6"]
                 [markdown-clj "0.9.43"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [com.cemerick/friend "0.2.0" :exclusions [org.clojure/core.incubator]]
                 [org.clojure/data.json "0.2.4"]
                 [ring/ring-json "0.3.1" :exclusions [ring/ring-core]]
                 [com.cemerick/drawbridge "0.0.6"]
                 [clj-time "0.7.0"]; :exclusions [org.clojure/clojure]]
                 [ring/ring-anti-forgery "0.3.1" :exclusions [hiccup]]
                 [enlive "1.1.5"]
                 [ring-basic-authentication "1.0.5"]
                 [org.clojure/clojurescript "0.0-2202"]]
  ;:repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  :uberjar-name "sanitycheck-0.1.0-standalone.jar"
  :min-lein-version "2.0.0"
  :main  sanitycheck.handler
  :aot :all
  :plugins [[lein-ring "0.8.10"]
            [lein-cljsbuild "1.0.3"]]
  :cljsbuild {
    :builds [{
              :source-paths ["resources/public/cljs"]

              :compiler {
                :output-to "resources/public/cljs/"
                :optimizations :whitespace
                :pretty-print true}}]}
  ;:hooks [leiningen.cljsbuild]
  :jar true
  :ring {:handler sanitycheck.handler/app}
  :profiles {:uberjar {:aot :all}})
