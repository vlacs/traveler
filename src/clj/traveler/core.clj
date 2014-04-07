(ns traveler.core
  (:require [compojure.core :refer [ANY defroutes]]
            [clojure.pprint :refer [pprint]]
            [helmsman :refer [compile-routes]]
            [liberator.core :refer [resource]]
            [liberator.dev :refer [wrap-trace]]
            [ring.middleware.params :refer [wrap-params]]
            [traveler.templates :as tmpl]
            [traveler.web.http :refer [wrap-host-urls]]
            [traveler.utils :as t-utils]))

(defroutes traveler-routes
  (ANY "/" [] (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (str "Hello Traveler"))))

(def liberator-resources
  {:dashboard (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx] (tmpl/render (tmpl/view-dashboard ctx))))

   :users     (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx] (do
                                               (pprint ctx)
                                               (tmpl/render (tmpl/view-users ctx)))))

   :system    (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (fn [ctx] (tmpl/render (tmpl/view-system ctx))))})

(def helmsman-definition
  [[:resources "/"]
   ^{:name "Traveler"
     :main-menu true}
   [:any "/" (:dashboard liberator-resources)
    ^{:name "Manage Users"}
    [:any "/users" (:users liberator-resources)]
    ^{:name "View System"}
    [:any "/system" (:system liberator-resources)]]

   ;;middleware
   [wrap-trace :header :ui]
   [wrap-params]
   [wrap-host-urls]
   ])

(def app (compile-routes helmsman-definition))
