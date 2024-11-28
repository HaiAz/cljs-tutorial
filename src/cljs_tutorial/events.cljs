(ns cljs-tutorial.events
  (:require
   [re-frame.core :as re-frame]
   [cljs-tutorial.db :refer [default-db todos->local-store]]
   [re-frame.alpha :refer [reg-event-db reg-event-fx inject-cofx path after sub]]
   [cljs.spec.alpha :as s]
   ))

(defn check-and-throw
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :cljs-tutorial.db/db)))

(def ->local-store (after todos->local-store))

(def todo-interceptors [(path :todos) ->local-store]) 

(defn allocate-next-id
  [todos]
  ((fnil inc 0) (last (keys todos))))

(reg-event-fx
::initialize-db
   
  [(inject-cofx :local-store-todos)]
  (fn [{:keys [db local-store-todos]} _]
  {:db (assoc default-db :todos local-store-todos)}))

(reg-event-db
 :add-todo
 todo-interceptors
 (fn [todos [_ text]]
 (let [id (allocate-next-id todos)]
 (assoc todos id {:id id :title text :done false}))))

(reg-event-db
 :delete-todo
 todo-interceptors
 (fn [todos [_ id]]
   (println "k co j")
   (dissoc todos id)))

(reg-event-db
 :toggle-done
 todo-interceptors
 (fn [todos [_ id]]
   (update-in todos [id :done] not)))

(reg-event-db
 :save
 todo-interceptors
 (fn [todos [_ id title]]
   (assoc-in todos [id :title] title)))
