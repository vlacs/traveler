(ns traveler.datomic.utils
  (:require [cheshire.core :refer [generate-string]]
            [clojure.string :refer [split]]
            [datomic.api :as d]
            [hatch]
            [inflections.core :refer [plural]]
            [traveler.conf :as t-conf]
            [traveler.schema :as t-schema]
            [traveler.datomic.output-models :refer :all]))

(defn conf []
  (t-conf/load-config))

(defn db []
  (d/connect (get-in (conf) [:db :uri])))

(def tx-entity! (partial hatch/tx-clean-entity! t-schema/partitions t-schema/valid-attrs))

(defn strip-namespace
  "Strip the datomic namespace from keys in a map"
  [in]
  (reduce conj {}
        (map #(vector (keyword (name (first %))) (second %)) in)))

(defn get-namespace
  "Get the namespace of an attribute"
  [in]
  (first (split (apply str (rest (str in))) #"/")))

(defn ent
  "Get an entity by attribute value"
  [attr value]
  (map #(d/entity (d/db (db)) (first %))
       (d/q '[:find ?e
              :in $ ?attr-in ?attr
              :where [?e ?attr-in ?attr]]
            (d/db (db))
            attr value)))

(defn ents
  "Return all entities that have specific attribute
  with option to limit or limit and offset them"
  ([attr]
   (map #(d/entity (d/db (db)) (first %))
        (sort (d/q '[:find ?e
                     :in $ ?a
                     :where [?e ?a]]
                   (d/db (db))
                   attr))))
  ([attr limit]
   (map #(d/entity (d/db (db)) (first %))
        (take limit (sort (d/q '[:find ?e
                                 :in $ ?a
                                 :where [?e ?a]]
                               (d/db (db))
                               attr)))))
  ([attr limit offset]
   (map #(d/entity (d/db (db)) (first %))
        (take limit (drop offset (sort (d/q '[:find ?e
                                              :in $ ?a
                                              :where [?e ?a]]
                                            (d/db (db))
                                            attr)))))))

(defn find-ents
  "Find an entity using a case-insensitive regex match"
  [attr match]
  (map #(d/entity (d/db (db)) (first %))
       (d/q '[:find ?e
              :in $ ?attr ?matcher
              :where
              [?e ?attr ?ln]
              [(re-find ?matcher ?ln)]]
            (d/db (db))
            attr (re-pattern (str "(?i:.*" match ".*)")))))

(defn user->db
  "Take pre-validated user map from liberator request
  and transact to database"
  [user]
  (tx-entity! (db) :user (hatch/slam-all user :user)))

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
  (generate-string {(keyword (plural (get-namespace attr)))
                    (let [entities (find-ents attr match)]
                      (into []
                            (flatten
                             (map (fn find-ents->json- [e]
                                    (vector (ent->map output-model e)))
                                  entities))))}))

(defn ent->json
  "Retrieve a single entity by attribute value in
  JSON format"
  [attr value output-model]
  (let [entity (first (ent attr value))]
    (generate-string (ent->map output-model entity))))

(defn ents->json
  "Convert all entities that have specific attribute
  to json using an output-model with option to limit
  or limit and offset them"
  ([attr output-model]
   (let [entities (ents attr)]
     (map (fn ents->json- [e]
            (generate-string (ent->map output-model e)))
          entities)))

  ([attr output-model limit]
   (let [entities (ents attr limit)]
     (map (fn ents->json- [e]
            (generate-string (ent->map output-model e)))
          entities)))

  ([attr output-model limit offset]
   (let [entities (ents attr limit offset)]
     (map (fn ents->json- [e]
            (generate-string (ent->map output-model e)))
          entities))))
