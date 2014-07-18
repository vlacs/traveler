(ns traveler
  (:require [traveler.schema]
            [hatch]
            [flare.event]))

(def partitions (hatch/schematode->partitions traveler.schema/schema))
(def valid-attrs (hatch/schematode->attrs traveler.schema/schema))
(def tx-entity! (partial hatch/tx-clean-entity! partitions valid-attrs))

(defn user-in [db-conn user]
  (tx-entity! db-conn :user (hatch/slam-all user :user)))   ; TODO: consider having gangway do a slam-all

(defn init!
  [system]
  (flare.event/register! (:db-conn system) :traveler :user
                         (traveler.schema/attribute-names :user))
  system)

(defn start!
  "Galleon start up fn that registers events with Flare."
  [system]
  system)
