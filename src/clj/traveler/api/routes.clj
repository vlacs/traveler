(ns traveler.api.routes
  (:require [cheshire.core :refer [generate-string]]
            [liberator.core :refer [resource]]
            [traveler.api.api :refer [handle-created]]
            [traveler.api.user :refer [add-user get-user
                                       get-users search-user]]))

(def liberator-resources
  "Map of liberator resources for the api endpoint"
  {:api          (resource :allowed-methods [:get]
                           :available-media-types ["application/json"]
                           :handle-ok (generate-string
                                       {:api
                                        {:name "Traveler"
                                         :version "0.1"}}))

   :user-add    (resource :allowed-methods [:put]
                          :available-media-types ["application/json"]
                          :put! (fn [ctx] (add-user ctx))
                          :handle-created (fn [ctx] (handle-created ctx)))

   :user-search (resource :allowed-methods [:get]
                          :available-media-types ["application/json"]
                          :handle-ok (fn [ctx] (search-user ctx)))

   :user-user   (resource :allowed-methods [:get]
                          :available-media-types ["application/json"]
                          :handle-ok (fn [ctx] (get-user ctx)))

   :user-users  (resource :allowed-methods [:get]
                          :available-media-types ["appliction/json"]
                          :handle-ok (fn [ctx] (get-users ctx)))})

(def api-routes
  "Helmsman routes for the api endpoint"
  [[:any "/" (:api liberator-resources)]
   [:any "/user/add"    (:user-add liberator-resources)]
   [:any "/user/search/:query" (:user-search liberator-resources)]
   [:any "/user/:id-sk" (:user-user liberator-resources)]
   [:any "/users/:per-page/:page" (:user-users liberator-resources)]])
