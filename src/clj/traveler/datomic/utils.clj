(ns traveler.datomic.utils
  (:require [cheshire.core :refer [generate-string]]
            [datomic.api :as d]
            [traveler.system :as s]
            [traveler.datomic.output-models :refer :all]))

(defn find-ents
  "Find an entity using a case-insensitive regex match"
  [attr match]
  (map #(d/entity (d/db @(:db s/system)) (first %))
       (d/q '[:find ?e
              :in $ ?attr ?matcher
              :where
              [?e ?attr ?ln]
              [(re-find ?matcher ?ln)]]
            (d/db @(:db s/system))
            attr (re-pattern (str "(?i:.*" match ".*)")))))

(defn ents
  "Return all entities that have specific attribute
   with option to limit or limit and offset them"
  ([attr]
   (map #(d/entity (d/db @(:db s/system)) (first %))
        (sort (d/q '[:find ?e
                     :in $ ?a
                     :where [?e ?a]]
                   (d/db @(:db s/system))
                   attr))))
  ([attr limit]
   (map #(d/entity (d/db @(:db s/system)) (first %))
        (take limit (sort (d/q '[:find ?e
                                 :in $ ?a
                                 :where [?e ?a]]
                               (d/db @(:db s/system))
                               attr)))))
  ([attr limit offset]
   (map #(d/entity (d/db @(:db s/system)) (first %))
        (take limit (drop offset (sort (d/q '[:find ?e
                                              :in $ ?a
                                              :where [?e ?a]]
                                            (d/db @(:db s/system))
                                            attr)))))))

(defn ent->map
  "Convert an entity to a map using an output-model"
  [convert-data entity]
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
          convert-data))

(defn find-ents->json
  "Find entities by attribute and convert to
   json using an output-model"
  [attr match output-model]
  (let [entities (find-ents attr match)]
    (map (fn find-ents->json- [e]
           (generate-string (ent->map output-model e)))
         entities)))

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

(defn ent->db
  "Adds user map to datomic database"
  [ent-type ent-map]
  (let [ent (atom {})]
    (doseq [[k v] ent-map]
      (swap! ent assoc (keyword (str ent-type "/" (name k))) v))
    (swap! ent merge {:db/id (d/tempid :db.part/user)})
    (d/transact @(:db s/system) (vector @ent))))
