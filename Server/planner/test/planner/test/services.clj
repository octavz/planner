(ns planner.test.services
  (:use midje.sweet
        ring.mock.request
        planner.util
        planner.services.impl
        planner.repos.redis
        planner.repos.sql
        korma.core
        korma.db
        planner.core)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]) )

(def valid-email "aaa@aaa.com")
(def bad-email "aaaaaa.com")
(def valid-pass "12345")
(def bad-pass "")

(facts "services"

  ;(fact "extract access-token"
  ;(prerequisite
  ;(do-login anything) =>
  ;{:session
  ;{:access_token .t. }
  ;:status 302
  ;:headers {"Location" "/"}
  ;:body ""} )
  ;((handler-login-post .req.) :session) => (contains {:access_token .t.}))

  ;(fact "authorize by header"
  ;(prerequisite
  ;(get-session "token") => {:id 1})
  ;(check-user {:request
  ;{:headers
  ;{"authorization" "token"}}}) => truthy)

  ;(fact "fail to authorize when no header is present"
  ;(prerequisite
  ;(get-session anything) => .x.)
  ;(check-user {}) => falsey)

  ;(fact "save user should exist and accept a request and id"
  ;(handler-user-save
  ;{:request {}} .id.) => truthy)

  ;(fact "register should not be called without email and password"
  ;(v-user-register {}) => (contains {:email anything
  ;:password anything}))

  ;(fact "register should not be called without email"
  ;(v-user-register
  ;{:password bad-pass}) => (contains {:email anything}))

  ;(fact "register should not be called without password"
  ;(v-user-register
  ;{:email valid-email}) => (contains {:password anything}))

  ;(fact "register should not be called with invalid email"
  ;(v-user-register
  ;{:email bad-email}) => (contains {:email anything}))

  ;(fact "register should not be called with invalid  password"
  ;(v-user-register
  ;{:email valid-email
  ;:password bad-pass}) => (contains {:password anything}))

  ;(fact "register should return"
  ;(handler-user-register
  ;{:json
  ;{:email .e.
  ;:password bad-pass}}) => (contains {:result anything})
  ;(provided
  ;(cluser/bcrypt anything) => .c.
  ;(uuid) => .guid.
  ;(get-user-by-email .e.) => nil
  ;(create-user {:login .e.
  ;:password .c.
  ;:id .guid.}) => anything))

  ;(fact "register should fail for existing email"
  ;(handler-user-register
  ;{:json
  ;{:email .e.
  ;:password .p.}}) => (contains {:err anything})
  ;(provided
  ;(get-user-by-email anything) => {:login .e.}))

  ;(fact "get user should return current user"
  ;(handler-user-get
  ;{:current-session
  ;{:uid .id.}}) => {:entry {:email valid-email} :perm anything}
  ;(provided
  ;(user-get-perm .id.) => anything
  ;(get-user .id.) => {:login valid-email}))


  ;(fact "get user should return permissions"
  ;(handler-user-get
  ;{:current-session
  ;{:uid .id.}}) => {:entry {:email valid-email} :perm [1 2 3 4]}
  ;(provided
  ;(get-user .id.) => {:login valid-email}))

  (fact "should create project group with name users, add user to group, add project in a single transaction"
    (handler-create-project (dev-req "1" ["1"] {} {:name "name" :desc "desc" :parent "parent"})) => {:data {:name "name" :desc "desc" :parent "parent" :id "pid"}}
    (provided 
      (uuid) =streams=> ["pid", "gid"] :times 2
      (store-insert-group "gid" "pid" "users" 1) => anything :times 1
      (store-add-user-to-group "1" "gid") => anything :times 1
      (store-insert-project "pid" "name" "desc" "parent" "1") => 
      {:updated "" :created "" :description "desc" :id "pid" :name "name" :parent_id "parent" :perm_public 1 :status 0 :user_id "1"} :times 1
      )
    )
  
  )

(facts "repo"
  (fact "should create project group with name users, add user to group, add project in a single transaction"
    (handler-create-project (dev-req "1" ["1"] {} {:name "name" :desc "desc" :parent nil})) => {:data {:name "name" :desc "desc" :parent nil :id anything}}
    )
  
  
  )
