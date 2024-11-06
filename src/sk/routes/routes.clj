(ns sk.routes.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.home.controller :as home-controller]))

(defroutes open-routes
  (GET "/" params home-controller/main)
  (GET "/procesar/rfid" params (home-controller/rfid params)))
