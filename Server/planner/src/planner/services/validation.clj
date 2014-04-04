(ns planner.services.validation
  (:use validateur.validation
        planner.util))
(def v-user
  (validation-set
    (presence-of :email)
    (presence-of :password)
    (length-of :password 
               :within (range 5 51))
    (format-of :email 
               :format rex-email)))
(def v-project
  (validation-set
    (presence-of :name)))

(def 
  errors 
  {
   :unknown 1
   :email-exists 2   
   })

