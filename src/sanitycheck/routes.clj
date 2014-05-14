(ns sanitycheck.routes
  (:use ring.util.response)
  (:require [sanitycheck.views.views :as views]
            [sanitycheck.models.db :as db]
            [cemerick.friend :as friend]
            [compojure.core :as cc]
            [compojure.route :as route]))


(cc/defroutes public-routes
  (cc/GET "/" [] (views/home))
  (cc/GET "/posts"
          []
          (views/show-all-posts))
  (cc/GET "/posts/:id" [id :as r] (views/show-post id r)) ; (-> id :params (:id))))
  (cc/GET "/login" req (views/login-page req))
  (route/resources "/"))

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
   (friend/wrap-authorize
   protected-routes "admin")
   (route/not-found "lolnope"))
