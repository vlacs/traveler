(ns traveler
  (:require [traveler.schema :as schema]))

(def partitions (hatch/schematode->partitions schema/schema))

(def valid-attrs (hatch/schematode->attrs schema/schema))

(def tx-entity! (partial hatch/tx-clean-entity! partitions valid-attrs))
