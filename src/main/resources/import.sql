CREATE TABLE IF NOT EXISTS buku (
    id SERIAL PRIMARY KEY,
    judul VARCHAR(255),
    penulis VARCHAR(255),
    penerbit VARCHAR(255),
    tanggal_terbit DATE
);

CREATE TABLE temp_buku (
    id BIGINT,
    judul VARCHAR(255),
    penulis VARCHAR(255),
    penerbit VARCHAR(255),
    tanggal_terbit DATE
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED', -- UNVERIFIED, VERIFIED, APPROVED
    verification_token TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE refresh_tokens (
    id VARCHAR(255) PRIMARY KEY,                    -- Unique token ID
    user_id BIGINT NOT NULL,                       -- Foreign key ke user
    token_hash VARCHAR(255) NOT NULL,              -- Hashed refresh token
    expires_at TIMESTAMP NOT NULL,                 -- Token expiry
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,-- Waktu dibuat
    revoked_at TIMESTAMP NULL,                     -- Waktu di-revoke (jika ada)
    device_info VARCHAR(500),                      -- Info device/browser
    ip_address VARCHAR(45),                        -- IP address saat login
    last_used_at TIMESTAMP NULL,                   -- Terakhir digunakan
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_expires_at ON refresh_tokens(expires_at);
CREATE INDEX idx_token_hash ON refresh_tokens(token_hash);


INSERT INTO buku (judul, penulis, penerbit, tanggal_terbit) VALUES
('Refactoring', 'Martin Fowler', 'Addison-Wesley', '1999-07-08'),
('Domain-Driven Design', 'Eric Evans', 'Addison-Wesley', '2003-08-30'),
('Test-Driven Development', 'Kent Beck', 'Addison-Wesley', '2002-11-18'),
('Working Effectively with Legacy Code', 'Michael Feathers', 'Prentice Hall', '2004-09-30'),
('Continuous Delivery', 'Jez Humble', 'Addison-Wesley', '2010-07-27'),
('The Clean Coder', 'Robert C. Martin', 'Prentice Hall', '2011-05-23'),
('Patterns of Enterprise Application Architecture', 'Martin Fowler', 'Addison-Wesley', '2002-11-15'),
('You Don’t Know JS', 'Kyle Simpson', 'OReilly Media', '2015-03-27'),
('JavaScript: The Good Parts', 'Douglas Crockford', 'OReilly Media', '2008-05-15'),
('Effective Java', 'Joshua Bloch', 'Addison-Wesley', '2008-05-28'),
('Head First Design Patterns', 'Eric Freeman', 'OReilly Media', '2004-10-25'),
('Spring in Action', 'Craig Walls', 'Manning Publications', '2014-11-28'),
('Clean Architecture', 'Robert C. Martin', 'Prentice Hall', '2017-09-20'),
('Microservices Patterns', 'Chris Richardson', 'Manning Publications', '2018-11-19'),
('Groovy in Action', 'Dierk König', 'Manning Publications', '2007-04-01'),
('Learning React', 'Alex Banks', 'OReilly Media', '2020-01-10'),
('Programming TypeScript', 'Boris Cherny', 'OReilly Media', '2019-05-20'),
('Docker Deep Dive', 'Nigel Poulton', 'Leanpub', '2018-06-30'),
('Site Reliability Engineering', 'Betsy Beyer', 'OReilly Media', '2016-03-23');
