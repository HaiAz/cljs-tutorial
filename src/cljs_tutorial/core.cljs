(ns cljs-tutorial.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [re-frame.alpha :as rf :refer [dispatch dispatch-sync]]
   [cljs-tutorial.events :as events]
   [cljs-tutorial.views :as views]
   [cljs-tutorial.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (println "after load mount-root")
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/todo-app] root-el)))

(defn init []
  (println "Initial DB =====")
  (dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
