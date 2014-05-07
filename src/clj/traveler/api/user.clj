(ns traveler.api.user
  (:require [traveler.api.api :refer [gen-response]]
            [traveler.datomic.output-models :refer [user-no-pass]]
            [traveler.datomic.utils :refer [user->db ent->json
                                            ents->json search]]
            [traveler.utils :refer [get-param]]
            [cheshire.core :refer [generate-string]]
            [clojure.pprint :refer [pprint]]
            [clojure.walk :refer [keywordize-keys]]
            [valip.core :refer [validate]]
            [valip.predicates :refer [digits? email-address? present?]]))

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
  "Gangway add-user endpoint"
  [user db]
  (if (nil? (validate-add-user user))
    (do
      (user->db db user)
      :success)
    (validate-add-user user)))

(defn get-users
  "Public facing JSON endpoint to retrieve all users"
  [ctx db]
  (let [per-page (read-string (get-in ctx [:request :route-params :per-page]))
        cur-page (read-string (get-in ctx [:request :route-params :page]))
        offset   (* per-page (dec cur-page))]
    (ents->json db :user/username user-no-pass per-page offset)))

(defn get-user
  "Public facing JSON endpoint to retrieve a single user"
  [ctx db]
  (let [id-sk (get-in ctx [:request :route-params :id-sk])
        res   (ent->json db :user/id-sk id-sk user-no-pass)]
    res))

(defn search-user
  "Public facing JSON endpoint to search for users"
  [ctx db]
  (let [query (get-in ctx [:request :route-params :query])]
    (search db [:user/username :user/firstname :user/lastname :user/email] (str query) user-no-pass)))
