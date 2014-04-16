(ns traveler.auth.pass
  (:require [crypto.password.scrypt :as pass]
            [digest]))

(defn encrypt
  "scrypt encrypts string"
  [pass]
  (pass/encrypt pass))

(defn check
  "Checks string against scrypt encrypted string"
  [pass encrypted]
  (pass/check pass encrypted))

(defn md5?
  "Takes a string and checks weather it is an md5 or not"
  [in]
  (if (nil? (re-matches #"([a-fA-F\d]{32})" in))
    false
    true))

(defn hashed-md5
  "Takes a string and returns it's hashed (configurable) md5"
  [in]
  (let [hash "vlacsiscool"]
    (digest/md5 (str hash in))))

(defn md5
  "Takes a string and returns it's md5"
  [in]
  (digest/md5 in))
