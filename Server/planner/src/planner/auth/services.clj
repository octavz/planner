(ns planner.auth.services
  )

(defprotocol Auth 
  (register-client [this])
  (register-user [this email password])
  (register-openid [this oid-type auth-token])
  (login [this email password])
  (oid-auth [this auth-token])
  )

