(ns app.core
  (:require [app.views :as view]
            [reagent.dom.client :as rdc]
            [re-frame.core :as rf]))

(defn ui
  []
  [:div
   (view/message "hehe")])

(defonce root-container
  (rdc/create-root (js/document.getElementById "app")))

(defn mount-ui
  []
  (rdc/render root-container [ui]))

(defn ^:dev/after-load clear-cache-and-render!
  []
  (rf/clear-subscription-cache!)
  (mount-ui))

(defn main []
  (rf/dispatch-sync [:initialize])
  (mount-ui))