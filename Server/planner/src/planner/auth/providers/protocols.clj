(ns planner.auth.providers.protocols)

(defprotocol AuthProvider
  (get-token-by-user [this id]))
