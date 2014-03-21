(defproject traveler "0.1.0-SNAPSHOT"
  :description "Library that controls storage of user data"
  :url "http://vlacs.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :username "mgeorge@vlacs.org"
                                   :password "b64b82b3-6c34-4bcc-aea7-95cc02380f68"}}
  :dependencies [[compojure "1.1.6"]
                 [com.datomic/datomic-pro "0.9.4556"]
                 [datomic-schema "1.0.2"] ;dep for datomic-schematode
                 [enlive "1.1.5"]
                 [http-kit "2.1.16"]
                 [im.chit/gyr "0.3.1"]
                 [liberator "0.10.0"]
                 [org.clojure/clojure "1.5.1"]
                 [ring "1.2.1"]]
  :source-paths ["src/clj"
                 ".lein-git-deps/datomic-schematode/src/"]
  :resource-paths ["resources"]
  :plugins [[lein-cloverage "1.0.2"]
            [lein-git-deps "0.0.1-SNAPSHOT"]]
  :git-dependencies [["https://github.com/vlacs/datomic-schematode.git" "16d31b9"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})
