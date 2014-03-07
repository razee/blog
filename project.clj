(defproject raziblog "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hiccup "1.0.2"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.h2database/h2 "1.3.170"]
                 [compojure "1.1.6"]
                 [ring/ring-jetty-adapter "1.1.0"]]
  :main raziblog.handler
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler raziblog.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [hiccup "1.0.5"]
                        [org.clojure/java.jdbc "0.2.3"]
                        [com.h2database/h2 "1.3.170"]
                        [korma "0.3.0-RC6"]
                        [ring-mock "0.1.5"]]}})
