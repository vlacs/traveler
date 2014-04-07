(ns traveler.datomic.output-models)

(def user
  [:user/privilege
   :user/email
   :user/lastname
   :user/firstname
   :user/password
   :user/username
   :user/id-sk])

(def user-no-pass
  [:user/privilege
   :user/email
   :user/lastname
   :user/firstname
   :user/username
   :user/id-sk])
