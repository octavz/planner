(defproject
  planner
  "0.1.0-SNAPSHOT"
  :repl-options
  {:init-ns planner.repl}
  :dependencies
  [[ring-server "0.3.1"]
   [com.taoensso/timbre "3.0.0"]
   [ragtime "0.3.4"]
   [environ "0.4.0"]
   [markdown-clj "0.9.41"]
   [korma "0.3.0-RC6"]
   [http-kit "2.1.14"]
   [liberator "0.10.0"]
   [clauth "1.0.0-rc17"]
   [lein-light-nrepl "0.0.11"]
   [cheshire "5.3.1"]
   [com.taoensso/tower "2.0.1"]
   [org.clojure/tools.reader "0.8.3"]
   [org.clojure/clojurescript "0.0-2138"]
   [org.clojure/clojure "1.5.1"]
   [log4j "1.2.17" :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [lib-noir "0.8.0"]
   [compojure "1.1.6"]
   [selmer "0.5.9"]
   [postgresql/postgresql "9.1-901.jdbc4"]]
  :ring {:handler planner.handler/app,
         :init planner.handler/init,
         :destroy planner.handler/destroy}
  :ragtime {:migrations ragtime.sql.files/migrations,
            :database "jdbc:postgresql://localhost/planner?user=postgres&password=root"}
  :profiles {:uberjar {:aot :all},
             :production {:ring {:open-browser? false, 
                                 :stacktraces? false, 
                                 :auto-reload? false}},
             :dev {:dependencies [[ring-mock "0.1.5"] 
                                  [com.taoensso/carmine "2.4.5"] 
                                  [midje "1.6.0"] 
                                  [ring/ring-devel "1.2.1"]],
                   :env {:dev true}}}
  :url "http://example.com/FIXME"
  :main planner.core
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  :plugins [[lein-ring "0.8.10"]
            [lein-midje "3.1.1"]
            [lein-environ "0.4.0"]
            [ragtime/ragtime.lein "0.3.4"]]
  :description "FIXME: write description"
  :min-lein-version "2.0.0")
