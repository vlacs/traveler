(ns traveler.datomic.utils
  (:require [cheshire.core :refer [generate-string]]
            [clojure.string :as string]
            [clojure.pprint :refer [pprint]]
            [datomic.api :as d]
            [hatch]
            [inflections.core :refer [plural]]
            [traveler.schema :as t-schema]
            [traveler.state :as t-state]))

(def tx-entity! (partial hatch/tx-clean-entity! t-schema/partitions t-schema/valid-attrs))

(defn strip-namespace
  "Strip the datomic namespace from keys in a map"
  [in]
  (reduce conj {}
          (map #(vector (keyword (string/replace (name (first %)) #"-" "_"))
                        (second %)) in)))

(defn get-namespace
  "Get the namespace of an attribute"
  [in]
  (first (string/split (apply str (rest (str in))) #"/")))

(defn ent
  "Get an entity by attribute value"
  [attr value]
  (map #(d/entity (d/db t-state/db) (first %))
       (d/q '[:find ?e
              :in $ ?attr-in ?attr
              :where [?e ?attr-in ?attr]]
            (d/db t-state/db)
            attr value)))

(defn count-ents
  "Count the number of entities that have attribute"
  [attr]
  (ffirst
   (d/q '[:find (count ?e)
          :in $ ?a
          :where [?e ?a]]
        (d/db t-state/db)
        attr)))

(defn ents
  "Return all entities that have specific attribute
  with option to limit or limit and offset them"
  ([attr]
   (map #(d/entity (d/db t-state/db) (first %))
        (sort (d/q '[:find ?e
                     :in $ ?a
                     :where [?e ?a]]
                   (d/db t-state/db)
                   attr))))
  ([attr limit]
   (map #(d/entity (d/db t-state/db) (first %))
        (take limit (sort (d/q '[:find ?e
                                 :in $ ?a
                                 :where [?e ?a]]
                               (d/db t-state/db)
                               attr)))))
  ([attr limit offset]
   (map #(d/entity (d/db t-state/db) (first %))
        (take limit (drop offset (sort (d/q '[:find ?e
                                              :in $ ?a
                                              :where [?e ?a]]
                                            (d/db t-state/db)
                                            attr)))))))

(defn find-ents
  "Find an entity using a case-insensitive regex match"
  [attr match]
  (map #(d/entity (d/db t-state/db) (first %))
       (d/q '[:find ?e
              :in $ ?attr ?matcher
              :where
              [?e ?attr ?ln]
              [(re-find ?matcher ?ln)]]
            (d/db t-state/db)
            attr (re-pattern (str "(?i:.*" match ".*)")))))

(defn user->db
  "Take pre-validated user map from liberator request
  and transact to database"
  [user]
  (tx-entity! t-state/db :user (hatch/slam-all user :user)))

(defn ent->map
  "Convert an entity to a map using an output-model"
  [convert-data entity]
  (strip-namespace
   (reduce (fn [m attr]
             (if (keyword? attr)
               (if-let [v (attr entity)]
                 (assoc m attr v)
                 m)
               (let [[attr conv-fn] attr]
                 (if-let [v (attr entity)]
                   (assoc m attr (conv-fn v))
                   m))))
           {}
           convert-data)))

(defn find-ents->json
  "Find entities by attribute and convert to
  json using an output-model"
  [attr match output-model]
  (let [entities (find-ents attr match)]
    (generate-string {(keyword (plural (get-namespace attr)))
                      (into []
                            (flatten
                             (map (fn find-ents->json- [e]
                                    (vector (ent->map output-model e)))
                                  entities)))})))

(defn ent->json
  "Retrieve a single entity by attribute value in
  JSON format"
  [attr value output-model]
  (let [entity (first (ent attr value))]
    (generate-string {(keyword (get-namespace attr))
                      (ent->map output-model entity)})))

(defn ents->json
  "Convert all entities that have specific attribute
  to json using an output-model with option to limit
  or limit and offset them"
  ([attr output-model]
   (let [entities (ents attr)]
     (generate-string {(keyword (plural (get-namespace attr)))
                       (flatten
                        (map (fn ents->json- [e]
                               (vector (ent->map output-model e)))
                             entities))
                       "count" (count-ents attr)})))

  ([attr output-model limit]
   (let [entities (ents attr limit)]
     (generate-string {(keyword (plural (get-namespace attr)))
                       (flatten
                        (map (fn ents->json- [e]
                               (vector (ent->map output-model e)))
                             entities))
                       "count" (count-ents attr)})))

  ([attr output-model limit offset]
   (let [entities (ents attr limit offset)]
     (generate-string {(keyword (plural (get-namespace attr)))
                       (flatten
                        (map (fn ents->json- [e]
                               (vector (ent->map output-model e)))
                             entities))
                       "count" (count-ents attr)}))))

(defn search
  "Loop through attributes and return unique set of
  entities converted to JSON using output model"
  [attrs match output-model]
  (generate-string {:results
                    (into []
                          (flatten
                           (map (fn search- [r]
                                  (vector (ent->map output-model r)))
                                (distinct
                                 (flatten
                                  (for [attr attrs]
                                    (find-ents attr match)))))))}))
