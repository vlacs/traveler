(ns traveler.schema)

(def traveler-schema
  [:user {:attrs [[:id-sk :string :indexed]
                  [:username :string :indexed]
                  [:password :string]
                  [:privilege :string :indexed]
                  [:lastname :string]
                  [:firstname :string]
                  [:email :string]
                  [:istest :boolean]
                  [:can_masquerade :boolean]]
          :part :traveler}])
