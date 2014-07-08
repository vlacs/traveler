(ns traveler
  (:require [traveler.schema :as schema]
            [hatch]))

(def partitions (hatch/schematode->partitions schema/schema))

(def valid-attrs (hatch/schematode->attrs schema/schema))

(def tx-entity! (partial hatch/tx-clean-entity! partitions valid-attrs))

(defn user-in [db-conn user]
  (tx-entity! db-conn :user (hatch/slam-all user :user)))   ; TODO: consider having gangway do a slam-all
