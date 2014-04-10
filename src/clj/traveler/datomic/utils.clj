(ns traveler.datomic.utils
  (:require [cheshire.core :refer [generate-string]]
            [datomic.api :as d]
            [traveler.system :as s]))

(defn find-ents
  [attr match]
  (map #(d/entity (d/db @(:db s/system)) (first %))
       (d/q '[:find ?e
              :in $ ?attr ?matcher
              :where
              [?e ?attr ?ln]
              [(re-find ?matcher ?ln)]]
            (d/db @(:db s/system))
            attr (re-pattern (str match ".*")))))

(defn ents
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

(defn ent->map [convert-data entity]
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
  [attr match output-model]
  (let [entities (find-ents attr match)]
    (map (fn find-ents->json- [e]
           (generate-string (ent->map output-model e)))
         entities)))

(defn ents->json
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
