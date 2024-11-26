(ns cljs-tutorial.events
  (:require
   [re-frame.core :as re-frame]
   [cljs-tutorial.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :toggle-done
 (fn [todos [_ id]]
   (update-in todos [id :completed] not))
 )

(re-frame/reg-event-db
 :delete-todo
 (fn [todos [_ id]]
   (dissoc todos id)))

;; (re-frame/reg-event-db
;;  :delete-todo
;;  (fn [todos [_ id]]
;;    (println "Delete - Todo id: " todos)
;;    (let [todos db/default-db]
;;      (doseq [item todos]
;;        (println "Item ====" item)))
;;    (dissoc todos id)))