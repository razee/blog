(ns sanitycheck.views.layout
  (:use ring.util.response
        ring.util.anti-forgery)
  (:require [hiccup.page :refer [html5 include-css]]))



(defn gen-page-head
  [title]
  [:head
   [:title (str "Locations: " title)]
   (include-css "/css/styles.css")])


(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Home"]
   " | "
   [:a {:href "/all-posts"} "Show all posts"]
   " | "
   [:a {:href "/login"} "Login"]
   " ] "])

(defn common
  [title & body]
  (html5
  [:head [:meta {:name "9d060ecd23b33655d1127ee79a923d1859d6665b" :content "a0cabc30ddb2b143a3d823e1bec06472f0ae742f"}]]
   [:title title]
    header-links
    [:body body]))

(defn four-oh-four []
  (common "Page Not Found"
    [:div {:id "four-oh-four"}
    "The page you requested could not be found"]))
