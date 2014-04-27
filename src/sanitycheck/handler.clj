(ns sanitycheck.handler
  (:use ring.util.response)
  (:require [sanitycheck.views.views :as views]
            [sanitycheck.models.db :as db]
            [compojure.core :as cc]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as json]
            [ring.middleware.session :as session]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kp]
            [ring.middleware.nested-params :as np]
            [cemerick.drawbridge])
            (:gen-class))

(def drawbridge-handler
  (-> (cemerick.drawbridge/ring-handler)
      (kp/wrap-keyword-params)
      (np/wrap-nested-params)
      (params/wrap-params)
      (session/wrap-session)))

(defn wrap-drawbridge [handler]
  (fn [req]
    (if (= "/repl" (:uri req))
      (drawbridge-handler req)
      (handler req))))

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
   (cc/ANY "/repl" [request]
           drawbridge-handler request)

(cc/defroutes app-routes
   public-routes
   protected-routes
   (route/not-found "lolnope"))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-body)
      (json/wrap-json-response)))



(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty (wrap-drawbridge app) {:port port
                            :join? false})))
