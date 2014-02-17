(ns planner.test.handler
  (:use midje.sweet
        ring.mock.request
        planner.auth.services
        planner.handler)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [planner.auth.providers.sql :as sql]
            ))

(do
  (reset! clauth.token/token-store (sql/create-token-store))
  (reset! clauth.auth-code/auth-code-store (sql/create-code-store))
  (reset! clauth.client/client-store (sql/create-client-store))
  (reset! clauth.user/user-store (sql/create-user-store)))

(facts "main route"
  (let [response (app (request :get "/"))]
    (fact "returns http 200" (:status response) => 200)
    (fact "body is html" (:body response) => (contains "<html>")
      )))

(facts "not-found route"
  (let [response (app (request :get "/invalid"))]
    (fact "for invalid route returns 404" (:status response) => 404)))

(facts "authentication"
  (fact "login"
    (prerequisite
      (cluser/authenticate-user anything anything) => {:password "pass"
                                                       :openid_token nil 
                                                       :openid_type nil 
                                                       :login "test@email.com" 
                                                       :id "2"}
      (get-or-create-token anything anything) => {:token "token"})

    (fact "should return the right key" (login "test@email.com" "pass") => "token"))

  (fact "token generation for valid tokens"
    (prerequisite
      (sql/get-token-by-user anything) => {:token "token" :expires (time/plus (time/now) (time/days 1)) }
      (cltoken/create-token anything anything) => {:token "newid" :expires (time/plus (time/now) (time/days 1)) })
    (get-or-create-token default-client-id "1") => (contains {:token "token"})
    )

  (fact "token generation for expired tokens"
    (prerequisite
      (sql/get-token-by-user anything) => {:token "token" :expires (time/minus (time/now) (time/days 1)) }
      (cltoken/create-token anything anything) => {:token "newid" :expires (time/plus (time/now) (time/days 1)) })
    (get-or-create-token default-client-id "") => (contains {:token "newid"})
    )
  )
