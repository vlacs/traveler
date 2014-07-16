(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer :all]
            [clojure.test :as test]
            [datomic.api :as d]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [traveler]
            [traveler.test-config :as tt-config]))

(defn go
  "Start the system from the REPL"
  []
  (tt-config/start!)
  (traveler/start! tt-config/system))

(defn reset
  "Reset your environment from the REPL"
  []
  (tt-config/stop!)
  (refresh :after 'user/go))

(defn touch-that
  "Execute the specified query on the current DB and return the
   results of touching each entity.

   The first binding must be to the entity.
   All other bindings are ignored."
  [query & data-sources]
  (map #(d/touch
         (d/entity
          (d/db (:db-conn tt-config/system))
          (first %)))
       (apply d/q query (d/db (:db-conn tt-config/system)) data-sources)))

(defn ptouch-that
  "Example: (ptouch-that '[:find ?e :where [?e :user/username]])"
  [query & data-sources]
  (pprint (apply touch-that query data-sources)))
