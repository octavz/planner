(ns planner.test.handler
  (:use midje.sweet
        ring.mock.request
        planner.core))

(facts "main route"
  (let [response (handler (request :get "/"))]
    (fact "returns http 200" 
      (:status response) => 200)
    (fact "body is html" 
      (type (:body response)) => java.io.File)))

(facts "login get"
  (let [response (handler (request :get "/login"))]
    (fact "returns http 200" 
      (:status response) => 200)
    (fact "contains form" 
      (:body response) => (contains "<form"))
    (fact "contains username" 
      (:body response) => (contains "username"))
    (fact "contains password" 
      (:body response) => (contains "password"))
    (fact "contains action login" 
      (:body response) => (contains "/login"))
    ))

(facts "not-found route"
  (let [response (handler (request :get "/invalid"))]
    (fact "for invalid route returns 404" 
      (:status response) => 404)))


