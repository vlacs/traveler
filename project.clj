(defproject org.vlacs/traveler "0.1.4"
  :description "Library that controls storage and management of user data"
  :url "http://vlacs.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [org.vlacs/helmsman "0.1.8"]
                 [vlacs/timber "0.1.0-SNAPSHOT"]
                 [cheshire "5.3.1"]
                 [com.datomic/datomic-free "0.9.4699" :exclusions [commons-codec]]
                 [crypto-password "0.1.3"]
                 [datomic-schematode "0.1.0-RC1"]
                 [digest "1.4.4"]
                 [enlive "1.1.5"]
                 [http-kit "2.1.16"]
                 [im.chit/gyr "0.3.1"]
                 [liberator "0.10.0" :exclusions [hiccup]]
                 [ring "1.2.1"]
                 [ring/ring-json "0.3.0"]
                 [valip "0.2.0"]]

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]

  :pedantic? :error

  :plugins [[lein-cloverage "1.0.2"]
            [lein-cljsbuild "1.0.2"]]

  :cljsbuild {:builds [{:id "traveler-dev"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/static/js/traveler_dev.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:id "traveler-prod"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/static/js/traveler.js"
                                   :externs ["resources/externs/angular-1.2.js"]
                                   :optimizations :advanced
                                   :pretty-print false}}]}

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})
