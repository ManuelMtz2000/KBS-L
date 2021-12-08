(deftemplate product
    (slot type)
    (slot name)
    (slot company)
    (slot color)
    (slot price)
)

(deftemplate credit_card
    (slot name)
)

(deftemplate order
    (slot id)
    (slot card)
)

(deftemplate car
    (slot name)
    (slot order)
    (slot type)
)

(deffacts products
    (product (type smartphone) (name iPhone13) (company apple) (color red) (price 14000))
    (product (type smartphone) (name moto_g) (company motorola) (color blue) (price 9000))
    (product (type smartphone) (name SamsungNote12) (company samsung) (color black) (price 12000))
    (product (type computer) (name Thinkpad) (company lenovo) (color black) (price 10000))
    (product (type computer) (name Mac) (company apple) (color white) (price 20000))
    (product (type computer) (name all_one) (company hp) (color black) (price 12000))
    (product (type laptop) (name Aspire) (company acer) (color black) (price 8000))
    (product (type laptop) (name MacbookAir) (company apple) (color gris) (price 15000))
    (product (type laptop) (name Pavilion) (company hp) (color black) (price 11000))
)

(deffacts credit_cards
    (credit_card (name Bancomer))
    (credit_card (name Banamex))
    (credit_card (name AmericaExpress))
    (credit_card (name Liverpool))
    (credit_card (name none))
)

(defrule iphone_months
    (order (id ?i)(card Banamex))
    (car (order ?i)(name iPhone13)(type smartphone))
    =>
    (printout t "Para la orden " ?i ", en la compra de este iPhone 13 con su tarjeta Banamex, le ofrecemos 24 meses sin intereses :D" crlf)
)

(defrule samsung_months
    (order (id ?i)(card Liverpool))
    (car (order ?i)(name SamsungNote12)(type smartphone))
    =>
    (printout t "Para la orden " ?i ", en la compra de este Samsung Note 12 con su tarjeta Liverpool, le ofrecemos 12 meses sin intereses :D" crlf)
)

(defrule mac_iphone_promo
    (order (id ?i)(card none))
    (car (order ?i)(name iPhone13)(type smartphone))
    (car (order ?i)(name MacbookAir)(type laptop))
    =>
    (printout t "Para la orden " ?i ", en la compra de su Macbook Air y iPhone 13 al contado, le ofrecemos $100 MXN en vales por cada $1000 MXN en compra :)" crlf)
)

(defrule offer_for_cellphone
    (order (id ?i)(card ?c))
    (car (order ?i)(name ?n)(type smartphone))
    =>
    (printout t "Para la orden " ?i ", en la compra de este " ?n " le ofrecemos una funda y mica con 15% de descuento :D" crlf)
)
