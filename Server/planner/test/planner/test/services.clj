(ns planner.test.services
  (:use midje.sweet
        ring.mock.request
        planner.services.impl
        planner.core)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]) )


(facts "login"
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
    (handler-user-register ..req.. ..email.. ..pass..) => 
    (contains {:planner.service.impl/access-token "token"})))

