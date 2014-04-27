(ns sanitycheck.models.db
  (:require [clojure.java.jdbc :as sql])
  (:use sanitycheck.models.schema))


(defn create-table-posts []
  (sql/db-do-commands db
    (sql/create-table-ddl
       :posts
       [:id "serial primary key"]
       [:category "varchar"]
       [:title "varchar"]
       [:body "text"]
       [:created_at "date"]
       [:updated_at "date"])))


(def current_time (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") (new java.util.Date)))

(defn create-user
  [user]
  )

(defn empty-results
  [res]
  (if (empty? res)
    (str "Sorry, nothing in the database matching that query")
    res))


(defn get-post
  [id]
  (sql/query
     db
     ["select * from posts where id=?" (Integer/parseInt id)]))

(defn get-all-posts
  []
  (sql/query
   db
   ["select * from posts order by updated_at desc"]))


(defn update-post
  [{:keys [id title body]}]
  (sql/update!
   db
   :posts
   {:title title
       :body body
       :updated_at current_time}
   ["id=?" (Integer/parseInt id)]))


(defn create-post
  [{:keys [id title category body created_at updated_at]}]
  (sql/insert!
   db
   :posts
   {:title title
    :category category
    :body body
    :created_at current_time
    :updated_at current_time}))


(defn delete-post
  [id]
  (sql/delete!
   db
   :posts
   ["id=?" (Integer/parseInt id)]))
