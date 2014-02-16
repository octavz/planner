(ns planner.util
  (:require [noir.io :as io]
            [clj-time.format :as timef  ] 
            [clj-time.coerce :as timec]           
            [clj-time.core :as tcore]
            [hiccup.bootstrap
             [middleware :refer [wrap-bootstrap-resources]]
             [page :refer [include-bootstrap fixed-layout]]]
            [hiccup
             [page :refer [html5 include-js include-css]]
             [element :refer [link-to unordered-list]]]
            [markdown.core :as md]  
            ))

(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (io/slurp-resource filename)
    (md/md-to-html-string)))

(defn now-ts [] (timec/to-timestamp (tcore/now) ))

(defn layout [title & body]
  (html5
    [:head
     [:title (or title "Clauth demo")]
     (include-js "http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"
                 "//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js")
     (include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css")
     ]
    [:body
     (fixed-layout
       [:div {:class "navbar"}
        [:div {:class "navbar-inner"}
         ]]
       body)]))

(defn use-layout
  "Wrap a response with a layout"
  [title response]
  (assoc response :body (layout title (response :body))))
