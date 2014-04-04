(defproject traveler "0.1.0-SNAPSHOT"
  :description "Library that controls storage of user data"
  :url "http://vlacs.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojurescript "0.0-2173"]
                 [compojure "1.1.6"]
                 [com.datomic/datomic-free "0.9.4556" :exclusions [commons-codec]]
                 [datomic-schematode "0.1.0-RC1"]
                 [enlive "1.1.5"]
                 [http-kit "2.1.16"]
                 [im.chit/gyr "0.3.1"]
                 [liberator "0.10.0" :exclusions [hiccup]]
                 [org.clojure/clojure "1.6.0"]
                 [ring "1.2.1"]]
  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]
  :pedantic? :error
  :plugins [[lein-cloverage "1.0.2"]
            [lein-cljsbuild "1.0.2"]]
  :cljsbuild {
    :builds [{:id "traveler-dev"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/static/js/traveler_dev.js"
                :optimizations :whitespace
                :pretty-print true}}
             {:id "traveler-prod"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/static/js/traveler.js"
                :externs ["resources/externs/angular-1.2.js"]
                :optimizations :advanced
                :pretty-print false}}]}
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]
                                  [com.datomic/datomic-free "0.9.4699"]]
                   :source-paths ["dev"]}})
