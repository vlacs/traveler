(ns traveler.state)

(def db nil)

(defn set-db-var!
 "Set db to the current datomic connection"
 [database]
 (alter-var-root #'db (constantly database)))

(defn detach-db-var! [] (alter-var-root #'db (constantly nil)))
