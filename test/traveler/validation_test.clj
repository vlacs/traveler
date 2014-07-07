(ns traveler.validation-test
  (:require [clojure.test :refer :all]
            [traveler.validation :as val]
            [schema.core :as s]))

(defn validator
  [validations entity-type data]
  (let [validation (entity-type validations)]
    (try
      (s/validate
       validation
       data)
      (catch Exception e (.getMessage e)))))
