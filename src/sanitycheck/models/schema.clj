(ns sanitycheck.models.schema)

(def db
   (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/sanitycheck"))



;(declare users email account)
;(defentity user
;
;  (pk :id)
;  (has-many email)
;  (belongs-to account)
;
;  (many-to-many posts :users_posts))
;
;
;(defentity email
;  (belongs-to user))
;
;
;(defentity account
;  (has-one user))
;
;(defentity posts
;  (many-to-many user :user_posts))
