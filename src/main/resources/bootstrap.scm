;;;;; bootstrap.scm
;;;;;
;;;;; This file contains portions from
;;;;; prelude.scheme -- UMB Scheme, standard primitives in Scheme.
;;;;; Copyright 1988, 1991 University of Massachusetts

;;;; Error Handling

;------------------------------------------------
;todo
(define error-abort #f)

(begin (call/cc (lambda (x) (set! error-abort x))) (display "\n")  )

(define (error msg . obj )
  (display "\n*** ") (display msg)
  (if (null? obj)
    (set! __errobj obj)
    (begin (display " ") (display (car obj)) (set! __errobj (car obj)) ))
  (error-abort #f))
;------------------------------------------------

;;;; LISTS

;(define (null? x) (eq? x '()))
;
;(define (list . elems) elems)

;------------------------------------------------
;todo
(define (list-copy x)
  (if (pair? x)
    (cons (car x) (list-copy (cdr x)))
    x))

(define (last x)
  (if (pair? x)
    (if (null? (cdr x))
      x
      (last (cdr x)))
    (error "last" x)))

(define (append . args)
  (define (_append2 lst1 lst2)
    (if (null? lst1)
        lst2
        (cons (car lst1) (_append2 (cdr lst1) lst2))))
  (if (not (pair? args))
      args
      (if (null? (cdr args))
        (car args)
        (_append2 (car args) (apply append (cdr args))))))
;------------------------------------------------

; nconc


;;;; Quasiquotation
;;;; Adapted from "Quasiquotation in Lisp" by Alan Bawden, Brandeis University

;------------------------------------------------
;todo
(define (tag-backquote? x)
  (if (pair? x)
    (eq? (car x) 'quasiquote)
    #f))

(define (tag-comma? x)
  (if (pair? x)
    (eq? (car x) 'unquote)
    #f))

(define (tag-comma-atsign? x)
  (if (pair? x)
    (eq? (car x) 'unquote-splicing)
    #f))

(define (tag-data x) (car (cdr x)))

(define (qq-expand x)
  (if (tag-comma? x)
   (tag-data x)
  (if (tag-comma-atsign? x)
   (error "illegal usage of ,@")
  (if (tag-backquote? x)
   (qq-expand
    (qq-expand (tag-data x)))
  (if (pair? x)
   (list 'append
    (qq-expand-list (car x))
    (qq-expand (cdr x)))
   (list 'quote x))))))

(define (qq-expand-list x)
  (if (tag-comma? x)
   (list 'list (tag-data x))
  (if (tag-comma-atsign? x)
   (tag-data x)
  (if (tag-backquote? x)
   (qq-expand-list
     (qq-expand (tag-data x)))
  (if (pair? x)
   (list 'list
      (list 'append
        (qq-expand-list (car x))
        (qq-expand (cdr x))))
   (list 'quote (list x)))))))

(define quasiquote
  (make-rewriter (lambda (_x)
    (qq-expand (tag-data _x))
    )))
;------------------------------------------------

;;;; PRIMITIVE  PROCEDURES

;;; equal?

;------------------------------------------------
;todo
(define (equal? a b)
  (define (equal-pair? a b)
    (if (equal? (car a) (car b))
      (equal? (cdr a) (cdr b))
      #f))
  (if (pair? a)
    (if (pair? b)
      (equal-pair? a b)
      #f)
    (if (pair? b)
      #f
      (eqv? a b)) ) )
;------------------------------------------------

;;; map

;------------------------------------------------
;todo
(define (map f list)
  (define (map1 f list res)
    (if (null? list)
      res
      (map1 f (cdr list) (append res (cons (f (car list)) '() ))) ))
  (map1 f list '()) )
;------------------------------------------------

;;; Pairs and lists.

;; car - cdr compositions  (caar pair) ... (cddddr pair)

; TODO --------------------------------------------
(define (caar x) (car (car x)))
(define (cadr x) (car (cdr x)))
(define (cdar x) (cdr (car x)))
(define (cddr x) (cdr (cdr x)))

(define (caaar x) (car (car (car x))))
(define (caadr x) (car (car (cdr x))))
(define (cadar x) (car (cdr (car x))))
(define (caddr x) (car (cdr (cdr x))))
(define (cdaar x) (cdr (car (car x))))
(define (cdadr x) (cdr (car (cdr x))))
(define (cddar x) (cdr (cdr (car x))))
(define (cdddr x) (cdr (cdr (cdr x))))

(define (caaaar x) (car (car (car (car x)))))
(define (caaadr x) (car (car (car (cdr x)))))
(define (caadar x) (car (car (cdr (car x)))))
(define (caaddr x) (car (car (cdr (cdr x)))))
(define (cadaar x) (car (cdr (car (car x)))))
(define (cadadr x) (car (cdr (car (cdr x)))))
(define (caddar x) (car (cdr (cdr (car x)))))
(define (cadddr x) (car (cdr (cdr (cdr x)))))
(define (cdaaar x) (cdr (car (car (car x)))))
(define (cdaadr x) (cdr (car (car (cdr x)))))
(define (cdadar x) (cdr (car (cdr (car x)))))
(define (cdaddr x) (cdr (car (cdr (cdr x)))))
(define (cddaar x) (cdr (cdr (car (car x)))))
(define (cddadr x) (cdr (cdr (car (cdr x)))))
(define (cdddar x) (cdr (cdr (cdr (car x)))))
(define (cddddr x) (cdr (cdr (cdr (cdr x)))))


;;; (memq   obj list)
;;; (memv   obj list)
;;; (member obj list)

;------------------------------------------------
;todo
(define (memq obj list)
    (if (null? list) #f
  (if (not (pair? list))
      (error "2nd arg to memq not a list: " list)
      (if (eq?  obj (car list)) list
    (memq  obj (cdr list)) ))))
;------------------------------------------------

;------------------------------------------------
;todo
(define (memv obj list)
    (if (null? list) #f
  (if (not (pair? list))
      (error "2nd arg to memv not a list: " list)
      (if (eqv?  obj (car list)) list
    (memv  obj (cdr list)) ))))
;------------------------------------------------

;------------------------------------------------
;todo
(define (member obj list)
    (if (null? list) #f
  (if (not (pair? list))
      (error "2nd arg to member not a list: " list)
      (if (equal?  obj (car list)) list
    (member  obj (cdr list)) ))))
;------------------------------------------------

;;; (assq  obj alist)
;;; (assv  obj alist)
;;; (assoc obj alist)

;------------------------------------------------
;todo
(define (assq obj alist)
    (if (null? alist) #f
  (if (not (pair? alist))
      (error "2nd argument to assq not a list: " alist)
      (if (eq? (caar alist) obj) (car alist)
    (assq obj (cdr alist))))))
;------------------------------------------------

;------------------------------------------------
(define (assv obj alist)
    (if (null? alist) #f
  (if (not (pair? alist))
      (error "2nd argument to assv not a list: " alist)
      (if (eqv? (caar alist) obj) (car alist)
    (assv obj (cdr alist))))))
;
;
(define (assoc obj alist)
    (if (null? alist) #f
  (if (not (pair? alist))
      (error "2nd argument to assoc not a list: " alist)
      (if (equal? (caar alist) obj) (car alist)
    (assoc obj (cdr alist))))))
;------------------------------------------------
(define _defmacro (make-rewriter
                    (lambda (_x)
                      `(define ,(caar (cdr _x)) (make-rewriter
                                                  (lambda (_form) (_let1 (( ,@(cdar (cdr _x)) (cdr _form) ))
                                                                    (begin ,@(cdr (cdr _x)) )))))) ))

(define _let1 (make-rewriter
                (lambda (_x)
                  `( (lambda (,(caaar (cdr _x))) ,@(cdr (cdr _x))  )
                     ,(cadaar (cdr _x)) ) )))

(_defmacro (let _x)
  (define decl (car _x))
  (define body (cdr _x))
  `((lambda ,(map car decl) ,@body) ,@(map cadr decl)  ))

(_defmacro (let* x)
  (if (null? (cdar x))
    `(let ,@x)
    `(let (,(caar x)) (let* ,(cdar x) ,@(cdr x)) )))

(_defmacro (cond form)
  (if (null? form) ''()
                   (let ((c1 (car form)))
                     (if (not (pair? c1))
                       (error "invalid cond syntax: " form)
                       (if (eq? (car c1) 'else)
                         `(begin ,@(cdr c1))
                         (if (null? (cdr c1))
                           `(or ,(car c1)
                              (cond ,@(cddr form)))
                           (if (eq? (cadr c1) '=>)
                             (let ((t (gensym "_t"))
                                    (r (gensym "_r"))
                                    (c (gensym "_c")))
                               `(let ((,t ,(car c1))
                                       (,r (lambda () ,@(cddr c1)))
                                       (,c (lambda () (cond ,@(cdr form)))))
                                  (if ,t ((,r),t) (,c))) )
                             `(if ,(car c1)
                                (begin ,@(cdr c1))
                                (cond ,@(cdr form)))
                             )))))))