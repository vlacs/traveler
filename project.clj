(defproject org.vlacs/traveler "0.3.0-SNAPSHOT"
  :description "Library that controls storage and management of user data"
  :url "http://vlacs.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]

                 ^{:voom {:repo "https://github.com/vlacs/hatch"}}
                 [org.vlacs/hatch "0.2.1-20140619_010011-g63ac9fa" :exclusions [com.datomic/datomic-free]]
                 ^{:voom {:repo "https://github.com/vlacs/helmsman"}}
                 [org.vlacs/helmsman "0.2.6-20140619_005947-gb4bb7d4"]
                 ^{:voom {:repo "https://github.com/vlacs/flare"
                          :branch "dev"}}
                 [org.vlacs/flare "0.1.0-20140707_172541-g163ca2f"]

                 ^{:voom {:repo "https://github.com/vlacs/datomic-schematode" :branch "dev"}}
                 [datomic-schematode "0.1.3-RC1-20140624_195331-g210f6fa"]
                 [com.datomic/datomic-free "0.9.4766"]
                 [prismatic/schema "0.2.2"]]

  :resource-paths ["resources"]
  :pedantic? :warn ; :abort

  :plugins [[lein-cloverage "1.0.2"]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.4"]]}}

  :repl-options {:init-ns user})
