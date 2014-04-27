(ns sanitycheck.models.db
  (:require [clojure.java.jdbc :as sql]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:use sanitycheck.models.schema))

(def now (c/to-sql-date (t/today)))

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
       :updated_at now}
   ["id=?" (Integer/parseInt id)]))


(defn create-post
  [{:keys [id category title body created_at updated_at]}]
  (sql/insert!
   db
   :posts
   {:category category
    :title title
    :body body
    :created_at now
    :updated_at now}))


(defn delete-post
  [id]
  (sql/delete!
   db
   :posts
   ["id=?" (Integer/parseInt id)]))
