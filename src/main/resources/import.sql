CREATE TABLE IF NOT EXISTS buku (
    id SERIAL PRIMARY KEY,
    judul VARCHAR(255),
    penulis VARCHAR(255),
    penerbit VARCHAR(255),
    tanggal_terbit DATE
);



INSERT INTO buku (judul, penulis, penerbit, tanggal_terbit) VALUES
('Clean Code', 'Robert C. Martin', 'Prentice Hall', '2008-08-01'),
('The Pragmatic Programmer', 'Andrew Hunt', 'Addison-Wesley', '1999-10-30'),
('Design Patterns', 'Erich Gamma', 'Addison-Wesley', '1994-10-31');
