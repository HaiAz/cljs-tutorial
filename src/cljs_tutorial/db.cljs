(ns cljs-tutorial.db
  (:require [cljs.reader]
            [cljs.spec.alpha :as s]
            [re-frame.alpha :as re-frame]))

(s/def ::id int?)
(s/def ::title string?)
(s/def ::done boolean)
(s/def ::todo (s/keys :req-un [::id ::title ::done]))
(s/def ::todos (s/and
                (s/map-of ::id ::todo)
                #(instance? PersistentTreeMap %)
                ))

(s/def ::showing
  #{:all :active :done})

(s/def ::db (s/keys :req-un [::todos ::showing]))

(def default-db
  {:todos (sorted-map)
   :showing :all})

(def ls-key "todos-store")

(defn todos->local-store
  "Puts todos into localStorage"
  [todos]
  (.setItem js/localStorage ls-key (str todos)))

(re-frame/reg-cofx
 :local-store-todos
 (fn [cofx _]
   (assoc cofx :local-store-todos 
        	 (into (sorted-map)
                (some->> (.getItem js/localStorage ls-key)
                         (cljs.reader/read-string)
                         )))))