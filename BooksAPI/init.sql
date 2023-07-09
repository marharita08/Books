create table users (
  username  varchar(200) primary key,
  full_name varchar(200),
  password  varchar(1000) not null,
  avatar    varchar(100)
);


create table genre (
  genre_id serial primary key,
  genre    varchar(200) not null
);

create table author (
  author_id serial primary key,
  full_name varchar(200) not null,
  photo     varchar(200)
);

create table book (
  book_id                serial primary key,
  title                  varchar(300) not null,
  description            text,
  cover_photo            varchar(300),
  first_publication_year integer,
  page_amount            integer
);

create table book_genre (
  book_id integer REFERENCES book (book_id) on delete cascade,
  genre_id integer REFERENCES genre (genre_id) on delete cascade,
  primary key(book_id, genre_id)
);

create table book_author (
  book_id integer  REFERENCES book (book_id) on delete cascade,
  author_id integer REFERENCES author (author_id) on delete cascade,
  primary key(book_id, author_id)
);

create table wishlist (
  book_id integer REFERENCES book (book_id),
  username varchar(200) REFERENCES users (username),
  adding_date timestamp,
  primary key(book_id, username)
);

create table reading (
  book_id integer REFERENCES book (book_id),
  username varchar(200) REFERENCES users (username),
  adding_date timestamp,
  primary key(book_id, username)
);

create table already_read (
  book_id integer REFERENCES book (book_id),
  username varchar(200) REFERENCES users (username),
  rating   integer,
  review   text,
  primary key(book_id, username)
);


insert into genre(genre) values
('Fantasy'),
('Adventure'),
('Romance'),
('Contemporary'),
('Mystery'),
('Horror'),
('Thriller'),
('Paranormal'),
('Historical fiction'),
('Science Fiction'),
('Children’s'),
('Art'),
('Motivational'),
('History'),
('Humor'),
('Biography'),
('Children’s');
