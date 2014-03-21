(ns traveler.core
  (:require [compojure.core :refer [ANY defroutes routes]]
            [compojure.handler :refer [site]]
            [compojure.route :refer [not-found]]
            [liberator.core :refer [resource]]
            [liberator.dev :refer [wrap-trace]]
            [traveler.web.http :refer [ignore-trailing-slash wrap-host-urls]]))

(defroutes traveler-routes
  (ANY "/" [] (resource :allowed-methods [:get]
                        :available-media-types ["text/html"]
                        :handle-ok (str "Hello Traveler"))))

(def handler
  (routes
   (site traveler-routes)
   (not-found "The resource you are looking for is not here!")))

(def app (-> (var handler)
             (wrap-trace :header :ui)
             (wrap-host-urls)
             (ignore-trailing-slash)))
