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
    [:email email-address? "must be a valid email address"]
    [:policies-assent-date present? "must be present"]))

;;testing

#_
(def user {:id-sk "00392"
           :username "mgeorge"
           :password "kaj309jdfna0sd9flk4nas2"
           :privilege "ADMIN"
           :lastname "George"
           :firstname "Michael"
           :email "mgeorge@vlacs.org"
           :policies-assent-date "2001-03-02"
           })


;;end testing

(defn add-user
  "Public facing add-user endpoint"
  [ctx]
  (gen-response "This endpoint is not implemented yet!"))
