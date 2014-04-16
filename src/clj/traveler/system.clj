(ns traveler.system
  (:require [datomic-schematode.core :as ds-core]
            [datomic.api :as d]
            [org.httpkit.server :refer :all]
            [traveler.conf :as t-conf]
            [traveler.core :as t-core]
            [traveler.schema :as t-schema]))

(defn conf
  "Load conf on the fly"
  []
  (t-conf/load-config))

(def system
  "Store the application state here"
  {:web (atom nil) :db (atom nil)})

(defn start-http
  "Start the web server"
  []
  (reset! (:web system)
          (run-server t-core/app {:port (get-in (conf) [:web :port])})))

(defn stop-http
  "Stop the web server"
  []
  (when-not (nil? @(:web system))
    (@(:web system) :timeout 100)
    (reset! (:web system) nil)))

(defn start-datomic
  "Start the datomic database and transact the schema"
  []
  (if (= (get-in (conf) [:db :type]) "datomic")
    (let [d-ss (get-in (conf) [:db :storage-service])
          d-name (get-in (conf) [:db :db-name])
          d-uri (str "datomic:" d-ss "://" d-name)]
    (d/create-database d-uri)
    (ds-core/load-schema! (d/connect d-uri) t-schema/traveler-schema)
    (reset! (:db system) (d/connect d-uri)))))

(defn stop-datomic
  "Shutdown and destroy the datomic database"
  []
  (let [d-ss (get-in (conf) [:db :storage-service])
        d-name (get-in (conf) [:db :db-name])
        d-uri (str "datomic:" d-ss "://" d-name)]
    (d/delete-database d-uri))
  (reset! (:db system) nil))

(defn start
  "Start the entire system"
  []
  (start-http)
  (start-datomic))

(defn stop
  "Stop the entire system"
  []
  (stop-http)
  (stop-datomic))

(defn -main
  "Unused main method (for uberjar)"
  [&args]
  (start))
