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
                ))

(def default-db
  {:name "re-frame"
   :todos
   [{:id 1 :title "Buy groceries" :done false}
    {:id 2 :title "Walk the dog" :done true}
    {:id 3 :title "Finish the report" :done false}]})
