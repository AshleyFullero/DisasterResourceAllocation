CREATE DATABASE IF NOT EXISTS equirelief_db;
USE equirelief_db;

-- 1. Barangays Table
CREATE TABLE barangays (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    location_details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CHAIRMAN', 'VOLUNTEER') NOT NULL,
    barangay_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (barangay_id) REFERENCES barangays(id) ON DELETE SET NULL
);

-- 3. Families Table
CREATE TABLE families (
    id INT AUTO_INCREMENT PRIMARY KEY,
    family_head_name VARCHAR(255) NOT NULL,
    family_size INT NOT NULL,
    address VARCHAR(255),
    notes TEXT,
    barangay_id INT NOT NULL,
    family_image MEDIUMBLOB NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (barangay_id) REFERENCES barangays(id) ON DELETE CASCADE
);

-- 4. Resources Table (Inventory)
CREATE TABLE resources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_type VARCHAR(50) NOT NULL,
    weight_kg DECIMAL(10, 2) NOT NULL,
    importance_score INT NOT NULL,
    total_quantity INT NOT NULL,
    resource_image MEDIUMBLOB NULL,
    value_per_weight DECIMAL(10, 2) AS (importance_score / weight_kg) STORED
);

-- 5. Distribution Events Table
CREATE TABLE distribution_events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    status ENUM('PLANNING', 'IN_PROGRESS', 'COMPLETED') NOT NULL,
    notes TEXT,
    barangay_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (barangay_id) REFERENCES barangays(id) ON DELETE CASCADE
);

-- 6. Allocations Table
CREATE TABLE allocations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    distribution_event_id INT NOT NULL,
    family_id INT NOT NULL,
    resource_id INT NOT NULL,
    quantity_allocated INT NOT NULL,
    FOREIGN KEY (distribution_event_id) REFERENCES distribution_events(id) ON DELETE CASCADE,
    FOREIGN KEY (family_id) REFERENCES families(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(id) ON DELETE CASCADE
);

insert into barangays ( name,location_details) values ('barangay001','Manila , Philippines');
insert into barangays ( name,location_details) values ('barangay002','Manila , Philippines');

INSERT INTO users (username, password,  role) 
VALUES ('admin', 'admin', 'ADMIN');
INSERT INTO users (username, password, role, barangay_id) 
VALUES ('barangay001', 'barangay001', 'CHAIRMAN',1);
INSERT INTO users (username, password, role, barangay_id) 
VALUES ('barangay002', 'barangay002', 'CHAIRMAN',2);
INSERT INTO users (username, password, role, barangay_id) 
VALUES ('volunteer', 'volunteer', 'VOLUNTEER',1);

INSERT INTO resources (name, unit_type, weight_kg, importance_score, total_quantity) VALUES
('Rice', 'bags', 3.00, 4, 20),
('Canned Goods', 'packs', 5.00, 5, 25),
('Bottled Water', 'bots', 2.00, 3, 30),
('Baby Formula', 'cans', 1.50, 5, 15),
('Medicine Kits', 'kits', 2.50, 5, 10),
('Hygiene Kits', 'kits', 3.00, 4, 18),
('Blankets', 'pcs', 4.00, 3, 12),
('Cooking Oil', 'bots', 1.20, 2, 10),
('Noodles', 'packs', 0.50, 2, 50),
('Face Masks', 'pcs', 0.10, 1, 100);

INSERT INTO families (family_head_name, family_size, address, notes, barangay_id) VALUES
('Bautista', 7, 'Zone 1', 'Largest family ', 1),
('Lopez', 6, 'Zone 2', '', 1),
('Reyes', 5, 'Zone 3', '', 1),
('Dela Cruz', 5, 'Zone 4', '', 1),
('Cruz', 4, 'Zone 5', 'Infant', 1),
('Santos', 3, 'Zone 6', '', 1);

select * from users;
