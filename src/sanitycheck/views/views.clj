(ns sanitycheck.views.views
  (:require [sanitycheck.models.db :as db]
            [clojure.string :as str]
            [hiccup.page :refer [html5]]
            [hiccup.form :as f]
            [sanitycheck.views.layout :as l]
            [cemerick.friend :as friend]
            [net.cgrand.enlive-html :as html])
  (:use ring.util.anti-forgery
        markdown.core))



(html/defsnippet post-snippet "sanitycheck/views/post.html"
  [:.post]
  [post]
  [:.post] (html/set-attr :class (str "post-" (:id post)))
  [:a] (html/set-attr :href (str "/posts/" (:id post)))
  [:a] (html/content (:title post))
  ;[:a :spa] (html/content (:title post))
  ;[:span.author] (html/content "razi")
  [:.post-category] (html/content (:category post))
  [:.post-body] (html/html-content (md-to-html-string (:body post))))

(html/deftemplate login-form "sanitycheck/views/login.html"
   []
   identity)

(html/deftemplate edit-post-page "sanitycheck/views/edit_post.html"
  [post]
  [:form] (html/set-attr :action (str "/admin/" (:id post) "/save"))
  [:.post-title] identity
  [:.post-category] identity
  [:.post-body] identity)


(html/defsnippet admin-summary "sanitycheck/views/post.html"
  [:.post]
  [post])

(html/deftemplate show-post-page "sanitycheck/views/post.html"
  [[post]]
  [:title] (html/content (:title post))
  [:h1] (html/content (:title post))
  ;[:span.author] (html/content "razi")
  [:p.category] (html/content (:category post))
  [:p.updated_at] (html/content (:updated_at post))
  [:.post-body] (html/html-content (:body post)))

(defn admin-post-summary [post]
  (let [{:keys [id title body created_at]} post]
   (html5
   [:section
    [:h3 title]
    [:h4 created_at]
    [:section body]
    [:section.actions
     [:a {:href (str "/admin/" id "/edit")} "Edit"] "/"
     [:a {:href (str "/admin/" id "/delete")} "Delete"]]])))

(html/deftemplate home-page "sanitycheck/views/home.html"
  [posts]
  [:title] (html/append "Sanity check")
  [:.posts] (html/append (map post-snippet posts)))


(html/deftemplate all-posts-page "sanitycheck/views/post.html"
 [posts]
 [:title] (html/content "All posts")
 [:.posts] (html/content (map post-snippet posts)))

(defn show-all-posts
  [])

(defn login-page
  [req]
  (login-form))


(defn show-post
  [id r]
  (let [post (db/get-post id)]
  (reduce str (show-post-page post))))

(defn home
  []
  (let [posts (db/get-all-posts)]
    (reduce str (home-page posts))))

(defn post-summary [post]
  (let [{:keys [id title body created_at]} post]
   (html5
   [:section
    [:h3 [:a {:href (str "posts/" id)} title]]
    [:h4 created_at]
    [:section body]])))
    ;[:section.actions
     ;[:a {:href (str "/admin/" id "/edit")} "Edit"] " / "
     ;[:a {:href (str "/admin/" id "/delete")} "Delete"]]])))



;(defn home
;  []
;  (if-let [posts (db/get-all-posts)]
;    (apply str (home-page (nth posts 0)))))

(defn admin-page
  []
  (html5
     (l/common "Sanity check - Admin page"
      [:h1 "Admin page"]
      [:h2 "All my posts"]
      [:a {:href "/admin/add"} "Add"]
      (map admin-post-summary (db/get-all-posts)))))

(defn add-post
  []
  (html5
   (l/common "Sanity check - Add post"
      [:h2 "Add post"]
      (f/form-to [:post "/admin/create"]
       (anti-forgery-field)
       (f/label "title" "Title")
        (f/text-field "title") [:br]
         (f/label "category" "Category") [:br]
         (f/text-field "category") [:br]
         (f/label "body" "Body") [:br]
          (f/text-area {:rows 42} "body") [:br]
           (f/submit-button "SAVE THAT SHIT YO")))))

(defn edit-post
  [id]
  (let [post (db/get-post id)]
    (reduce str (edit-post-page (nth post 0)))))

(defn fielder
  [field n text]
  (list (f/label n text)
    (field n)
    [:br]))

(defn registration-page
  []
  (f/form-to [:post "/register"]
     (fielder f/text-field :id "nickname")
     (fielder f/password-field :pass "password")
     (fielder f/password-field :pass1 "retype password plz")
      (f/submit-button "Create account")))
