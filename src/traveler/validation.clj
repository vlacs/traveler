(ns traveler.validation
  (:require [schema.core :as s]))

(def validations
  {:user {:id-sk                    s/Str
          :id-sk-origin             s/Keyword
          :username                 s/Str
          :password                 s/Str
          :privilege                s/Str
          :lastname                 s/Str
          :firstname                s/Str
          :email                    s/Str
          :istest                   s/Bool
          :can-masquerade           s/Bool}})
