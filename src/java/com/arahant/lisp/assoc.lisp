
(defparameter *arahant-lisp-packages* (make-hash-table :test 'equal :size 100))

(defmacro fill-table (&rest vals)
  `(progn
     (clrhash *arahant-lisp-packages*)
     ,@(mapcar  (lambda (elm)
		  `(setf (gethash ,(car elm) *arahant-lisp-packages*)
			 ,(cadr elm)))
		vals)))

(defmacro append-table (&rest vals)
  `(progn
     ,@(mapcar  (lambda (elm)
		  `(setf (gethash ,(car elm) *arahant-lisp-packages*)
			 ,(cadr elm)))
		vals)))

(defun find-file (package)
  (gethash package *arahant-lisp-packages*))


(fill-table
	  ("TEST2" "test2")
	  ("TEST3" "test3")
)
