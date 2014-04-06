(ns traveler.api.api
  (:require [cheshire.core :refer [generate-string]]
            [traveler.utils :as t-utils]
            [ring.util.codec :refer [url-encode]]))

(def response-key :response-status)

(defn gen-response
  ([] {response-key {:status "success"}})
  ([error] {response-key {:status "fail" :error error}}))

(defn handle-created [ctx]
  (let [status (get-in ctx [response-key :status])
        error  (get-in ctx [response-key :error])]
    (generate-string {:status status :error error})))

(defn post-has-error? [ctx]
  (if
    (empty? (get-in ctx [response-key :error]))
    {:location (t-utils/referer ctx)}
    {:location (str (t-utils/referer ctx) "?error=" (url-encode (get-in ctx [response-key :error])))}))
