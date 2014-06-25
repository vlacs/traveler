(ns traveler.schema
  (:require hatch))

(def traveler-schema
  "Main traveler datomic schema"
  [{:namespace :user
    :attrs [[:id-sk :string :db.unique/identity]
            [:username :string :db.unique/identity]
            [:password :string]
            [:privilege :string :indexed]
            [:lastname :string]
            [:firstname :string]
            [:email :string]
            [:policies-assent-date :string]
            [:istest :boolean]
            [:can-masquerade :boolean]]}])

(def partitions (hatch/schematode->partitions traveler-schema))

(def valid-attrs (hatch/schematode->attrs traveler-schema))
