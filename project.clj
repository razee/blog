(defproject sanitycheck "0.1.0"
  :description "Sanity check"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hiccup "1.0.2"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [postgresql "9.3-1101.jdbc4"]
                 [compojure "1.1.6"]
                 [org.pegdown/pegdown "1.4.2"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [com.cemerick/friend "0.2.0"]
                 [org.clojure/data.json "0.2.4"]
                 [ring/ring-json "0.3.1"]
                 [com.cemerick/drawbridge "0.0.6"]]
  :uberjar-name "sanitycheck-0.1.0-standalone.jar"
  :min-lein-version "2.0.0"
  :main ^:skip-aot sanitycheck.handler
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler sanitycheck.handler/app}
  :profiles {:uberjar {:aot :all}})
