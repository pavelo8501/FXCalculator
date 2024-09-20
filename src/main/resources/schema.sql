CREATE TABLE IF NOT EXISTS rates (
    id SERIAL PRIMARY KEY,
    currency VARCHAR(4) NOT NULL,
    rate DOUBLE PRECISION NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fees (
    id SERIAL PRIMARY KEY,
    from_currency_id INT NOT NULL,
    to_currency_id INT NOT NULL,
    fee DOUBLE PRECISION NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_from_currency_id FOREIGN KEY (from_currency_id) REFERENCES rates(id),
    CONSTRAINT fk_to_currency_id FOREIGN KEY (to_currency_id) REFERENCES rates(id)
);