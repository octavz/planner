(defproject runner "0.0.1"
    :dependencies [[org.clojure/clojure "1.5.1"]
                   [compojure "1.1.6"]
                   [ring/ring-devel "1.2.1"]
                   [ring/ring-core "1.2.1"]
                   [http-kit "2.1.14"]]
    
  :main service.core
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}

  )


