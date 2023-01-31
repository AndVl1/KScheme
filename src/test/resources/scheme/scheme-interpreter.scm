(define (evlis exprs)
    (if (null? exprs)
        '()
        (cons (evaluate (car exprs)) (evlis (cdr exprs)) )))

(define (from-racket-bool x)
    (if x 't 'f))

(define (to-racket-bool x)
    (cond ((eq? x 't) #t)
          ((eq? x 'f) #f)
          (else (error 'to-racket-bool "not t or f"))))

(define (evaluate expr)
  (cond ((number? expr) expr)
    ((eq? (car expr) 'quote) (cadr expr))
    ((eq? (car expr) 'if) (if (to-racket-bool (evaluate (cadr expr)))
                            (evaluate (caddr expr))
                            (evaluate (cadddr expr))))
    (else (apply-fun (car expr) (evlis (cdr expr))))))
                            
(define (apply-fun fun xs)
    (cond ((eq? fun '*) (* (car xs) (cadr xs)))
          ((eq? fun '+) (+ (car xs) (cadr xs)))
          ((eq? fun '-) (- (car xs) (cadr xs)))
          ((eq? fun '/) (/ (car xs) (cadr xs)))
          
          ((eq? fun 'car) (caar xs))
          ((eq? fun 'cdr) (cdar xs))
          ((eq? fun 'cons) (cons (car xs) (cadr xs)))
          
          ((eq? fun 'list) xs)
          ((eq? fun 'null?) (from-racket-bool (null? (car xs))))))
