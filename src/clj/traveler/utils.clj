(ns traveler.utils
  (:require [clojure.string :refer [split]]
            [helmsman.uri :as h-uri])
  (:import (java.io File)))

(defmacro maybe-substitute
  "https://github.com/swannodette/enlive-tutorial/"
  ([expr] `(if-let [x# ~expr] (html/substitute x#) identity))
  ([expr & exprs] `(maybe-substitute (or ~expr ~@exprs))))

(defmacro maybe-content
  "https://github.com/swannodette/enlive-tutorial/"
  ([expr] `(if-let [x# ~expr] (html/content x#) identity))
  ([expr & exprs] `(maybe-content (or ~expr ~@exprs))))

(defn file-exists?
  [path]
  (if (.isFile (File. path))
    true
    false))

(defn throw-file-missing
  [path]
  (throw (ex-info
          (format "File missing at (%s)" path)
          {:cause :file-missing :file-path path})))

(defn referer [ctx]
  (str (first (split (get-in ctx [:request :headers "referer"]) #"\?"))))

(defn base-uri [ctx]
  (h-uri/assemble (get-in ctx [:request :helmsman :uri-path])))

(defn error [ctx]
  (let [error (get-in ctx [:request :query-params "error"])]
    (if (empty? error)
      false
      error)))

(defn chop
  "Removes the last character of string."
  [s]
  (subs s 0 (dec (count s))))
