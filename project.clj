(defproject org.vlacs/traveler "0.2.12"
  :description "Library that controls storage and management of user data"
  :url "http://vlacs.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2173"]

                 ^{:voom {:repo "https://github.com/vlacs/hatch"}}
                 [org.vlacs/hatch "0.1.2"]
                 ^{:voom {:repo "https://github.com/vlacs/helmsman"}}
                 [org.vlacs/helmsman "0.2.6" :exclusions [org.eclipse.jetty.orbit/javax.servlet]]
                 ^{:voom {:repo "https://github.com/vlacs/timber"}}
                 [org.vlacs/timber "0.1.7"]

                 [cheshire "5.3.1"]
                 [com.datomic/datomic-free "0.9.4766"
                  :exclusions [org.jboss.logging/jboss-logging org.jgroups/jgroups]]
                 [crypto-password "0.1.3"]
                 [datomic-schematode "0.1.0-RC3"]
                 [digest "1.4.4"]
                 [enlive "1.1.5"]
                 [im.chit/gyr "0.3.1"]
                 [inflections "0.9.6" :exclusions [commons-codec]]
                 [liberator "0.10.0" :exclusions [hiccup]]
                 [org.immutant/immutant "1.1.1"]
                 [ring/ring-core "1.2.2"]
                 [valip "0.2.0"]]

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]

  :pedantic? :error

  :immutant {:init traveler.system/init
             :resolve-dependencies true
             :context-path "/"}

  :plugins [[lein-cloverage "1.0.2"]
            [lein-cljsbuild "1.0.2"]
            [lein-immutant "1.2.1"]]

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
