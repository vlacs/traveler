(ns traveler.core
  (:require [helmsman :refer [compile-routes]]
            [immutant.web :as web]
            [liberator.core :refer [resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as response]
            [timber.core :as timber]
            [traveler.api.routes :refer [api-routes]]
            [traveler.system :as s]
            [traveler.templates :as tmpl]
            [traveler.utils :as t-utils]))

(defn dash-redirect
  "Quick hack to redirect to dashboard"
  [_]
  (response/redirect "dashboard"))

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

(def helmsman-definition
  "Main helmsman definition"
  [[:resources "/"]
   (first timber/helmsman-assets)
   (second timber/helmsman-assets)
   ^{:name "Traveler"
     :main-menu true}
   [:any "/" dash-redirect
    ^{:name "Dashboard"}
    [:any "/dashboard" (:dashboard liberator-resources)]
    ^{:name "Manage Users"}
    [:any "/users" (:users liberator-resources)]
    ^{:name "Manage User"}
    [:any "/user/:id-sk" (:user liberator-resources)]
    ^{:name "View System"}
    [:any "/system" (:system liberator-resources)]]
   (into [:context "/api"] api-routes)
   ;;middleware
   [wrap-params]
   ])

(def app
  "Main app"
  (compile-routes helmsman-definition))

(defn init
  "Immutant dev init function"
  []
  (s/start!)
  (web/start app :reload true))
