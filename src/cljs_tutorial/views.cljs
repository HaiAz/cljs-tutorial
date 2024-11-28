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
        stop #(do (reset! val "") (when on-stop (on-stop)))
        save #(let [v (-> @val str str/trim)] (on-save v) (stop))]
    (fn [props]
      (println "val ===" @val)
      [:input (merge (dissoc props :on-save :on-stop :title)
                     {:type "text"
                      :value @val
                      :auto-focus true
                      :on-blur save
                      :on-change #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %) 13 (save) 27 (stop) nil)})])
    ))

(defn task-entry
  []
  [:header#header
   [:h1 "Todo List"]
   [todo-input
    {:id "new-todo"
     :placeholder "What needs to be done?"
     :on-save #(when (seq %)
                 (dispatch [:add-todo %]))}]])

(defn todo-item
  []
  (let [editing (reagent/atom false)]
    (fn [{:keys [id title done]}]
      [:li {:class (str (when done "completed ")
                        (when @editing "editing"))
            :style {:margin-bottom "20px"}}
       [:div.view
        [:input.toggle
         {:type "checkbox"
          :checked done
          :on-change #(dispatch [:toggle-done id])}]
        [:label
         {:on-double-click #(reset! editing true)
          :style {:padding-right "20px"}}
         id " - " title " - " (str done)]
        [:button.destroy
         {:on-click #(dispatch [:delete-todo id])}
         [:label "Delete"]]
        (when @editing
          [todo-input
           {:placeholder "Edit todo"
            :on-save #(if (seq %)
                        (dispatch [:save id %])
                        (println "Do nothing!"))
            :on-stop #(reset! editing false)}])
        ]])))

(defn todos-list []
  (let [visible-todos @(subscribe [:visible-todos])]
    [:ul
     (for [item visible-todos]
       ^{:key (:id item)} [todo-item item])]))

(defn todo-app []
    [:<>
     [task-entry]
     (when (seq @(subscribe [:todos])) [todos-list])
     ])
