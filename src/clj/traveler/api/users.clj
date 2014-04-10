(ns traveler.api.users
  (:require [traveler.api.api :refer [gen-response]]
            [traveler.utils :refer [get-param]]
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
;;  can_masquerade
(defn validate-add-user [user]
  (validate user
    [:id-sk present? "must be present"]
    [:id-sk digits? "must consist of digits"]
    [:username present? "must be present"]
    [:password present? "must be present"]
    [:privilege present? "must be present"]
    [:lastname present? "must be present"]
    [:firstname present? "must be present"]
    [:email present? "must be present"]
    [:email email-address? "must be a valid email address"]
    [:policies_assent_date present? "must be present"]))

(defn add-user [ctx]
  (gen-response "This endpoint is not implemented yet!"))
