(ns traveler.system
  (:require [datomic-schematode.core :as ds-core]
            [datomic.api :as d]
            [traveler.schema :as t-schema]
            [traveler.state :as state]
            [traveler.test-data :as td]))

(def system {:datomic-uri "datomic:mem://traveler"})

(defn load-schema!
  []
  [(ds-core/init-schematode-constraints! state/db)
   (ds-core/load-schema! state/db t-schema/traveler-schema)])

(defn start-datomic!
  "Start the datomic database and transact the schema"
  [system]
  (d/create-database (:datomic-uri system))
  (state/set-db-var! (d/connect (:datomic-uri system))))

(defn stop-datomic!
  "Shutdown and destroy the datomic database"
  [system]
  (state/detach-db-var!)
  (d/delete-database (:datomic-uri system))
  system)

(defn start!
  "Start the entire system"
  []
  (start-datomic! system)
  (println (str "Datomic started: " state/db))
  (load-schema!)
  (println (str "Datomic schema loaded!"))
  (d/transact state/db (td/load-testdata))
  (println (str "Test data loaded!"))
  (println (str "Ready to set sail!")))

(defn stop!
  "Stop the entire system"
  []
  (stop-datomic!))
