(ns traveler.api.user
  (:require [traveler.api.api :refer [gen-response]]
            [traveler.datomic.output-models :refer [user-no-pass]]
            [traveler.datomic.utils :refer [user->db ent->json find-ents->json]]
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

;;TODO: Pull apart valip response on error and make the
;; error messages better.
(defn add-user
  "Public facing add-user endpoint"
  [ctx]
  (let [user (keywordize-keys (get-in ctx [:request :params]))]
    (if (nil? (validate-add-user user))
      (do
        (user->db user)
        (gen-response))
      (gen-response (validate-add-user user)))))

(defn get-user
  "Public facing JSON endpoint to retrieve a single user"
  [ctx]
  (let [id-sk (get-in ctx [:request :route-params :id-sk])
        res   (ent->json :user/id-sk id-sk user-no-pass)]
    res))

(defn search-user
  "Public facing JSON endpoint to search for users"
  [ctx]
  (let [query      (get-in ctx [:request :route-params :query])
        s-username (find-ents->json :user/username (str query) user-no-pass)]
    s-username))
