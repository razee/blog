(ns raziblog.views
  (:require [raziblog.db :as db]
            [clojure.string :as str]
            [hiccup.page :as hic-p]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "Locations: " title)]
   (hic-p/include-css "/css/styles.css")])

(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Home"]
   " | "
   [:a {:href "/add-location"} "Add a location"]
   " | "
   [:a {:href "/all-locations"} "View all locations"]
   " ] "])

(defn home-page
  []
  (hic-p/html5
    (gen-page-head "Home")
    header-links
    [:h1 "Home"]
    [:p "Shitfuck to store and display some 2D locations."]))

(defn add-location-page
  []
  (hic-p/html5
   (gen-page-head "Add a location")
    header-links
    [:h1 "Add a location"]
    [:form {:action "/add-location" :method "POST"}
     [:p "x value: " [:input {:type "text" :name "x"}]]
     [:p "y value: " [:input {:type "text" :name "x"}]]
     [:p [:input {:type "submit" :value "submit location"}]]]))

(defn add-location-results-page
  [{:keys [x y]}]
  (let [id (db/add-location-to-db x y)]
    (hic-p/html5
     (gen-page-head "Added a location")
     header-links
     [:h1 "Added a location"]
     [:p "Added [" x "," y "] (id: " id ") to the db."
     [:a {:href (str "/location/" id)} "See for yourself"]
       "."])))

(defn location-page
  [loc-id]
  (let [{x :x y :y} (db/get-xy loc-id)]
    (hic-p/html5
     (gen-page-head (str "Location " loc-id))
     header-links
     [:h1 "A single location"]
     [:p "id: " loc-id]
     [:p "x: " x]
     [:p "y: " y])))

(defn all-locations-page
  []
(let [all-locs (db/get-all-locations)]
  (hic-p/html5
   (gen-page-head "All locations in the db")
    header-links
    [:h1 "All locations"]
    [:table
     [:tr [:th "id"] [:th "x"] [:th "y"]]
     (for [loc all-locs]
       [:tr [:td (:id loc)] [:td (:x loc)] [:td (:y loc)]])])))
