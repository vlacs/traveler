(ns traveler.datomic.utils
  (:require [datomic.api :as d]
            [cheshire.core :refer :all]
            [traveler.system :as s]))

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
