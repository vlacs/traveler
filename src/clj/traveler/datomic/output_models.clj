(ns traveler.datomic.output-models)

(def user
  "Define template for user map"
  [:user/can-masquerade
   :user/istest
   :user/policies-assent-date
   :user/privilege
   :user/email
   :user/lastname
   :user/firstname
   :user/password
   :user/username
   :user/id-sk
   :user/istest])

(def user-no-pass
  "Return user model without password attr"
  (mapv #(keyword %) (remove #{:user/password} user)))
