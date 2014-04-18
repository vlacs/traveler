(ns traveler.api.users
  (:require [traveler.api.api :refer [gen-response]]
            [traveler.datomic.utils :refer [user->db]]
            [traveler.utils :refer [get-param]]
            [clojure.walk :refer [keywordize-keys]]
            [valip.core :refer [validate]]
            [valip.predicates :refer [digits? email-address? present?]]))

;; Required Fields
;;  id-sk (unique)
;;  username (unique)
;;  password
;;  privilege
;;  lastname
;;  firstname
;;  email
;;  policies_assent_date
;;
;; Optional Fields (default to false)
;;  istest
;;  can-masquerade
(defn validate-add-user
  "Validate user map"
  [user]
  (validate user
    [:id-sk present? "must be present"]
    [:id-sk digits? "must consist of digits"]
    [:username present? "must be present"]
    [:password present? "must be present"]
    [:privilege present? "must be present"]
    [:lastname present? "must be present"]
    [:firstname present? "must be present"]
    [:email present? "must be present"]
    [:policies-assent-date present? "must be present"]))

(defn add-user
  "Public facing add-user endpoint"
  [ctx]
  (let [user (keywordize-keys (get-in ctx [:request :params]))]
    (if (nil? (validate-add-user user))
      (do
        (user->db user)
        (gen-response))
      (gen-response (validate-add-user user)))))
