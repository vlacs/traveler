(ns traveler.web.http
  (:require [traveler.utils :refer [chop]]))

(defn get-nginx-scheme
  "Get the correct scheme using Nginx as a reverse proxy"
  [req]
  (let [headers (:headers req)]
    (keyword (get headers "x-forwarded-proto" (get req :scheme)))))

;;wrap-host-urls and related functions created by: https://github.com/jrdoanes
;;modified for nginx by: https://github.com/migeorge

(def default-ports
  {:http 80
   :https 443})

(defn cons-url
  ([protocol hostname]
   (cons-url protocol hostname nil ""))
  ([protocol hostname port]
   (cons-url protocol hostname port ""))
  ([protocol hostname port uri]
   (str
     (name protocol) "://" hostname
     (if (and
           (not= (get default-ports protocol) port)
           (not= port nil))
       (str ":" port) "")
     uri)))

(defn wrap-host-urls
  "This adds string representations of the path to the root of the
  web server and another that represents the current URL."
  [handler & [opts]]
  (fn wrap-host-urls-middleware
    [req]
    (let [url-fn (partial cons-url
                          (get-nginx-scheme req)
                          (get req :server-name)
                          (get req :server-port))]
      (-> req
          (assoc :base-url (url-fn ""))
          (assoc :current-url (url-fn (:uri req)))
          (handler)))))

;; URI Rewriting to remove trailing /

(defn with-uri-rewrite
  "Rewrites a request uri with the result of calling f with the
   request's original uri.  If f returns nil the handler is not called."
  [handler f]
  (fn [request]
    (let [uri (:uri request)
          rewrite (f uri)]
      (if rewrite
        (handler (assoc request :uri rewrite))
        nil))))

(defn- uri-snip-slash
  "Removes a trailing slash from all uris except \"/\"."
  [uri]
  (if (and (not (= "/" uri))
           (.endsWith uri "/"))
    (chop uri)
    uri))

(defn ignore-trailing-slash
  "Makes routes match regardless of whether or not a uri ends in a slash."
  [handler]
  (with-uri-rewrite handler uri-snip-slash))
