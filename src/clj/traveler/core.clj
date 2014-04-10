(ns traveler.core
  (:require [helmsman :refer [compile-routes]]
            [liberator.core :refer [resource]]
            [liberator.dev :refer [wrap-trace]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as response]
            [traveler.api.routes :refer [api-routes]]
            [traveler.templates :as tmpl]
            [traveler.utils :as t-utils]))

(defn dash-redirect [_]
  (response/redirect "/dashboard"))

(def liberator-resources
  {:dashboard (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx] (tmpl/render (tmpl/view-dashboard ctx))))

   :users     (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx] (tmpl/render (tmpl/view-users ctx))))

   :system    (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx] (tmpl/render (tmpl/view-system ctx))))})

(def helmsman-definition
  [[:resources "/"]
   [:resources "timber" {:root "/timber"}]
   ^{:name "Traveler"
     :main-menu true}
   [:any "/" dash-redirect
    ^{:name "Dashboard"}
    [:any "/dashboard" (:dashboard liberator-resources)]
    ^{:name "Manage Users"}
    [:any "/users" (:users liberator-resources)]
    ^{:name "View System"}
    [:any "/system" (:system liberator-resources)]]
   (into [:context "/api"] api-routes)
   ;;middleware
   [wrap-params]
   [wrap-trace :header :ui]
   ])

(def app (compile-routes helmsman-definition))
