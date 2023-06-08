INSERT INTO friend_status(friend_status_id) VALUES (0);
INSERT INTO friend_status(friend_status_id) VALUES (1);
INSERT INTO friend_status(friend_status_id) VALUES (2);

UPDATE friend_status SET status = 'FOLLOWS' WHERE friend_status_id = 0;
UPDATE friend_status SET status = 'FOLLOWED_BY' WHERE friend_status_id = 1;
UPDATE friend_status SET status = 'FRIENDS' WHERE friend_status_id = 2;


INSERT INTO rating(rating_id) VALUES (1);
INSERT INTO rating(rating_id) VALUES (2);
INSERT INTO rating(rating_id) VALUES (3);
INSERT INTO rating(rating_id) VALUES (4);
INSERT INTO rating(rating_id) VALUES (5);

UPDATE rating SET rating_name = 'G' WHERE rating_id = 1;
UPDATE rating SET rating_name = 'PG' WHERE rating_id = 2;
UPDATE rating SET rating_name = 'PG13' WHERE rating_id = 3;
UPDATE rating SET rating_name = 'R' WHERE rating_id = 4;
UPDATE rating SET rating_name = 'NC17' WHERE rating_id = 5;


INSERT INTO genre(genre_id) VALUES (0);
INSERT INTO genre(genre_id) VALUES (1);
INSERT INTO genre(genre_id) VALUES (2);

UPDATE genre SET genre_name  = 'COMEDY' WHERE genre_id = 0;
UPDATE genre SET genre_name  = 'DRAMA' WHERE genre_id = 1;
UPDATE genre SET genre_name  = 'ACTION' WHERE genre_id = 2;
