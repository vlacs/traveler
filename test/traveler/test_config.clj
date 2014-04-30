(ns traveler.test-config
  (:require [traveler.system :as s]))

(defn testing-fixture [f]
  (alter-var-root #'s/system {:datomic-uri "datomic:mem://traveler-test"})
  (s/start!)
  (f)
  (s/stop!))
