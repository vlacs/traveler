(ns traveler.utils
  (:require [clojure.string :refer [split]]
            [helmsman.uri :as h-uri]
            [helmsman.navigation :as h-nav]
            [ring.util.response :refer [url-response]])
  (:import (java.io File)))

(defn file-exists?
  "Check if a file exists (boolean)"
  [path]
  (if (.isFile (File. path))
    true
    false))

(defn get-param
  "Get a parameter out of the liberator context"
  [ctx param]
  (get-in ctx [:request :params param]))

(defn referer
  "Get the referrer out of the liberator context"
  [ctx]
  (str (first (split (get-in ctx [:request :headers "referer"]) #"\?"))))

(defn resource-uri
  "Generate the relative uri to a resource"
  [request path]
  (str
   (h-uri/assemble (h-nav/id->uri-path request :traveler/resources))
   path))

(defn id-uri
  "Generate the relative uri based on the helmsman id"
  [request id]
  (h-uri/assemble (h-nav/id->uri-path request id)))

(defn error
  "Get the error out of the liberator context"
  [ctx]
  (let [error (get-in ctx [:request :query-params "error"])]
    (if (empty? error)
      false
      error)))
