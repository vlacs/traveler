(ns traveler.schema
  (:require [hatch]))

(def schema
  "Main traveler datomic schema"
  [{:namespace :user
    :attrs [[:id-sk :string :db.unique/identity]
            [:username :string :db.unique/identity]
            [:password :string]
            [:privilege :string :indexed]
            [:lastname :string]
            [:firstname :string]
            [:email :string]
            [:istest :boolean]
            [:can-masquerade :boolean]]}])

(defn attribute-names
  "Returns the namespaced attributes of an entity type. One argument form
  assumes that traveler's schema is being used, the two argument varient
  takes in schema and the entity namespace."
  ([ns-key]
   (attribute-names schema ns-key))
  ([schema ns-key]
   (when-let [ns-def (some #(when (= ns-key (:namespace %)) %) schema)]
     (map (partial hatch/slam ns-key) (map first (:attrs ns-def))))))
