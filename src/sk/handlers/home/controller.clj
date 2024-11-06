(ns sk.handlers.home.controller
  (:require [sk.layout :refer [application error-404]]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [sk.handlers.home.model :refer [process-lector
                                            get-lector]]
            [sk.models.util :refer [get-session-id]]))

(def informacion
  (list
   [:div.container
    [:h5 "Detalles:"]
    [:li "Borra los datos de la tabla pos_items en cliclimobc.org"]
    [:li "Popula la tabla pos_items en ciclismobc.org con los dataos de rfi_mount pos_items"]
    [:li "Actualiza la tabla carreras en ciclismobc.org con los tiempos del rfid reader"]
    [:br]
    [:strong "Nota: "]
    [:span "Primero hay que borrar los datos de la tabla pos_items de la base de datos rfid_mount. La razon es que no hay referencia a la carrera. Es posible que en el historial de esa tabla existan los mismos numeros asignados a la carrera/paseo activo y no hay manera de identificarlos. Existe un campo que es la fecha que se dieron de alta los numeros pero esto no es confiable porque es posible tener mas de una fecha de asignacion de numeros."]
    [:br]
    [:h1 [:span.text-danger "Este proceso no se puede correr si no hay conexión de internet!!!"]]]))

(defn main
  [_]
  (let [title "home"
        ok 0
        js nil
        content informacion]
    (application title ok js content)))

(defn rfid
  [_]
  (let [result (doall (process-lector (get-lector)))]
    (if (seq result)
      (error-404 "Procesado con exito!" "/")
      (error-404 "No se pudo procesar!" "/"))))
