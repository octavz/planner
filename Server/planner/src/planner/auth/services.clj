(ns planner.auth.services
  (:use planner.auth.providers.sql)
  (:require [clj-time.core :as time]
            [clauth.user :as cluser]
            [clauth.token :as cltoken]
            [clauth.endpoints :as ep]
            ))


(def master-client (get-client "1"))

(def web-client-id (:client-id master-client))


