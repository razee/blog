(ns sanitycheck.handler
  (:use ring.middleware.anti-forgery
        ring.util.response)
  (:require [sanitycheck.views.views :as views]
            [sanitycheck.models.db :as db]
            [sanitycheck.routes :as r]
            (compojure
              [core :as cc]
              [handler :as handler]
              [route :as route])
            [ring.adapter.jetty :as jetty]
            (ring.middleware
              [json :as json]
              [session :as session]
              [params :as params]
              [keyword-params :as kp]
              [nested-params :as np]
              [basic-authentication :as basic])
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [cemerick.drawbridge])
            (:gen-class))

(def drawbridge-handler
  (-> (cemerick.drawbridge/ring-handler)
      (kp/wrap-keyword-params)
      (np/wrap-nested-params)
      (params/wrap-params)
      (session/wrap-session)))

(defn authenticated? [name pass]
  (= [name pass] [(System/getenv "AUTH_USER") (System/getenv "AUTH_PASS")]))

(defn wrap-drawbridge [handler]
  (fn [req]
    (let [handler (if (= "/repl" (:uri req))
      (basic/wrap-basic-authentication
       drawbridge-handler authenticated?)
      handler)]
      (handler req))))

(def app
   (handler/site
    (friend/authenticate r/app-routes
     {:login-uri "/login"
      :default-landing-uri "/"
      :unauthorized-handler
        #(-> (str "You do not have sufficient privileges to access " (:uri %))
                               response
                                (status 401))
     :credential-fn (partial creds/bcrypt-credential-fn #((db/friendly-db) %))
     :workflows [(workflows/interactive-form)]})
   (wrap-anti-forgery #'r/app-routes)))


(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty (wrap-drawbridge app) {:port port
                            :join? false})))
