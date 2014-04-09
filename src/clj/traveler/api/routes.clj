(ns traveler.api.routes
  (:require [cheshire.core :refer [generate-string]]
            [liberator.core :refer [resource]]))


(def liberator-resources
  {:api          (resource :allowed-methods [:get]
                           :avaliable-media-types ["application/json"]
                           :handle-ok (generate-string {:api {:name "Traveler" :version "0.0.1"}}))

   :users/add    (resource :allowed-methods [:put]
                           :avaliable-media-types ["application/json"]
                           :put! (fn [ctx])
                           :handle-created (fn [ctx]))

   :users/search (resource :allowed-methods [:get]
                           :avaliable-media-types ["application/json"])

   :users/user   (resource :allowed-methods [:get]
                           :avaliable-media-types ["application/json"])})

(def api-routes
  [[:any "/" (str "Traveler API")]
   [:any "/users/add"    (:users/add liberator-resources)]
   [:any "/users/search" (:users/search liberator-resources)]
   [:any "/users/:id-sk" (:users/user liberator-resources)]])
