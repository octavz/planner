(ns planner.repos.redis
  (:use clojure.walk)
  (:require 
    [taoensso.carmine :as car :refer (wcar)]
    ))

(def store-con {:pool {} :spec {}})

(defmacro redis [& body] `(car/wcar store-con ~@body))
(defmacro begin-trans [wk & body] `(redis (car/atomically ~wk ~@body)))

(defn rec-get [key] (redis (car/hgetall key)))
(defn rec-set [key m] (redis (car/hmset* key m)))
(defn smembers* [key] (redis (car/smembers key)))
(defn sadd* [key vals] (redis (car/sadd key vals)))
(defn flushall* [] (redis (car/flushall)))  
(defn del* [key] (redis (car/del)))
(defn get* [key] (redis (car/get key)))
(defn set* [key value] (redis (car/set key value)))

(def ns-action "a")
(def ns-session "s")

(defn key-ns [namespace key] (format "%s:%s" namespace key) )

(defn cache-save
  [key values] 
  (rec-set (key-ns ns-session key) values) )

(defn get-session 
  [key]
  (when-let [rec (rec-get (key-ns ns-session key))]
    (if (empty? rec)
      nil
      (keywordize-keys (apply array-map rec)))))

(defn get-or-else
  [namespace key getter]
  (let [rec (rec-get (key-ns namespace key))]
    (if (or (nil? rec) (empty? rec))
      (let [res (getter)] 
        (rec-set (key-ns namespace key) res) 
        res)
      (keywordize-keys rec))))
