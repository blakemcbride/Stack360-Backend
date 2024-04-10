
; Written by Blake McBride (blake@mcbride.name)


(defun fun1 ()
    (princ "Lisp's fun1 called")
    (terpri))

;;;;;;;;;;;;;;;;;;;;;;;;;;;

; The following macro creates a Lisp function named "println" that wraps the
; Java "println" method.  The first argument represents the name of the Lisp
; function to be created.  The second argument indicates the Java class.  The
; third argument indicates the name of the instance method to be called by the
; Lisp wrapper function.  The remaining arguments provides the Java type signature
; of the desired method (since Java can have many methods with the same name but
; differing by argument types.

(defun-instance-method println "java.io.PrintStream" "println" "java.lang.String")

; This Lisp function calls the Lisp function "println" created by the previous line.
; It is also making use of the (instance) argument passed from Java.

(defun fun2 (out)
    (println out "Lisp's fun2 called but printed from Java!"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defvar stdout nil)
(defun set-stdout (val)
    (setq stdout val))

; This function uses the Java object held by "out".  It calls the Lisp function
; "println" which is the Lisp wrapper for the Java "println" method.  Note that
; the Lisp code:  (println stdout "Lisp's fun3 called")
; is equivelent to the Java code:  stdout.println("Lisp's fun3 called");
; (provided that "stdout" was set to System.out within Java)

(defun fun3 ()
    (println stdout "Lisp's fun3 called"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Define a Lisp function that wrapps the "abs" Java class method.

(defun-class-method jabs "java.lang.Math" "abs" "int")

; This shows how a Java value can be passed to a Lisp function and function as
; a normal Lisp object.  It then calls Java with a Lisp object but the Java class
; method receives a normal Java object.  The Java result is then returned and again
; used as a normal Lisp object. The returned Lisp object is then used by Java.

(defun fun4 (x)
    (+ 10 (jabs (+ 1 x))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Call a local class method with String arguments.

; Create a Lisp function "jcmeth" that wraps the Java class method "cmeth1".

(defun-class-method jcmeth "com.arahant.lisp.Main" "cmeth1" "java.lang.String")

(defun fun5 (str)
    (jcmeth (concatenate 'string str "; from lisp fun5")))


;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Wrap the Java constructor for the Main class in a Lisp function named "newMain"

(defun-constructor newMain "com.arahant.lisp.Main")


; Wrap an instance method

(defun-instance-method jimeth1 "com.arahant.lisp.Main" "imeth1" "int")


; Create a new instance of the Java class "Main" and then call its instance method.

(defun fun6 ()
    (princ "Lisp fun6: ")
    (princ (jimeth1 (newMain) 42))
    (terpri))



