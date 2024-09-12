CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        age INT NOT NULL

);
create table type_consomations(
        id SERIAL PRIMARY KEY,
        type VARCHAR(200) NOT NULL
);

create table consomations(
        id SERIAL PRIMARY KEY,
        user_id INT NOT NULL,
        tconsomation_id INT NOT NULL,
        value INT NOT NULL,
        start_date DATE NOT NULL,
        end_date DATE NOT NULL,
        FOREIGN KEY (tconsomation_id) REFERENCES type_consomations(id)
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transport (
        distance DOUBLE PRECISION,
        type VARCHAR(100)
) INHERITS (consomations);
CREATE TABLE logement (
        energie DOUBLE PRECISION,
        type VARCHAR(100)
) INHERITS (consomations);
CREATE TABLE alimentation (
        poids DOUBLE PRECISION,
        type VARCHAR(100)
) INHERITS (consomations);
