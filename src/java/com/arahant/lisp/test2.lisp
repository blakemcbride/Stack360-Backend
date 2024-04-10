
(defpackage "TEST2"
  (:use "COMMON-LISP" "UTILS" "MAPPINGS"))
(in-package "TEST2")

;; (defvar *count* 0)
;; (setq *count* (1+ *count*))
;; (format t "~%test2 *count* = ~d~2%" *count*)

(defun main (in out hsu)
    (main2 (wrap-java in DataObjectMap)
	   (wrap-java out DataObjectMap)
	   (wrap-java hsu HibernateSessionUtil)))

(defun checkRight (in out hsu)
  (let ((jin (wrap-java in DataObjectMap))
	(jout (wrap-java out DataObjectMap))
	(jhsu (wrap-java hsu HibernateSessionUtil)))
	(putInt jout "accessLevel" 2)))

(defun listEEOCategories (in out hsu)
  (let ((jin (wrap-java in DataObjectMap))
	(jout (wrap-java out DataObjectMap))
	(jhsu (wrap-java hsu HibernateSessionUtil)))
    (let ((itemList (new DataObjectList)))
      (addObjectMap itemList
      		    (let ((item (new DataObjectMap)))
      		      (putString item "eeoCategoryId" "00000-0000000003")
      		      (putString item "name" "Blue")
      		      item))
      (addObjectMap itemList
      		 (let ((item (new DataObjectMap)))
      		   (putString item "eeoCategoryId" "00000-0000000004")
      		   (putString item "name" "Green")
      		   item))
      (addObjectMap itemList
      		 (let ((item (new DataObjectMap)))
      		   (putString item "eeoCategoryId" "00000-0000000007")
      		   (putString item "name" "Yellow")
      		   item))
      (putObjectList jout "itemList" itemList)
      )))
      

(defun dumpMAP (desc map)
  (princ "-----------------------------")
  (terpri)
  (princ desc)
  (terpri)
  (printMap map)
  (princ "-----------------------------")
  (terpri))


(defun main2 (in out hsu)
  (dumpMAP "in" in)
  (let ((hcu (createHCU hsu HrEeoCategory))
	scr)

    (setCurrentPersonToArahant hsu)

    (orderBy hcu HrEeoCategory.NAME)
    (setq scr (scroll hcu))

 
    (rec-loop scr rec HrEeoCategory ()
    	      (println (getEeoCategoryId rec) "  " (getName rec)))

    (putInt out "abcd" 4)
    (putString out "myString" "The Value")
    (putString out "someOthher string"  "motorcycle")
    (putString out "Brad" "Programmer")))
