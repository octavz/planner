(ns planner.planner.repos.redis 
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
(defn set* [key] (redis (car/set key)))


