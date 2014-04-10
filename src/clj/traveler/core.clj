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
                        :handle-ok (fn [ctx] (tmpl/render (tmpl/view-system ctx))))

   :timber    (resource :allowed-methods [:get]
                        :available-media-types ["text/html" "text/css" "text/javascript"]
                        :handle-ok (fn [ctx] (t-utils/external-resource ctx "timber")))})

(def helmsman-definition
  [[:resources "/"]
   [:any "/timber/*" (:timber liberator-resources)]
   ^{:name "Traveler"
     :main-menu true}
   [:any "/" (:dashboard liberator-resources)
    ^{:name "Manage Users"}
    [:any "/users" (:users liberator-resources)]
    ^{:name "View System"}
    [:any "/system" (:system liberator-resources)]]
   (into [:context "/api"] api-routes)
   ;;middleware
   [wrap-trace :header :ui]
   [wrap-params]
   [wrap-host-urls]
   ])

(def app (compile-routes helmsman-definition))
