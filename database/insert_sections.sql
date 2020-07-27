USE shopping_list_test;
INSERT INTO section(name) VALUES
('Kwiaty'), ('Kawa i herbata'), ('Słodycze'), ('Pieczywo'), ('Pieczywo pakowane'), ('BIO'), ('Bakalie'),
                                 ('Produkty konserwowe i sosy'), ('Owoce'), ('Warzywa'), ('Produkty sypkie'),
                                 ('Przyprawy'), ('Dania gotowe'), ('Przetwory warzywne i świeże soki'),
                                 ('Wyroby mięsne'), ('Produkty rybne'), ('Sery'), ('Produkty sezonowe - lodówka'),
                                 ('Produkty mleczne'), ('Mrożonki'), ('Ryby'), ('Mięsa'), ('Produkty sezonowe'),
                                 ('Artykuły różne'), ('Lody'), ('Mrożonki warzywne'),
                                 ('Papier toaletowy i ręczniki papierowe'), ('Słone przekąski'), ('Alkohole'),
                                 ('Napoje'), ('Karma dla zwierząt'), ('Chemia i kosmetyki'), ('Produkty do wypieków'),
                                 ('Smarowidła słodkie'), ('Produkty zbożowe');

INSERT INTO store(name) VALUES ('Lidl Średzka'), ('Lidl Królewiecka');

INSERT INTO store_section(store_id, section_id, position) VALUES (1,1,1), (1,2,2), (1,3,3), (1,4,4), (1,5,5), (1,6,6),
                                                                 (1,7,7), (1,8,8), (1,9,9), (1, 10, 10), (1, 11, 11),
                                                                 (1, 12, 12), (1, 13, 13), (1, 14, 14), (1, 15, 15),
                                                                 (1, 16, 16), (1, 17, 17), (1, 18, 18), (1, 19, 19),
                                                                 (1, 20, 20), (1, 21, 21), (1, 22, 22), (1, 23, 23),
                                                                 (1, 24, 24), (1, 25, 25), (1, 26, 26), (1, 27, 27),
                                                                 (1, 28, 28), (1, 29, 29), (1, 30, 30), (1, 31, 31),
                                                                 (1, 32, 32), (1, 33, 33) , (1, 34, 34), (1, 35, 35);

INSERT INTO product(default_unit, name, section_id) VALUES (1, 'Ryż', 11), (2, 'Mleko', 33), (3, 'Banan', 9),
                                                           (3, 'Jabłko', 9), (1, 'Łosoś wędzony', 16), (2, 'Woda', 30);

INSERT INTO cart_item(quantity, unit, product_id) VALUES (10, 1, 1), (3, 3, 3), (2, 3, 5);


