(ns traveler.utils
  (:import (java.io File)))

(defn file-exists?
  [path]
  (if (.isFile (File. path))
    true
    false))

(defn throw-file-missing
  [path]
  (throw (ex-info
          (format "File missing at (%s)" path)
          {:cause :file-missing :file-path path})))
