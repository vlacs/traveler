(ns traveler.core
  (:require [helmsman.navigation :as h-nav]
            [liberator.core :refer [resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as response]
            [timber.core :as timber]
            [traveler.api.routes :refer [api-routes]]
            [traveler.templates :as tmpl]
            [traveler.utils :as t-utils]))

(defn dash-redirect
  "Redirect to the dashboard"
  [request]
  (response/redirect (t-utils/id-uri request :traveler/dashboard)))

(def liberator-resources
  "Core resources"
  {:dashboard (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx]
                                     (tmpl/render (tmpl/view-dashboard ctx))))

   :users     (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx]
                                     (tmpl/render (tmpl/view-users ctx))))

   :user      (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx]
                                     (tmpl/render (tmpl/view-user ctx))))

   :system    (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx]
                                     (tmpl/render (tmpl/view-system ctx))))})

(defn helmsman-definition
  "Main helmsman definition"
  [db-conn]
  [^{:id :traveler/resources}
   [:resources "/"]
   ^{:name "Traveler"
     :id :traveler/root
     :main-menu true}
   [:any "/" (:dashboard liberator-resources)]
   ^{:name "Dashboard" :id :traveler/dashboard}
   [:any "/dashboard" (:dashboard liberator-resources)]
   ^{:name "Manage Users" :id :traveler/users}
   [:any "/users" (:users liberator-resources)]
   ^{:name "Manage User" :id :traveler/user}
   [:any "/user/:id-sk" (:user liberator-resources)]
   ^{:name "View System" :id :traveler/system}
   [:any "/system" (:system liberator-resources)]
   (into [:context "/api"] (api-routes db-conn))
   ;;middleware
   [wrap-params]
   ])
