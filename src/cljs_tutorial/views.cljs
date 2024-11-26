;; (ns cljs-tutorial.views
;;   (:require
;;    [re-frame.core :as re-frame]
;;    [cljs-tutorial.subs :as subs]))

(ns cljs-tutorial.views
  (:require
   [re-frame.core :as re-frame]
   [re-frame.alpha :refer [subscribe dispatch sub]]
   [reagent.core  :as reagent]
   [cljs-tutorial.subs :as subs]
   [clojure.string :as str]))

(defn todo-input [{:keys [title on-save on-stop]}]
  (let [val (reagent/atom title)
        stop #(do (reset! @val "") (when on-stop (on-stop)))
        save #(let [v (-> @val str str/trim)] (on-save v) (stop))]
    (fn [props]
      [:input (merge (dissoc props :on-save :on-stop :title)
                     {:type "text"
                      :value @val
                      :auto-focus true
                      :on-blur save
                      :on-change #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %) 13 (save) 27 (stop) nil)})])
    ))

(defn todo-item
  []
  (let [editing (reagent/atom false)]
    (fn [{:keys [id title completed]}]
      [:li {:class (str (when completed "completed ")
                        (when @editing "editing"))
            :style {:margin-bottom "20px"}}
       [:div.view
        [:input.toggle
         {:type "checkbox"
          :checked completed
          :on-change #(dispatch [:toggle-done id])}]
        [:label
         {:on-double-click #(reset! editing true)
          :style {:padding-right "20px"}}
         id " - " title " - " (str completed)]
        [:button.destroy
         {:on-click #(dispatch [:delete-todo id])}
         [:label "Delete"]]
        ]])))

(defn todos-list []
  (let [todos @(subscribe [::subs/todos])]
    [:ul
     (for [item todos]
       ^{:key (:id item)} [todo-item item])]))

(defn todo-app []
  (let [name @(subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " name]
     (todos-list)
     ]))
