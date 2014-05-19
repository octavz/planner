(ns planner.util
  (:import (java.util UUID))
  (:use clojure.walk)
  (:require 
    ;[noir.io :as io]
    [clj-time.format :as timef  ] 
    [clj-time.coerce :as timec]           
    [clj-time.core :as tcore]
    [hiccup
     [page :refer [html5 include-js include-css]]
     [element :refer [link-to unordered-list]]]))

(defn now-ts [] (timec/to-timestamp (tcore/now) ))

(defn uuid  []  (str (UUID/randomUUID)))

(defmacro tryc[ & e]
  `(try ~@e
        (catch Exception e# {:er (.getMessage e#) :ec 1})))

(defmacro to-int [s] (list 'Integer/parseInt s))

(defn char-at [word idx] (subs word idx (inc idx)))

(def rex-email #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

(defn generic-get 
  [getter 
   {session :current-session 
    {params :params} :request} ]
  (let [off (:offset params)
        lim (:limit params)]
    {:data (getter 
             (:id params) 
             (:user  session)
             (if off (to-int off) 0)
             (if lim (to-int lim) 10))}))

(defn clean-response [data]
  {:data (dissoc data :created :updated :user_id 
                 :group_id :perm_public :perm_group 
                 :perm_owner :status)})

