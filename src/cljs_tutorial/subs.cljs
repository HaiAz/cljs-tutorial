(ns cljs-tutorial.subs
  (:require
   [re-frame.core :as re-frame]
   [re-frame.alpha :refer [reg-sub subscribe reg]]))

(reg-sub
 :showing
 (fn [db _]
   (:showing db)))

(defn sorted-todos
  [db _]
  (:todos db))

(reg-sub :sorted-todos sorted-todos)

(reg-sub
 :todos
 (fn [query-v _]
   (subscribe [:sorted-todos]))
  (fn [sorted-todos query-v _]
   (vals sorted-todos)))

(re-frame/reg-sub
  ::todos
  (fn [db]
    (:todos db)))
