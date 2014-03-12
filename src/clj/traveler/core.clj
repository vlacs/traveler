(ns traveler.core
  (:require [compojure.core :refer [routes]]
            [compojure.handler :refer [site]]
            [compojure.route :refer [not-found]]))


(def handler
  (routes
   (not-found "The resource you are looking for is not here!")))

(def app (-> (var handler)))
