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

(def traveler-test-data
  [{:db/id #db/id[:db.part/traveler]
    :user/id-sk          "33100"
    :user/username       "tbowles"
    :user/password       "4c1f9568b6dbbaf35a7860aa24a62cf5"
    :user/privilege      "STUDENT"
    :user/lastname       "Bowles"
    :user/firstname      "Tammy"
    :user/email          "tbowles@vlacs.org"
    :user/istest         true
    :user/can_masquerade false}
   {:db/id #db/id[:db.part/traveler]
    :user/id-sk          "52333"
    :user/username       "bmccormack"
    :user/password       "ab97642a46684702a4ed14d77e6e1ce1"
    :user/privilege      "STUDENT"
    :user/lastname       "McCormack"
    :user/firstname      "Brianna"
    :user/email          "bmccormack@vlacs.org"
    :user/istest         true
    :user/can_masquerade false}
   {:db/id #db/id[:db.part/traveler]
    :user/id-sk          "16911"
    :user/username       "tthomas1"
    :user/password       "256ae3818836aae43fd962fa74818ec4"
    :user/privilege      "STUDENT"
    :user/lastname       "Thomas"
    :user/firstname      "Thalia"
    :user/email          "tthomas1@vlacs.org"
    :user/istest         true
    :user/can_masquerade false}
   {:db/id #db/id[:db.part/traveler]
    :user/id-sk          "33053"
    :user/username       "mfigella"
    :user/password       "97fb7dfac083b39d4d8af69bf408ee0d"
    :user/privilege      "STUDENT"
    :user/lastname       "Figella"
    :user/firstname      "Michelle"
    :user/email          "mfigella@vlacs.org"
    :user/istest         true
    :user/can_masquerade false}
   {:db/id #db/id[:db.part/traveler]
    :user/id-sk          "61365"
    :user/username       "jkatz"
    :user/password       "b25c1cea506d0f35e385f8e61e65d86a"
    :user/privilege      "STUDENT"
    :user/lastname       "Katz"
    :user/firstname      "Jason"
    :user/email          "jkatz@vlacs.org"
    :user/istest         true
    :user/can_masquerade false}])
