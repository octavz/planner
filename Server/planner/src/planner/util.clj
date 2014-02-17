(ns planner.util
  (:require [noir.io :as io]
            [markdown.core :as md]))
(require '(clj-time [format :as timef]
                    [core :as tcore]
                    [coerce :as timec]))

(defn md->html
  "reads a markdown file from public/md and returns an HTML string"
  [filename]
  (->>
    (io/slurp-resource filename)
    (md/md-to-html-string)))

(defn now-ts [] (timec/to-timestamp (tcore/now) ))
