CREATE VIEW cart_view AS
SELECT 
    c.id AS cart_id,
    u.id AS user_id,
    u.email AS user_email,
    p.id AS product_id,
    p.name AS product_name,
    p.price AS current_price,  -- Prezzo attuale del prodotto
    ci.quantity,
    (ci.quantity * p.price) AS subtotal  -- Calcolo del subtotale con il prezzo aggiornato
FROM carts_item ci
JOIN carts c ON ci.cart_id = c.id
JOIN users u ON c.user_id = u.id
JOIN products p ON ci.product_id = p.id
where p.active = true;