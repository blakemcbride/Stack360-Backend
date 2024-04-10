
(defclass2 MyClass () (cv1 cv2) (iv1 iv2))

(defclass2 MyClass2 () () ())

(defvar i1 (make-instance MyClass))

(defvar i2 (make-instance MyClass2))

(defmethod add-to-iv1 ((ins MyClass) val)
  (+ (get-slot ins iv1) val))

(get-slot i1 iv3)    ; this line causes the slot error

; (add-to-iv1 i2 4)    ; this line causes the no method for generic error

