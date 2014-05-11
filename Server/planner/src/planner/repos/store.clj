(ns planner.repos.store)
;(use 'planner.util)

;(require '[bitemyapp.revise.connection :refer [connect close]])
;(require '[bitemyapp.revise.query :as r])
;(require '[bitemyapp.revise.core :refer [run run-async]])

;(def conn (connect))

;(defn load-test-rethinkdb []
  ;(let [id (uuid)]
    ;(-> 
      ;(r/db "test")
      ;(r/table-db "load") 
      ;(r/insert {:name (uuid) :id id :urls [(uuid) (uuid)]})
      ;(run conn))
    ;(let [rec (-> (r/db "test") (r/table-db "load") (r/get id) (run conn))]
      ;{:data (-> :response rec (first))})))

;(require '[couchbase-clj.client :as c])
;(require '[couchbase-clj.util :as u])

;(c/defclient client {:bucket "default"
                     ;:uris ["http://127.0.0.1:8091/pools"]})

;(defn load-test-couchbase []
  ;(let [id (uuid)]
      ;(c/add-json client id {:name (uuid) :id id :urls [(uuid) (uuid)]})
    ;(let [rec (c/get-json client id)]
      ;{:data rec} )))

;(use 'korma.core)
;(use 'korma.db)
;(use 'planner.models.schema)

;(defn load-test-pg []
  ;(let [id (uuid)]
      ;(exec-raw ["insert into load(id,name,urls) values(?,?,?)" [id (uuid) (uuid)]])
    ;(let [rec (exec-raw ["select * from load where id=?" [id]] :results)]
      ;{:data (first rec)} )))

;(use 'cheshire.core)
;(require '[taoensso.carmine :as car :refer (wcar)])

;(def store-con {:pool {} :spec {}})

;(defmacro redis [& body] `(car/wcar store-con ~@body))

;(defn load-test []
  ;(let [id (uuid)]
      ;(redis (car/set id (generate-string {:name (uuid) :id id :urls [(uuid) (uuid)]})))
    ;(let [rec (redis (car/get id))]
      ;{:data (parse-string rec)} )))

;(require '[com.ashafa.clutch :as couch]) 

;(def client-couchdb (couch/get-database "test"))

;(defn load-test-couchdb []
  ;(let [id (uuid)]
      ;(couch/put-document client-couchdb {:name (uuid) :_id id :urls [(uuid) (uuid)]})
    ;(let [rec (couch/get-document client-couchdb id)]
      ;{:data (dissoc rec :_rev)} )))

;(defn load-test[] (load-test-pg))
;#_(load-test) 
