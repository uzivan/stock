CREATE TABLE stock_products
(
    id INT NOT NULL AUTO_INCREMENT,
    price INT NOT NULL,
    name VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE stock_users
(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    password VARCHAR(200) NOT NULL,
    username VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE stock_orders
(
    id INT NOT NULL AUTO_INCREMENT,
    time DATETIME NOT NULL,
    status VARCHAR(10) NOT NULL,
    id_users INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_users) REFERENCES stock_users(id)
);

CREATE TABLE stock_order_products
(
    id_product INT NOT NULL,
    id_order INT NOT NULL,
    PRIMARY KEY (id_product, id_order),
    FOREIGN KEY (id_order) REFERENCES stock_orders(id),
    FOREIGN KEY (id_product) REFERENCES stock_products(id)
);

CREATE table stock_roles(
                            id INT NOT NULL PRIMARY KEY,
                            name VARCHAR(100)
);

CREATE TABLE stock_user_roles(
                                 user_id INT,
                                 role_id INT,
                                 FOREIGN KEY (user_id) REFERENCES stock_users(id),
                                 FOREIGN KEY (role_id) REFERENCES stock_roles(id)
);

INSERT INTO stock_users(id, username, password, name) VALUE
    (1, 'ivan','$2a$04$hPflLhA15vp8nBU/Q4KF5OyzfLhLKafnugMjpYZT6HWz0jcZA2I/S', 'ivan'),
    (2, 'dima', '$2a$04$wrn5/wmFlLQre.Djd9kVH.CBdEGLF4JcQPCvoJeTAACxWRLoGj66K', 'dima');
INSERT INTO stock_roles(id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_SALESMAN');
INSERT INTO stock_user_roles(user_id, role_id) VALUES
                                                   (1, 1),
                                                   (2, 2);

INSERT INTO stock_products(name, price) VALUES ('cola', 200),
                                               ('lipton', 300),
                                               ('sprite', 400);


CREATE TABLE stock_gen_id_orders(
                                    gen_value INT,
                                    gen_name VARCHAR(30)
);
CREATE TABLE stock_gen_id_products(
                                      gen_value INT,
                                      gen_name VARCHAR(30)
);