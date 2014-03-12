(ns traveler.conf
  (:require [clojure.edn :as edn]
            [traveler.utils :refer [file-exists? throw-file-missing]]))

(def default-config-path "src/config/")

(defn load-edn
  [path]
  (if (file-exists? path)
    (edn/read-string (slurp path))
    (throw-file-missing path)))

(defn load-config []
  (load-edn (str default-config-path "config.edn")))
