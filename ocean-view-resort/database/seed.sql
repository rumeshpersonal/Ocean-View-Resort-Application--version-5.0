USE ocean_view_resort;

INSERT INTO room_types (type_name, rate_per_night) VALUES
('STANDARD', 12000.00),
('DELUXE', 18000.00),
('SUITE', 25000.00)
ON DUPLICATE KEY UPDATE rate_per_night = VALUES(rate_per_night);

INSERT INTO guests (full_name, address, contact_number) VALUES
('Kasun Perera', 'No. 12, Wakwella Road, Galle', '0771234567'),
('Dinushi Fernando', 'No. 44, Marine Drive, Negombo', '0719876543'),
('Nimal Silva', 'No. 8, Temple Road, Matara', '0753332211'),
('Sahan Jayasinghe', 'No. 21, Lighthouse Street, Galle', '0765552211'),
('Anjali Kumar', 'No. 15, Beach Road, Colombo', '0772223333');

INSERT INTO reservations (reservation_no, guest_id, room_type_id, check_in, check_out) VALUES
('RES-2026-0001', 1, 1, '2026-03-02', '2026-03-04'),
('RES-2026-0002', 2, 2, '2026-03-05', '2026-03-07');
