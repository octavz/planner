(ns planner.repl
  (:use planner.handler
        ring.server.standalone
        [ring.middleware file-info file])
  (:require 
            [clauth
             [middleware :as mw]
             [endpoints :as ep]
             [client :refer [client-store clients register-client]]
             [token :refer [token-store]]
             [user :refer [user-store]]
             [auth-code :refer [auth-code-store]]])
  )
(use 'planner.auth.postgres)

(defonce server (atom nil))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (-> #'app
      ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-file "resources")
      ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 9090)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :init init
                    :auto-reload? true
                    :destroy destroy
                    :join? false}))
    (println (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(do
    (reset! token-store (create-token-store))
    (reset! auth-code-store (create-code-store))
    (reset! client-store (create-client-store))
    (reset! user-store (create-user-store))
)
