(ns service.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [ring.middleware.reload :as reload]
            [compojure.core :as ccore]
            [compojure.handler :as handler]
            [compojure.route :as route]
            ))

(ccore/defroutes app-routes
  (ccore/GET "/" [] "hello tasks")
  (route/not-found "<p>Page not found.</p>")) ;; all other, return 404

(defn in-dev? [& args] true) ;; TODO read a config variable from command line, env, or file?

(defn -main [& args] ;; entry point, lein run will pick up and start from here
  (let [handler (if (in-dev? args)
                  (reload/wrap-reload (handler/site #'app-routes)) ;; only reload when dev
                  (handler/site app-routes))]
    (do
      (prn "Server started")
      (run-server handler {:port 8090})
    )))
