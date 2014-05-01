(ns sanitycheck.models.db
  (:use markdown.core)
  (:require [clojure.java.jdbc :as sql]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds]))
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

(defn create-table-users
  []
  (sql/db-do-commands db
    (sql/create-table-ddl
    :users
    [:id "serial primary key"]
    [:username "varchar"]
    [:password "varchar"]
    [:email "varchar"]
    [:roles "varchar"])))

(defn create-user
  [{:keys [username password email admin] :as user-data}]
  (sql/insert!
   db
   :users
   (assoc (dissoc user-data :admin) :password (creds/hash-bcrypt password)
              :roles (str (into #{::user} (when admin [::admin]))))))

(defn get-user
  [req]
  (sql/query
   db
   ["select * from users where id=?" (:id req)]))

(defn empty-results
  [res]
  (if (empty? res)
    (str "Sorry, nothing in the database matching that query")
    res))


(defn get-post
  [req]
  (sql/query
     db
     ["select * from posts where id=?" (Integer/parseInt req)]))

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

(defn delete-user
  [id]
  (sql/delete!
   db
   :users
   ["id=?" (Integer/parseInt id)]))

(defn get-all-users
  []
  (sql/query
   db
   ["select * from users"]))

(def friendly-db
  (into {} (for [user (get-all-users)
        :let [u (:username user)]]
  (assoc {} u user))))


(derive ::admin ::user)
