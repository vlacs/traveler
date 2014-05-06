(ns traveler.templates
  (:require [net.cgrand.enlive-html :as html]
            [helmsman.uri :as h-uri]
            [helmsman.navigation :as h-nav]
            [timber.core :as timber]
            [traveler.utils :as t-utils :refer [maybe-content
                                                maybe-substitute]]))

(defn transform-vars
  "Transform elements inside varmap"
  [varmap]
  (html/transform-content
   (html/replace-vars varmap)))

(defn page-transforms
  "Default page transformations"
  [base-uri]
  {:ASSET_PATH (str base-uri "/static")
   :BASE_PATH base-uri})

(defn replace-page-vars
  "Replace variables in the template"
  [base-uri]
  (transform-vars (page-transforms base-uri)))

(defn render
  "Take a template and render it to something liberator understands"
  [template]
  (reduce str template))

;; base template

(defn base-page
  [{:keys [page-name brand request user-name main-menu
           user-menu page-content extra-styles extra-scripts
           ng-app]}]
  (timber/base-page
   {:page-name page-name
    :brand brand
    :asset-uri-path (h-uri/relative-uri request (h-nav/id->uri-path request :timber/assets))
    :user-name      user-name
    :main-menu      main-menu
    :user-menu      user-menu
    :page-content   page-content
    :extra-styles   extra-styles
    :extra-scripts  extra-scripts
    :ng-app         ng-app
    }))

;;snippets

(defn extra-styles
  [request]
  (timber/extra-styles
   [(t-utils/resource-uri request "/static/css/traveler.css")]))

(defn extra-scripts
  [request]
  (timber/extra-scripts
   [(t-utils/resource-uri request "/static/js/traveler.js")]))

(defn nav-side
  "Generate the sidebar"
  [request]
  (timber/main-menu
   [{:menu-name "Dashboard"
     :menu-url  (h-uri/relative-uri-str request (h-nav/id->uri-path request :traveler/dashboard))}
    {:menu-name "Users"
     :menu-url  (h-uri/relative-uri-str request (h-nav/id->uri-path request :traveler/users))}
    {:menu-name "System"
     :menu-url  (h-uri/relative-uri-str request (h-nav/id->uri-path request :traveler/system))}]))

;;pages

(html/defsnippet ^{:doc "Load html for dashboard page"}
  pg-dashboard "templates/pages/dashboard.html" [:div#content]
  [])

(html/defsnippet ^{:doc "Load html for users page"}
  pg-users "templates/pages/users.html" [:div#content]
  [])

(html/defsnippet ^{:dov "Load html for single user page"}
  pg-user "templates/pages/user.html" [:div#content]
  [ctx]
  [:div#init] (html/set-attr :ng-init (str "setUserId('" (t-utils/get-param ctx :id-sk) "')")))

(html/defsnippet ^{:doc "Load html for system page"}
  pg-system "templates/pages/system.html" [:div#content]
  [])

;;layouts

(defn layout-main
  "Main page layout type"
  [{:keys [title content ng-app ctx]}]
  (let [request (:request ctx)]
    (base-page {:page-name title
                :brand "VLACS Traveler"
                :request request
                :main-menu (nav-side request)
                :page-content content
                :extra-styles (extra-styles request)
                :extra-scripts (extra-scripts request)
                :ng-app ng-app})))

;;views

(defn view-dashboard
  "Dashboard view"
  [ctx]
  (layout-main {:title "VLACS Traveler - Dashboard"
                :content (pg-dashboard)
                :ctx ctx}))

(defn view-users
  "Users view"
  [ctx]
  (layout-main {:title "VLACS Traveler - Users"
                :content (pg-users)
                :ng-app "traveler"
                :ctx ctx}))

(defn view-user
  "Single user view"
  [ctx]
  (layout-main {:title "VLACS Traveler - User"
                :content (pg-user ctx)
                :ng-app "traveler"
                :ctx ctx}))

(defn view-system
  "System view"
  [ctx]
  (layout-main {:title "VLACS Traveler - System"
                :content (pg-system)
                :ctx ctx}))
