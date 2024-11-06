(ns sk.handlers.home.model
  (:require [sk.models.crud :refer [Query
                                    Query!
                                    Update
                                    Insert-multi
                                    db
                                    dbr]]))

(defn get-active-carrera []
  (:id (first (Query db "select id from carrera where activa='S'"))))

(def lector-sql
  "
  select
  pos_items.OID as lector_id,
  pos_items.checkin_dt as lector_salida,
  pos_items.checkout_dt as lector_llegada,
  carreras.id as carreras_id
  from carreras
  left join pos_items on pos_items.name = carreras.numero_asignado
  where carreras.carrera_id = ?
  and pos_items.OID is not null
  ")

(defn process-carreras
  "postear salida y llegada de la table lector a la table carreras"
  [row]
  (let [carreras-id (:carreras_id row)
        prow {:salida (:lector_salida row)
              :llegada (:lector_llegada row)}
        result (Update db :carreras prow ["id = ?" carreras-id])]
    result))

(defn process-lector
  [rows]
  (map process-carreras rows))

(defn populate-tables
  "Populates table with default data"
  [table rows]
  (Query! db "delete from pos_items;")
  (Query! db (str "LOCK TABLES " table "WRITE;"))
  (Insert-multi db (keyword table) rows)
  (Query! db "UNLOCK TABLES;"))

(defn get-lector
  []
  (let [dbr-rows (Query dbr "select * from pos_items")]
    (populate-tables "pos_items" dbr-rows)
    (Query db [lector-sql (get-active-carrera)])))
;; End lector

(comment
  (get-lector)
  (Query db "select * from carreras where carrera_id = 10")
  (Query db "select * from pos_items")
  (Query dbr "select * from pos_items")
  (get-active-carrera))
