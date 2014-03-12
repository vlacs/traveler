(ns traveler.system
  (:require [org.httpkit.server :refer :all]
            [datomic.api :as d]
            [datomic-schematode.core :as ds-core]
            [traveler.conf :as t-conf]
            [traveler.core :as t-core]
            [traveler.schema :as t-schema]))

(def conf
  t-conf/load-config)

(def system
  {:web nil
   :db  nil})

(defn start-http []
  (alter-var-root #'system (fn [s]
                             (update-in s [:web] (run-server t-core/app {:port (get-in conf [:web :port])})))))

(defn stop-http []
  (when-not (nil? (:web system))
    ((:web system) :timeout 100)
    (alter-var-root #'system (fn [s] (update-in s [:web] (fn [_] nil))))))

(defn start-datomic [])

(defn start []
  (start-http)
  (start-datomic))

(defn stop []
  (stop-http))

(defn -main [&args]
  (start))
