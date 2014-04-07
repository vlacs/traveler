(ns traveler.schema)

(def traveler-schema
  [[:user {:attrs [[:id-sk :string :db.unique/identity]
                   [:username :string :db.unique/identity]
                   [:password :string]
                   [:privilege :string :indexed]
                   [:lastname :string]
                   [:firstname :string]
                   [:email :string]
                   [:istest :boolean]
                   [:can_masquerade :boolean]]}]])
