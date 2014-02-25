(ns planner.test.services
  (:use midje.sweet
        ring.mock.request
        planner.services.impl
        planner.repos.redis
        planner.core)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]) )

(facts "authentication & authorization"
  (fact "extract access-token"
    (prerequisite 
       (oauth-login anything) => 
          {:session 
           {:access_token "token", 
            :csrf-token "csrf-token"}, 
           :status 302, 
           :headers {"Location" "/"}, 
           :body ""} )
      (handler-login-post ..req..) => 
    (contains {:planner.services.impl/access-token "token"})  )
  (fact "register user"
    (prerequisite 
      (oauth-login anything) => 
      {:session 
       {:access_token "token", 
        :csrf-token "csrf-token"}, 
       :status 302, 
       :headers {"Location" "/"}, 
       :body ""} )
    (handler-user-register ..req.. ) => 
    (contains {:result "token"}))
  
  (fact "authorize by header"
    (prerequisite
      (get-session "token") => {:id 1})
    (check-user {:request {:headers {"authorization" "token"}}} ) => true)
  )

