(defproject ionic-cljs "0.1.0"
  :description "A simple example of how to use lein-cljsbuild"
  :source-paths ["src-clj"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [im.chit/gyr "0.3.1"]
                 [com.cemerick/piggieback "0.1.3"]
                 [org.clojure/clojurescript "0.0-2371"
                  :exclusions [org.apache.ant/ant]]
                 ]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.7"]]
  :cljsbuild {
    :builds [{:source-paths ["src-cljs"]
              :compiler {:output-to "www/js/gen/main.js"
                         :output-dir "www/js/gen"
                         :optimizations :whitespace
                         :pretty-print true}}]})
