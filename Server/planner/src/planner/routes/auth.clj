(ns planner.routes.auth
  (:use compojure.core
        planner.auth.services)
  (:require 
            [compojure.route :as route]
            [clauth
             [middleware :as mw]
             [endpoints :as ep]
             ]
            [hiccup.bootstrap
             [middleware :refer [wrap-bootstrap-resources]]
             [page :refer [include-bootstrap fixed-layout]]]
            [hiccup
             [page :refer [html5 include-js include-css]]
             [element :refer [link-to unordered-list]]]
            ))

(defn nav-menu [req]
  (if (ep/logged-in? req)
    [(link-to "/logout" "Logout")]
    [(link-to "/login" "Login")]))

(defn layout [req title & body]
  (html5
    [:head
      [:title (or title "Clauth demo")]
     (include-js "http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"
                 "//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js")
     (include-css "//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css")
     ]
    [:body
      (fixed-layout
        [:div {:class "navbar"}
          [:div {:class "navbar-inner"}
            [:div {:class "container"}
              [:a {:href "/" :class "brand"} "Clauth"]
              (unordered-list {:class "nav"} (nav-menu req))]]]
        [:h1 (or title "Clauth demo")]
        body)]))

(defn use-layout
  "Wrap a response with a layout"
  [req title response]
  (assoc response :body (layout req title (response :body))))

(defn login [req] 
  (let [res ((ep/login-handler {:client web-client-id}) req)
        at (-> res :session :access_token)
        ret (assoc res :cookies {"access_token" {:path "/" :value at}})]
    ret))

(defroutes auth-routes
  (GET "/login" req
       (use-layout req "Login" ((ep/login-handler master-client) req)))
  (POST "/login" req
        (use-layout req "Login" (login req)))
  (POST "/token" req
        ((ep/token-handler) req))
  (GET "/authorization" req
       (use-layout req "Authorize App" ((ep/authorization-handler) req)))
  (route/resources "../../Client/app")
  (route/files "/" {:root "../../Client/app"})
  (route/not-found "Not Found")
  )
