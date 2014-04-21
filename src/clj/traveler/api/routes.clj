(ns traveler.api.routes
  (:require [cheshire.core :refer [generate-string]]
            [liberator.core :refer [resource]]
            [traveler.api.api :refer [handle-created]]
            [traveler.api.user :refer [add-user get-user search-user]]))

(def liberator-resources
  "Map of liberator resources for the api endpoint"
  {:api          (resource :allowed-methods [:get]
                           :available-media-types ["application/json"]
                           :handle-ok (generate-string
                                       {:api
                                        {:name "Traveler"
                                         :version "0.1"}}))

   :users-add    (resource :allowed-methods [:put]
                           :available-media-types ["application/json"]
                           :put! (fn [ctx] (add-user ctx))
                           :handle-created (fn [ctx] (handle-created ctx)))

   :users-search (resource :allowed-methods [:get]
                           :available-media-types ["text/html"]
                           :handle-ok (fn [ctx] (str "this is a search")))

   :users-user   (resource :allowed-methods [:get]
                           :available-media-types ["application/json"]
                           :handle-ok (fn [ctx]))})

(def api-routes
  "Helmsman routes for the api endpoint"
  [[:any "/" (:api liberator-resources)]
   [:any "/users/add"    (:users-add liberator-resources)]
   [:any "/users/search/:query" (:users-search liberator-resources)]
   [:any "/users/:id-sk" (:users-user liberator-resources)]])
