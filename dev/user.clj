(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer (javadoc)]
   [clojure.pprint :refer (pprint)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer (apropos dir doc find-doc pst source)]
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.test :as test]
   [datomic.api :as d]
   [traveler.system :as s]))

(defn stop
  "Stop the system from the REPL"
  []
  (s/stop!))

(defn go
  "Start the system from the REPL"
  []
  (s/start!))

(defn reset
  "Reset your environment from the REPL"
  []
  (s/stop!)
  (s/start!))
