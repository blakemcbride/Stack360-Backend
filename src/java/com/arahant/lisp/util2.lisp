
; Written by Blake McBride (blake@mcbride.name)


(defmacro defun-class-method (lfun cls-name meth-name &rest arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) arg-types))
	(method (gensym))
	(cls (gensym)))
    `(let ((,method (jmethod ,cls-name ,meth-name ,@arg-types))
	   (,cls (jclass ,cls-name)))
       (defun ,lfun ,arglist
	 (jcall ,method ,cls ,@arglist)))))

(defmacro defun-instance-method (lfun cls-name meth-name &rest arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) (cons nil arg-types)))
	(method (gensym)))
    `(let ((,method (jmethod ,cls-name ,meth-name ,@arg-types)))
       (defun ,lfun ,arglist
	 (jcall ,method ,@arglist)))))

(defmacro defun-constructor (lfun cls-name &rest arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) arg-types))
	(method (gensym)))
    `(let ((,method (jconstructor ,cls-name ,@arg-types)))
       (defun ,lfun ,arglist
	 (jnew ,method ,@arglist)))))

(defun-class-method Class.forName "java.lang.Class" "forName" "java.lang.String")
(defun-instance-method Class.getDeclaredMethods "java.lang.Class" "getDeclaredMethods")
(defun-instance-method Class.getDeclaredFields "java.lang.Class" "getDeclaredFields")
(defun-instance-method Class.getName "java.lang.Class" "getName")
(defun-instance-method Class.getSuperclass "java.lang.Class" "getSuperclass")
(defun-instance-method Method.getName "java.lang.reflect.Method" "getName")
(defun-instance-method Method.getReturnType "java.lang.reflect.Method" "getReturnType")
(defun-instance-method Method.getParameterTypes "java.lang.reflect.Method" "getParameterTypes")
(defun-instance-method Field.getName "java.lang.reflect.Field" "getName")
(defun-instance-method Field.getModifiers "java.lang.reflect.Field" "getModifiers")
(defun-class-method Modifier.isPublic "java.lang.reflect.Modifier" "isPublic" "int")

(defmacro while (what &rest code)
  (let ((var (gensym)))
    `(do ()
	  ((not ,what))
	,@code)))

(defmacro while-more (s &rest code)
    `(while (next ,s) ,@code))

(defmacro cat (&rest str)
    `(concatenate 'string ,@str))

(defmacro println (&rest v)
    `(progn (princ (cat ,@v))
        (terpri)))

;(defmacro toString (num)
;    `(format nil "~A" ,num))

(defun get-last-part (name)
  (let ((idx (position #\. name :from-end t)))
    (if  (numberp idx)
	 (subseq name (1+ idx))
	 name)))

(defmacro map-java-instance-methods (class-name)
  (let* ((cls (Class.forName class-name))
	 (cls-name (Class.getName cls))
	 (methods (Class.getDeclaredMethods cls))
	 (nmethods (array-total-size methods))
	 (package (string-upcase (get-last-part cls-name)))
	 ret)
    `(progn
       ,@(do ((i 0 (1+ i)))
	     ((eql i nmethods) ret)
	     (let* ((meth (aref methods i))
		    (meth-name (Method.getName meth))
		    (params (Method.getParameterTypes meth))
		    (np (array-total-size params))
		    (arg-types (nreverse (do ((i 0 (1+ i))
					      (pl nil pl))
					     ((eql i np) pl)
					   (setq pl (cons (Class.getName (aref params i)) pl)))))
		    (lisp-func-name (intern (string-upcase meth-name)))
		    (method (gensym))
		    (arglist (do ((i 0 (1+ i))
				  (al nil al))
				 ((> i np) al)
			       (setq al (cons (gensym) al)))))
	       (setq ret (cons `(let ((,method (jmethod ,cls-name ,meth-name ,@arg-types)))
				  (defun ,lisp-func-name ,arglist
				    (jcall ,method ,@arglist)))
			       ret)))))))

