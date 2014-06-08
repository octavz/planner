(ns planner.services.projects
  (:use
        planner.repos.redis
        planner.services.validation
        planner.util)
  (:require
            [planner.models.schema :as models]
            [planner.repos.sql :as repo]
            [clj-time.core :as time]
            [ring.util.response :as rutil]))

(defn handler-projects-get [ctx]
  (tryc (repo/get-project-by-id )))

(defn handler-project-save 
  [{{params :params} :request
    json :json {user :user} :current-session}]
  (tryc
    (let [rec {:name (:name json) 
               :description (:description json) 
               :parent_id (:parent_id json)} ]
      (clean-response 
        (if (:id json) 
          (repo/generic-update models/projects user (assoc rec :id (:id json)))
          (repo/generic-insert models/projects user (assoc rec :id (uuid))))))))

(defn handler-create-project [ctx]
  (let [{json :json} ctx
        project-id (uuid) 
        project-group-id (uuid)
        project-admin-group-id (uuid)
        user-id (-> ctx :request :current-session :user :id) ]
    (repo/all
      (let [ret (repo/insert-project project-id (:name json) (:desc json) (:parent json) user-id)]
        (repo/insert-group project-group-id project-id "users" 1)
        (repo/insert-group project-admin-group-id project-id "admin" 1)
        (repo/add-user-to-group user-id project-group-id)
        (repo/add-user-to-group user-id project-admin-group-id)
        {:data {:id (:id ret) :name (:name ret) :desc (:description ret) :parent (:parent_id ret)}} ))))

(defn handler-update-project 
  [ctx]
  (tryc
    (let [{json :json} ctx
          user-id (-> ctx :request :current-session :user :id)
          user-groups (-> ctx :request :current-session :user :groups) 
          project (:data ctx) ]
      (if project 
        
        )

      )
    {:ec (:not-allowed errors)}
  )
  
)


