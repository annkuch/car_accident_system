CREATE TABLE IF NOT EXISTS accident_type (
                                             type_id INT PRIMARY KEY,
                                             type_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS accident_rule (
                                             rule_id INT PRIMARY KEY,
                                             rule_name VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS accident (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
                                        date_accident DATE NOT NULL,
                                        time_accident TIME NOT NULL,
                                        text VARCHAR(10000),
                                        address VARCHAR(100),
                                        accident_type_id INT NOT NULL,
                                        accident_rule_id INT NOT NULL,
                                        CONSTRAINT fk_accident_type FOREIGN KEY (accident_type_id) REFERENCES accident_type(type_id),
                                        CONSTRAINT fk_accident_rule FOREIGN KEY (accident_rule_id) REFERENCES accident_rule(rule_id)
);

-- Заповнення таблиці accident_type
INSERT INTO accident_type (type_id, type_name) VALUES
                                                   (1, 'Два транспортні засоби'),
                                                   (2, 'Транспортний засіб і людина'),
                                                   (3, 'Транспортний засіб й інший транспорт'),
                                                   (4, 'Громадський транспорт'),
                                                   (5, 'Інше');

-- Заповнення таблиці accident_rule
INSERT INTO accident_rule (rule_id, rule_name) VALUES
                                                   (1, 'Стаття 286 ККУ – Порушення правил безпеки дорожнього руху'),
                                                   (2, 'Стаття 130 КпАП – Водіння в нетверезому стані'),
                                                   (3, 'Стаття 132 КпАП – Водіння без водійського посвідчення'),
                                                   (4, 'Стаття 124 КпАП – Порушення правил дорожнього руху, що спричинило матеріальну шкоду'),
                                                   (5, 'Стаття 117 КпАП – Недотримання дистанції'),
                                                   (6, 'Стаття 122 КпАП – Порушення правил обгону'),
                                                   (7, 'Стаття 152 КпАП – Порушення правил паркування');
