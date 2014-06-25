(ns traveler.system
  (:require [datomic-schematode :as dst]
            [datomic.api :as d]
            [helmsman :refer [compile-routes]]
            [immutant.web :as web]
            [timber.core :as timber]
            [traveler.core :as t-core]
            [traveler.schema :as t-schema]
            [traveler.test-data :as td]))

(def system {:datomic-uri "datomic:mem://traveler"})
(def datomic-uri (:datomic-uri system))

(defn load-schema!
  [system]
  [(dst/init-schematode-constraints! (:db-conn system))
   (dst/load-schema! (:db-conn system) t-schema/traveler-schema)])

(defn start-datomic!
  "Start the datomic database and transact the schema"
  [s]
  (d/create-database datomic-uri)
  (assoc s :db-conn
    (d/connect datomic-uri)))

(defn stop-datomic!
  "Shutdown and destroy the datomic database"
  [s]
  (d/delete-database datomic-uri)
  (dissoc s :db-conn))

(defn start!
  "Start the entire system"
  []
  (alter-var-root #'system start-datomic!)
  (println (str "Datomic started: " (:db-conn system)))
  (load-schema! system)
  (println (str "Datomic schema loaded!"))
  (d/transact (:db-conn system) (td/load-testdata))
  (println (str "Test data loaded!"))
  (println (str "Ready to set sail!")))

(defn stop!
  "Stop the entire system"
  []
  (alter-var-root #'system
                  (fn [s] (when s (-> s
                                      (stop-datomic!))))))

(defn standalone-helmsman-definition
  [s]
  (into (timber/helmsman-assets s) (t-core/helmsman-definition (:db-conn s))))

(defn app
  "Main app"
  [s]
  (compile-routes (standalone-helmsman-definition s)))

(defn init
  "Immutant dev init function"
  []
  (start!)
  (web/start (app system) :reload true))
