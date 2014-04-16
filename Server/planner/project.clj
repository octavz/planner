(defproject
  planner
  "0.1.0-SNAPSHOT"
  :repl-options
  {:init-ns planner.repl}
  :dependencies
  [[ring-server "0.3.1"]
   [ring/ring-core "1.2.2"]
   [com.taoensso/timbre "3.1.6"]
   [ragtime "0.3.7"]
   [environ "0.4.0"]
   ;[markdown-clj "0.9.41"]
   [korma "0.3.0"]
   [http-kit "2.1.18"]
   [liberator "0.11.0"]
   ;[lib-noir "0.8.1"]
   [clj-time "0.6.0"]
   [clauth "1.0.0-rc17"]
   ;[lein-light-nrepl "0.0.17"]
   [cheshire "5.3.1"]
   ;[com.taoensso/tower "2.0.2"]
   [hiccup "1.0.5"]
   ;[hiccup-bootstrap "0.1.2"]
   ;[org.clojure/tools.reader "0.8.3"]
   ;[org.clojure/clojurescript "0.0-2197"]
   [com.novemberain/validateur "2.0.0"]
   [org.clojure/clojure "1.6.0"]
   [log4j "1.2.17" :exclusions [javax.mail/mail javax.jms/jms com.sun.jdmk/jmxtools com.sun.jmx/jmxri]]
   [compojure "1.1.6"]
   ;[selmer "0.6.4"]
   [com.taoensso/carmine "2.6.0"]
   [postgresql/postgresql "9.1-901-1.jdbc4"]
   ]
  :bootclasspath true,
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
                                  [midje "1.6.3"]
                                  [ring/ring-devel "1.2.2"]],
                   :env {:dev true}}}
  :url "http://example.com/FIXME"
  :main planner.core
  :repl-options {:port 55555}
  ;:repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]}
  :plugins [
            [lein-ancient "0.5.5"]
            [lein-ring "0.8.10"]
            [lein-midje "3.1.1"]
            [lein-environ "0.4.0"]
            [ragtime/ragtime.lein "0.3.7"]]
  :description "FIXME: write description"
  :min-lein-version "2.0.0")