(defmacro map-entire-java-class (class-name)
  (let* ((cls (Class.forName class-name))
	 (cls-name (Class.getName cls))
	 (methods (Class.getDeclaredMethods cls))
	 (nmethods (array-total-size methods))
	 (fields (Class.getDeclaredFields cls))
	 (nfields (array-total-size fields))
	 (short-name (get-last-part cls-name))
	 (class (intern (string-upcase short-name)))
	 (super-class (Class.getSuperClass cls))
	 (super-class-name (Class.getName super-class))
	 (super-class-short-name (get-last-part super-class-name))
	 (super-symbol (intern (string-upcase super-class-short-name)))
	 ret fret)
    `(progn
       
       ;; (defclass ,class
       ;; 	   ,(if (not (or (equal super-class-short-name "ArahantBean")
       ;; 			       (equal super-class-short-name "AuditedBean")
       ;; 			       (equal super-class-short-name "Filtered")))
       ;; 		      (list super-symbol))
       ;; 	 ((java-instance :initarg :java-object)
       ;; 			    (class-name :allocation :class :initform ,class-name)
       ;; 			    (class-object :allocation :class :initform (Class.forName ,class-name))))
       
       (defclass2 ,class
	   ,(if (not (or (equal super-class-short-name "ArahantBean")
			 (equal super-class-short-name "AuditedBean")
			 (equal super-class-short-name "Filtered")))
		(list super-symbol))
	 ((class-name :initform ,class-name)
	  (class-object :initform (Class.forName ,class-name)))
	 ((java-instance :initarg :java-object)))

       
       ,@(do ((i 0 (1+ i)))
	     ((eql i nfields) fret)
	     (let* ((fld (aref fields i))
		    (fld-name (Field.getName fld))
		    (mods (Field.getModifiers fld)))
	       (if (Modifier.isPublic mods)
		   (setq fret (cons `(defvar ,(intern (string-upcase (concatenate 'string short-name "." fld-name))) (jfield ,class-name ,fld-name))
				    fret)))))
       ,@(do ((i 0 (1+ i)))
	     ((eql i nmethods) ret)
	     (let* ((meth (aref methods i))
		    (meth-name (Method.getName meth))
		    (return-type (Method.getReturnType meth))
		    (return-type-short-name (get-last-part (Class.getName return-type)))
		    (params (Method.getParameterTypes meth))
		    (np (array-total-size params))
		    (arg-types (nreverse (do ((i 0 (1+ i))
					      (pl nil pl))
					     ((eql i np) pl)
					   (setq pl (cons (Class.getName (aref params i)) pl)))))
		    (lisp-func-name (intern (string-upcase meth-name)))
		    (method (gensym))
		    (tmpvar (gensym))
		    (arglist (do ((i 0 (1+ i))
				  (al nil al))
				 ((eql i np) al)
			       (setq al (cons (gensym) al)))))
	       (setq ret (cons (if (or (equal return-type-short-name "byte")
				       (equal return-type-short-name "char")
				       (equal return-type-short-name "short")
				       (equal return-type-short-name "int")
				       (equal return-type-short-name "long")
				       (equal return-type-short-name "float")
				       (equal return-type-short-name "double")
				       (equal return-type-short-name "String"))
				   `(let ((,method (jmethod ,cls-name ,meth-name ,@arg-types)))
				      (defmethod ,lisp-func-name ((self ,class) ,@arglist)
					(jcall ,method (slot-value self 'java-instance) ,@arglist)))
				   `(let ((,method (jmethod ,cls-name ,meth-name ,@arg-types)))
				      (defmethod ,lisp-func-name ((self ,class) ,@arglist)
					(let ((,tmpvar (jcall ,method (slot-value self 'java-instance) ,@arglist)))
					  (if ,tmpvar
					      (make-instance ',(intern (string-upcase return-type-short-name)) :java-object ,tmpvar))))))
			       ret)))))))

;; (defmacro map-java-class (lisp-class java-class-name)
;;   `(defvar ,lisp-class
;;      (defclass ,lisp-class () ((java-instance :initarg :java-object)
;; 			       (class-name :allocation :class :initform ,java-class-name)
;; 			       (class-object :allocation :class :initform (jclass ,java-class-name))))))

;; (defmacro java-class-name (lisp-class)
;;   `(let ((i (make-instance ',lisp-class)))
;;      (slot-value i 'class-name)))

;; (defmacro java-class-object (lisp-class)
;;   `(let ((i (make-instance ',lisp-class)))
;;      (slot-value i 'class-object)))

(defmacro map-java-class (lisp-class java-class-name)
  `(defclass2 ,lisp-class ()
     ((class-name :initform ,java-class-name)
      (class-object :initform (jclass ,java-class-name)))
     ((java-instance :initarg :java-object))))

(defmacro java-class-name (lisp-class)
  `(get-slot ,lisp-class class-name))

(defmacro java-class-object (lisp-class)
  `(get-slot ,lisp-class class-object))

; Maps a Java method to a CLOS method returning a Java native object

(defmacro map-java-instance-method (lisp-method lisp-class java-meth-name &rest java-arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) java-arg-types))
	(method (gensym)))
    `(let ((,method (jmethod (java-class-name ,lisp-class) ,java-meth-name ,@java-arg-types)))
       (defmethod ,lisp-method ((self ,lisp-class) ,@arglist)
	 (jcall ,method (slot-value self 'java-instance) ,@arglist)))))

; The "-with-wrapper" means that it returns in instance of another class

(defmacro map-java-class-method-with-wrapper (lisp-method lisp-class java-meth-name other-class &rest java-arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) java-arg-types))
	(method (gensym))
	(cls (gensym))
	(tmpvar (gensym)))
    `(let ((,method (jmethod (java-class-name ,lisp-class) ,java-meth-name ,@java-arg-types))
	   (,cls (java-class-object ,lisp-class)))
       (defmethod ,lisp-method ((self (eql ,lisp-class)) ,@arglist)
	 (let ((,tmpvar (jcall ,method ,cls ,@arglist)))
	   (if ,tmpvar
	       (make-instance ',other-class :java-object ,tmpvar)))))))

(defmacro map-java-class-method (lisp-method lisp-class java-meth-name &rest java-arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) java-arg-types))
	(method (gensym))
	(cls (gensym)))
    `(let ((,method (jmethod (java-class-name ,lisp-class) ,java-meth-name ,@java-arg-types))
	   (,cls (java-class-object ,lisp-class)))
       (defmethod ,lisp-method ((self (eql ,lisp-class)) ,@arglist)
	 (jcall ,method ,cls ,@arglist)))))

(defmacro map-java-constructor (lisp-method lisp-class &rest java-arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) java-arg-types))
	(method (gensym)))
    `(let ((,method (jconstructor (java-class-name ,lisp-class) ,@java-arg-types)))
       (defmethod ,lisp-method ((self (eql ,lisp-class)) ,@arglist)
	 (make-instance ',lisp-class :java-object (jnew ,method ,@arglist))))))

(defmacro map-java-instance-method-with-wrapper (lisp-method lisp-class java-meth-name other-class &rest java-arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) java-arg-types))
	(method (gensym))
	(tmpvar (gensym)))
    `(let ((,method (jmethod (java-class-name ,lisp-class) ,java-meth-name ,@java-arg-types)))
       (defmethod ,lisp-method ((self ,lisp-class) ,@arglist)
	 (let ((,tmpvar (jcall ,method (slot-value self 'java-instance) ,@arglist)))
	   (if ,tmpvar
	       (make-instance ',other-class :java-object ,tmpvar)))))))

(defmacro map-java-instance-method-return-self (lisp-method lisp-class java-meth-name &rest java-arg-types)
  (let ((arglist (mapcar (lambda (x) (gensym)) java-arg-types))
	(method (gensym)))
    `(let ((,method (jmethod (java-class-name ,lisp-class) ,java-meth-name ,@java-arg-types)))
       (defmethod ,lisp-method ((self ,lisp-class) ,@arglist)
	 (jcall ,method (slot-value self 'java-instance) ,@arglist)
	 self))))

(defmacro list-loop (lst java-rec locals &body body)
  (let ((it (gensym)))
    `(let ((,it (iterator ,lst))
	   ,java-rec
	   ,@locals)
       (while (hasNext ,it)
	 (setq ,java-rec (next ,it))
	 ,@body))))

(defmacro wrap-java (java-obj class)
  `(make-instance ',class :java-object ,java-obj))

(defmacro rec-loop (lst lisp-rec class locals &body body)
  (let ((jrec (gensym)))
    `(list-loop ,lst ,jrec ,locals
;		(wrap-java ,lisp-rec ,jrec ,class)
		(setq ,lisp-rec (make-instance ',class :java-object ,jrec))
		,@body)))


;; (defmacro createHCU (hsu cls)
;;   `(createCriteria ,hsu (let ((i (make-instance ',cls)))
;; 			  (slot-value i 'class-name))))

(defmacro createHCU (hsu cls)
  `(createCriteria ,hsu (get-slot ,cls class-name)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; (map-java-class JClass "java.lang.Class")
;; (map-java-class JMethod "java.lang.reflect.Method")
;; (map-java-class JField "java.lang.reflect.Field")
;; (map-java-class JModifier "java.lang.reflect.Modifier")

;; (map-java-class-method-with-wrapper forName JClass "forName" JClass "java.lang.String")
;; (map-java-instance-method getDeclaredMethods JClass "getDeclaredMethods")
;; (map-java-instance-method getDeclaredFields JClass "getDeclaredFields")
;; (map-java-instance-method getName JClass "getName")
;; (map-java-instance-method getName JMethod "getName")
;; (map-java-instance-method getParameterTypes JMethod "getParameterTypes")
;; (map-java-instance-method getName JField "getName")
;; (map-java-instance-method getModifiers JField "getModifiers")
;; (map-java-instance-method isPublic JModifier "isPublic" "int")

