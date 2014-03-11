(ns planner.repos.protocols)

(defprotocol AuthProvider
  (get-token-by-user [this id]))
