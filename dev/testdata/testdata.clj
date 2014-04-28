(ns testdata.testdata
  (:require [clojure.pprint :refer [pprint]]
            [traveler.utils :refer [file-exists?]]))

(def testdata-path
  "Default test data path"
  "dev/testdata/")

(defn load-dtm
  "Load dtm file if it exits, or return empty vector"
  [path]
  (if (file-exists? path)
    (:data (load-string (slurp path)))
    []))

(defn load-testdata
  "Load test data"
  []
  (load-dtm (str testdata-path "testdata.dtm")))
