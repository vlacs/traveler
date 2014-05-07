(ns traveler.api.routes
  (:require [cheshire.core :refer [generate-string]]
            [liberator.core :refer [resource]]
            [traveler.api.api :refer [handle-created]]
            [traveler.api.user :refer [get-user get-users search-user]]))

(defn liberator-resources
  "Map of liberator resources for the api endpoint"
  [db-conn]
  {:api          (resource :allowed-methods [:get]
                           :available-media-types ["application/json"]
                           :handle-ok (generate-string
                                       {:api
                                        {:name "Traveler"
                                         :version "0.1"}}))

   :user-search (resource :allowed-methods [:get]
                          :available-media-types ["application/json"]
                          :handle-ok (fn [ctx] (search-user ctx db-conn)))

   :user-user   (resource :allowed-methods [:get]
                          :available-media-types ["application/json"]
                          :handle-ok (fn [ctx] (get-user ctx db-conn)))

   :user-users  (resource :allowed-methods [:get]
                          :available-media-types ["appliction/json"]
                          :handle-ok (fn [ctx] (get-users ctx db-conn)))})

(defn api-routes
  "Helmsman routes for the api endpoint"
  [db-conn]
  [[:any "/" (:api (liberator-resources db-conn))]
   [:any "/user/search/:query" (:user-search (liberator-resources db-conn))]
   [:any "/user/:id-sk" (:user-user (liberator-resources db-conn))]
   [:any "/users/:per-page/:page" (:user-users (liberator-resources db-conn))]])
