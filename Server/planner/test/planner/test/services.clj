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
      (do-login anything) =>
      {:session
       {:access_token .t. }
       :status 302
       :headers {"Location" "/"}
       :body ""} )
    ((handler-login-post .req.) :session) => (contains {:access_token .t.}))

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
       {:uid .id.}}) => {:entry {:email valid-email} :perm anything}
    (provided
      (user-get-perm .id.) => anything
      (get-user .id.) => {:login valid-email}))

  (fact "read should be allowed for public"
    (can-read? {:group 1 :perm "0001" :user 1} {:group 10 :user 10}) => true
    (can-read? {:group 1 :perm "0003" :user 1} {:group 10 :user 10}) => true)

  (fact "read should be allowed for same user"
    (can-read? {:group 1 :perm "0100" :user 1} {:group 10 :user 1}) => true
    (can-read? {:group 1 :perm "0300" :user 1} {:group 10 :user 1}) => true)

  (fact "read should be allowed for same group"
    (can-read? {:group 1 :perm "0010" :user 1} {:group 1 :user 10}) => true
    (can-read? {:group 1 :perm "0030" :user 1} {:group 1 :user 10}) => true)

  (fact "read should not be allowed for wrong permission and public"
    (can-read? {:group 1 :perm "0000" :user 1} {:group 10 :user 10}) => false
    (can-read? {:group 1 :perm "0002" :user 1} {:group 10 :user 10}) => false)

  (fact "read should not be allowed for wrong permission and same user"
    (can-read? {:group 1 :perm "0210" :user 1} {:group 10 :user 1}) => false
    (can-read? {:group 1 :perm "0230" :user 1} {:group 10 :user 1}) => false)

  (fact "read should not be allowed for wrong permission and same group"
    (can-read? {:group 1 :perm "0120" :user 1} {:group 1 :user 10}) => false
    (can-read? {:group 1 :perm "0320" :user 1} {:group 1 :user 10}) => false)

  (fact "write should be allowed for public"
    (can-write? {:group 1 :perm "0002" :user 1} {:group 10 :user 10}) => true
    (can-write? {:group 1 :perm "0003" :user 1} {:group 10 :user 10}) => true)

  (fact "write should be allowed for same user"
    (can-write? {:group 1 :perm "0200" :user 1} {:group 10 :user 1}) => true
    (can-write? {:group 1 :perm "0300" :user 1} {:group 10 :user 1}) => true)

  (fact "write should be allowed for same group"
    (can-write? {:group 1 :perm "0020" :user 1} {:group 1 :user 10}) => true
    (can-write? {:group 1 :perm "0030" :user 1} {:group 1 :user 10}) => true)

  (fact "write should not be allowed for wrong permission and public"
    (can-write? {:group 1 :perm "0000" :user 1} {:group 10 :user 10}) => false
    (can-write? {:group 1 :perm "0001" :user 1} {:group 10 :user 10}) => false)

  (fact "write should not be allowed for wrong permission and same user"
    (can-write? {:group 1 :perm "0120" :user 1} {:group 10 :user 1}) => false
    (can-write? {:group 1 :perm "0130" :user 1} {:group 10 :user 1}) => false)

  (fact "write should not be allowed for wrong permission and same group"
    (can-write? {:group 1 :perm "0210" :user 1} {:group 1 :user 10}) => false
    (can-write? {:group 1 :perm "0310" :user 1} {:group 1 :user 10}) => false)

  (fact "get user should return permissions"
    (handler-user-get
      {:current-session
       {:uid .id.}}) => {:entry {:email valid-email} :perm [1 2 3 4]}
    (provided
      (get-user .id.) => {:login valid-email}))
  )

