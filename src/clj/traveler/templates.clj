(ns traveler.templates
  (:require [net.cgrand.enlive-html :as html]
            [traveler.utils :as t-utils]
            [traveler.utils :refer [maybe-content maybe-substitute]]))

(defn replace-asset-path [base-url]
  (html/transform-content (html/replace-vars {:ASSET_PATH (str base-url "/static/")})))

(defn render [template]
  (reduce str template))

;; base template

(html/deftemplate base "templates/base.html"
  [{:keys [title nav-side content base-url ng-app]}]
  [:html]          (html/set-attr :ng-app (or ng-app (str "")))
  [:head :title]   (maybe-content title)
  [:#nav]          (maybe-substitute nav-side)
  [:div.content]   (maybe-substitute content)
  [#{:head :body}] (replace-asset-path base-url))

;;snippets

(html/defsnippet nav-side "templates/snippets/nav-side.html" [:nav#nav]
  [{:keys [brand nav-items]}]
  [:a.navbar-brand] (maybe-content brand))

;;pages

(html/defsnippet pg-dashboard "templates/pages/dashboard.html" [:div.content]
  [])

(html/defsnippet pg-users "templates/pages/users.html" [:div.content]
  [])

(html/defsnippet pg-system "templates/pages/system.html" [:div.content]
  [])

;;navbars
;;(defn nav-main []
;;  (nav {:brand "VLACS Dossier"}))

;;layouts

(defn layout-main [{:keys [title content ng-app ctx]}]
  (base {:title title
         ;;:nav-side (nav-side)
         :content content
         ;;:footer (footer)
         :base-url (t-utils/base-url ctx)
         :ng-app ng-app}))

;;views

(defn view-dashboard [ctx]
  (layout-main {:title "VLACS Traveler - Dashboard"
                :content (pg-dashboard)
                :ctx ctx}))

(defn view-users [ctx]
  (layout-main {:title "VLACS Traveler - Users"
                :content (pg-users)
                :ng-app "users-module"
                :ctx ctx}))

(defn view-system [ctx]
  (layout-main {:title "VLACS Traveler - System"
                :content (pg-system)
                :ctx ctx}))
