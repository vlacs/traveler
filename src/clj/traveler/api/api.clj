(ns traveler.api.api
  (:require [cheshire.core :refer [generate-string]]
            [ring.util.codec :refer [url-encode]]
            [traveler.utils :as t-utils]))

(def response-key
  "Default response key, used in get-response and handle-created"
  :response-status)

(defn gen-response
  "Generate a liberator response with status and error"
  ([] {response-key {:status "success"}})
  ([error]
   (if (nil? error)
     {response-key {:status "success"}}
     {response-key {:status "fail" :error error}})))

(defn handle-created
  "Get the response out of the context and return it"
  [ctx]
  (let [status (get-in ctx [response-key :status])
        error  (get-in ctx [response-key :error])]
    (generate-string {:status status :error error})))

(defn post-has-error?
  "Check if the post has an error, if it does redirect
   with the error appended to the url"
  [ctx]
  (if
    (empty? (get-in ctx [response-key :error]))
    {:location (t-utils/referer ctx)}
    {:location (str
                (t-utils/referer ctx)
                "?error="
                (url-encode
                 (get-in ctx [response-key :error])))}))
