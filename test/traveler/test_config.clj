(ns traveler.test-config
  (:require [datomic.api :as d]
            [datomic-schematode :as dst]
            [traveler.schema :as schema]
            [traveler]))

(def system {:datomic-uri "datomic:mem://traveler-test"})
(def datomic-uri (:datomic-uri system))

(defn start-datomic! [system]
  (d/create-database datomic-uri)
  (assoc system :db-conn
         (d/connect datomic-uri)))

(defn load-schema! [system]
  [(dst/init-schematode-constraints! (:db-conn system))
   (dst/load-schema! (:db-conn system) schema/schema)])

(defn stop-datomic! [system]
  (dissoc system :db-conn)
  (d/delete-database datomic-uri)
  system)

(defn start!
  "Starts the current development system."
  [& options]
  (alter-var-root #'system start-datomic!)
  (load-schema! system))

(defn stop!
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
                  (fn [s] (when s (-> s
                                      (stop-datomic!))))))

(defn testing-fixture [f]
  (start!)
  (f)
  (stop!))
