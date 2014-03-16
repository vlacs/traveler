(ns traveler.system
  (:require [clojure.pprint :refer [pprint]]
            [org.httpkit.server :refer :all]
            [traveler.conf :as t-conf]
            [traveler.core :as t-core]
            [traveler.schema :as t-schema]))

(defn conf []
  (t-conf/load-config))

(def system
  {:web (atom nil) :db (atom nil)})

(defn start-http []
   (reset! (:web system) (run-server t-core/app {:port (get-in (conf) [:web :port])})))

(defn stop-http []
  (when-not (nil? @(:web system))
    (@(:web system) :timeout 100)
    (reset! (:web system) nil)))

(defn start-datomic [])

(defn start []
  (start-http)
  (pprint #'system))

(defn stop []
  (stop-http))

(defn -main [&args]
  (start))
