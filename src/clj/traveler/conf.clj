(ns traveler.conf
  (:require [clojure.edn :as edn]
            [traveler.utils :refer [file-exists? throw-file-missing]]))

(def default-config-path
  "Default config path"
  "src/config/")

(defn load-edn
  "Load an edn file if it exists"
  [path]
  (if (file-exists? path)
    (edn/read-string (slurp path))
    (throw-file-missing path)))

(defn load-config
  "Load the config file"
  []
  (load-edn (str default-config-path "config.edn")))
