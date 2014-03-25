(ns planner.test.services
  (:use midje.sweet
        ring.mock.request
        planner.util
        planner.services.impl
        planner.repos.redis
        planner.repos.sql
        planner.core)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]) )

(def valid-email "aaa@aaa.com")
(def bad-email "aaaaaa.com")
(def valid-pass "12345")
(def bad-pass "")

(facts "authentication & authorization"

  (fact "extract access-token"
    (prerequisite
      (oauth-login anything) =>
      {:session
       {:access_token .t.,
        :csrf-token "csrf-token"},
       :status 302,
       :headers {"Location" "/"},
       :body ""} )
    (handler-login-post .req.) => (contains {:access-token .t.}))

  (fact "authorize by header"
    (prerequisite
      (get-session "token") => {:id 1})
    (check-user {:request
                 {:headers
                  {"authorization" "token"}}}) => truthy)

  (fact "fail to authorize when no header is present"
    (prerequisite
      (get-session anything) => .x.)
    (check-user {}) => falsey)

  (fact "save user should exist and accept a request and id"
    (handler-user-save
      {:request {}} .id.) => truthy)

  (fact "register should not be called without email and password"
    (v-user-register {}) => (contains {:email anything
                                       :password anything}))

  (fact "register should not be called without email"
    (v-user-register
      {:password bad-pass}) => (contains {:email anything}))

  (fact "register should not be called without password"
    (v-user-register
      {:email valid-email}) => (contains {:password anything}))

  (fact "register should not be called with invalid email"
    (v-user-register
      {:email bad-email}) => (contains {:email anything}))

  (fact "register should not be called with invalid  password"
    (v-user-register
      {:email valid-email
       :password bad-pass}) => (contains {:password anything}))

  (fact "register should return"
    (handler-user-register
      {:json
       {:email .e.
        :password bad-pass}}) => (contains {:result anything})
    (provided
      (cluser/bcrypt anything) => .c.
      (uuid) => .guid.
      (get-user-by-email .e.) => nil
      (create-user {:login .e.
                    :password .c.
                    :id .guid.}) => anything))

  (fact "register should fail for existing email"
    (handler-user-register
      {:json
       {:email .e.
        :password .p.}}) => (contains {:err anything})
    (provided
      (get-user-by-email anything) => {:login .e.}))

  (fact "get user should return current user"
    (handler-user-get
      {:current-session
       {:uid .id.}}) => {:entry {:email valid-email}}
    (provided
      (get-user .id.) => {:login valid-email}))

  (fact "user should have access to specific routes"
    (check-user-access .id.) => true
    )
  )

