(ns planner.repl
  (:use 
    planner.models.schema
    planner.repos.redis
    planner.repos.oauth
    planner.util)
  (:require 
    [clauth.store :as cl]))

;(use 'midje.repl)
;(autotest)
(use 'planner.services.users)
(use 'planner.services.projects)

;(use 'ragtime.core)
;(require 'ragtime.sql.database)
;(use 'ragtime.sql.files)
;(migrate-all tdb (migrations))
;(rollback-last tdb)
