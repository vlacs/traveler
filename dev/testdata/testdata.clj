(ns testdata.testdata
  (:require [clojure.pprint :refer [pprint]]
            [traveler.utils :refer [file-exists?]]))

(def testdata-path "dev/testdata/")

(defn load-dtm
  [path]
  (if (file-exists? path)
    (vector (read-string (slurp path)))
    []))

(defn load-testdata []
  (load-dtm (str testdata-path "testdata.dtm")))
