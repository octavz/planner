(ns planner.test.handler
  (:use midje.sweet
        ring.mock.request
        planner.handler))

  (facts "main route"
    (let [response (app (request :get "/"))]
      (fact "returns http 200" (:status response) => 200)
      (fact "body is html" (:body response) => (contains "<html>")
      (fact "body is html" (:body response) => (contains "dsdsdhtml>")
            )))

  (facts "not-found route"
    (let [response (app (request :get "/invalid"))]
      (fact "for invalid route returns 404" (:status response) => 404)))
