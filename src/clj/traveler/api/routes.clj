(ns traveler.api.routes
  (:require [liberator.core :refer [resource]]))

;;Need the following routes:
;;  /api/users/add
;;  /api/users/search
;;  /api/users/:id-sk

(def liberator-resources
  {:users/add    (resource)
   :users/search (resource)
   :users/user   (resource)})

(def api-routes
  [[:any "/api" (str "Traveler API")
    [:any "/users/add"    (:users/add liberator-resources)]
    [:any "/users/search" (:users/search liberator-resources)]
    [:any "/users/:id-sk" (:users/user liberator-resources)]]])
