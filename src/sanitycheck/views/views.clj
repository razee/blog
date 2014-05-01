(ns sanitycheck.views.views
  (:require [sanitycheck.models.db :as db]
            [clojure.string :as str]
            [hiccup.page :refer [html5]]
            [hiccup.form :as f]
            [sanitycheck.views.layout :as l]
            [cemerick.friend :as friend])
  (:use ring.util.anti-forgery))

(defn login-form
  [req]
  (l/common "Sanity check - Login"
             [:p (if-let [identity (friend/identity req)]
             (apply str "Logged in, with these roles: "
               (-> identity friend/current-authentication :roles))
             "anonymous user")]
     [:div {:class "row"}
      [:div {:class "columns small-12"}
       [:h3 "Login"]
       [:div {:class "row"}
       [:form {:method "POST" :action "login" :class "columns small-4"}
             (anti-forgery-field)
        [:div "Username" [:input {:type "text" :name "username"}]]
        [:div "Password" [:input {:type "text" :name "password"}]]
        [:div [:input {:type "submit" :class "button" :value "Login"}]]]]]]))

(defn show-post
  [id r]
  (let [post (db/get-post id)
        {:keys [title body updated_at category]} (nth post 0)]
  (l/common (str "Sanity check - " title)
   [:h1 title]
   [:p "Updated on: " updated_at]
   [:p "Category: " category]
   [:body body])))

(defn show-all-posts
  []
  (html5
   [:ul.posts
     (for [{:keys [id title]} (db/get-all-posts)]
       [:li
         [:a {:href id} title]])]))

(defn all-posts
  []
  (let [posts (db/get-all-posts)]
    (html5
     (l/gen-page-head "All posts")
      (doall posts))))

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

(defn admin-post-summary [post]
  (let [{:keys [id title body created_at]} post]
   (html5
   [:section
    [:h3 title]
    [:h4 created_at]
    [:section body]
    [:section.actions
     [:a {:href (str "/admin/" id "/edit")} "Edit"] " / "
     [:a {:href (str "/admin/" id "/delete")} "Delete"]]])))


(defn home-page []
  []
  (l/common "Sanity check - Home"
  (map post-summary (db/get-all-posts))))

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
    (l/common
      "Sanity check - Edit post"
      [:h2 (str "Edit post " id)]
      (f/form-to [:put "save"]
        (anti-forgery-field)
        (f/label "title" "Title")
        (f/text-field "title" (:title post)) [:br]
        (f/label "body" "Body") [:br]
        (f/text-area {:rows 20} "body" (:body post)) [:br]
        (f/submit-button "Save")))))

