(ns traveler.system
  (:require [clojure.pprint :refer [pprint]]
            [org.httpkit.server :refer :all]
            [datomic.api :as d]
            [datomic-schematode.core :as ds-core]
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

(defn start-datomic []
  (if (= (get-in (conf) [:db :type]) "datomic")
    (let [d-ss (get-in (conf) [:db :storage-service])
          d-name (get-in (conf) [:db :db-name])
          d-uri (str "datomic:" d-ss "://" d-name)]
    (d/create-database d-uri)
    (ds-core/load-schema! (d/connect d-uri) t-schema/traveler-schema)
    (reset! (:db system) d-uri))))

(defn stop-datomic []
  (reset! (:db system) nil))

(defn start []
  (start-http)
  (start-datomic))


(defn stop []
  (stop-http)
  (stop-datomic))

(defn -main [&args]
  (start))
