(ns sanitycheck.views.layout
  (:use ring.util.response)
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
   [:title title]
    header-links
    [:body body]))

(defn four-oh-four []
  (common "Page Not Found"
    [:div {:id "four-oh-four"}
    "The page you requested could not be found"]))
