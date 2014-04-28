(ns traveler.templates
  (:require [net.cgrand.enlive-html :as html]
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

(html/deftemplate ^{:doc "Load and transform the base html for the site"}
  base "templates/base.html"
  [{:keys [title nav-side content base-uri ng-app]}]
  [:html]          (html/set-attr :ng-app (or ng-app (str "")))
  [:head :title]   (maybe-content title)
  [:#nav]          (maybe-substitute nav-side)
  [:div.content]   (maybe-substitute content)
  [#{:head :body}] (replace-page-vars base-uri))

;;snippets

(html/defsnippet ^{:doc "Load html for sidebar and transform"}
  nav-side "templates/snippets/nav-side.html" [:nav#nav]
  [{:keys [brand nav-items]}]
  [:a.navbar-brand] (maybe-content brand))

;;pages

(html/defsnippet ^{:doc "Load html for dashboard page"}
  pg-dashboard "templates/pages/dashboard.html" [:div.content]
  [])

(html/defsnippet ^{:doc "Load html for users page"}
  pg-users "templates/pages/users.html" [:div.content]
  [])

(html/defsnippet ^{:dov "Load html for single user page"}
  pg-user "templates/pages/user.html" [:div.content]
  [ctx]
  [:div#init] (html/set-attr :ng-init (str "setUserId('" (t-utils/get-param ctx :id-sk) "')")))

(html/defsnippet ^{:doc "Load html for system page"}
  pg-system "templates/pages/system.html" [:div.content]
  [])

;;navbars
;;(defn nav-main []
;;  (nav {:brand "VLACS Dossier"}))

;;layouts

(defn layout-main
  "Main page layout type"
  [{:keys [title content ng-app ctx]}]
  (base {:title title
         ;;:nav-side (nav-side)
         :content content
         ;;:footer (footer)
         :base-uri (t-utils/base-uri ctx)
         :ng-app ng-app}))

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
