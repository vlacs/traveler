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
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [datomic.api :as d]
   [testdata.testdata :as td]
   [traveler.system :as s]))

(defn start
  []
  (s/start)
  (d/transact @(:db s/system) (td/load-testdata))
  :ready)

(defn stop
  []
  (s/stop)
  :stopped)

(defn go
  []
  (start)
  :ready)

(defn reset
  []
  (stop)
  (refresh :after 'user/go))
