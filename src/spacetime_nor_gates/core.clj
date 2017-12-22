(ns spacetime-nor-gates.core
    (:require [fun.imagej.img :as img]
              [fun.imagej.core :as ij]
              [fun.imagej.ops :as ops]
              [fun.imagej.img.shape :as shape]
              [fun.imagej.conversion :as convert]
              [fun.imagej.segmentation.imp :as ij1seg]
              [fun.imagej.img.cursor :as cursor]
              [fun.imagej.imp.roi :as roi]
              [fun.imagej.img.type :as imtype]
              [fun.imagej.imp :as ij1]
              [clojure.string :as string]
              [clojure.java.io :as io]))

(def rois (roi/open-roi-zip (.getPath (io/resource "V5_IJ1_ROIs.zip"))))
(def input (convert/imp->img (ij1/open-imp (.getPath (io/resource "V5_fullNOR_shrinkquarter_compress.gif")))))

(doseq [roi rois]
       (let [roi-length (count (iterator-seq (.iterator roi)))
             branch-img (fun.imagej.ops.create/img (long-array [roi-length (img/get-size-dimension input 2)]))]
          (let [liter (.iterator roi)]
               (loop [k 0]
                     (when (.hasNext liter)
                           (let [pt (.next liter)
                                 x (.getX pt)
                                 y (.getY pt)]
                                (dotimes [t (img/get-size-dimension input 2)]
                                         (img/set-val branch-img (long-array [k t])
                                                      (double (img/get-val input (long-array [x y t]))))))
                           (recur (inc k)))))
          (let [roi-imp (convert/img->imp (fun.imagej.ops.convert/float32 branch-img))]
               (img/show branch-img))))
