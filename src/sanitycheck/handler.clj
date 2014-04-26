(ns sanitycheck.handler
  (:use ring.util.response)
  (:require [sanitycheck.views.views :as views]
            [sanitycheck.models.db :as db]
            [compojure.core :as cc]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as middleware])
            (:gen-class))

(cc/defroutes public-routes
  (cc/GET "/" [] (views/home-page))
  (cc/GET "/all-posts"
          []
          (views/show-all-posts))
  (cc/GET "/posts/:id" [id] (views/show-post id))
  (cc/GET "/login" [] (views/login-form))
  ;(cc/POST "/show-user"
   ;        {params :params}
    ;       (views/show-user params))
   (route/resources "/"))
;  (cc/GET "/location/:loc-id"
 ;         [loc-id]
  ;        (views/location-page loc-id))
  ;(cc/GET "/all-locations"
  ;        []
          ;(views/all-locations-page))
 ; (route/not-found "Not Found"))

(cc/defroutes protected-routes
   (cc/GET "/admin" [] (views/admin-page))
   (cc/GET "/admin/add" [] (views/add-post))
   (cc/POST "/admin/create" [& params]
      (do (db/create-post params)
        (redirect "/admin")))
   (cc/GET "/admin/:id/edit" [id] (views/edit-post id))
   (cc/PUT "/admin/:id/save" [& params]
      (do (db/update-post params)
        (redirect "/admin")))
   (cc/GET "/admin/:id/delete" [id]
      (do (db/delete-post id)
        (redirect "/admin"))))

(cc/defroutes app-routes
   public-routes
   protected-routes
   (route/not-found "lolnope"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(defn -main [& args]
  (let [port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))
        ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")]
  (jetty/run-jetty #'app {:port port
                          :host ip
                          :join? false})))
;(defn -main
;  [& [port]]
;  (let [port (Integer. (or port
;                           (System/getenv "PORT")
;                           5000))]
;    (jetty/run-jetty #'app {:port port
;                            :join? false})))
