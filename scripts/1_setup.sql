create
    database musiconnect;
\c musiconnect;

create table country
(
    id   int         not null
        constraint country_pkey
            primary key,
    name varchar(50) not null
);

alter table country
    owner to postgres;

create table app_user
(
    id            serial              not null
        constraint app_user_pkey
            primary key,
    name          varchar(70)         not null,
    email         varchar(255) unique not null,
    password_hash varchar             not null,
    country_id    integer             not null
        constraint app_user_country_id_fkey
            references country,
    picture       varchar,
    is_admin      boolean default false
);

alter table app_user
    owner to postgres;

create table follower_following
(
    follower_id  integer not null
        constraint follower_following_follower_id_fkey
            references app_user,
    following_id integer not null
        constraint follower_following_following_id_fkey
            references app_user,
    constraint follower_following_pkey
        primary key (follower_id, following_id)
);

alter table follower_following
    owner to postgres;

create table subscription
(
    user_id      integer not null
        constraint subscription_pkey
            primary key
        constraint subscription_user_id_fkey
            references app_user,
    last_payment date    not null
);

alter table subscription
    owner to postgres;

create table artist
(
    id         serial  not null
        constraint artist_pkey
            primary key,
    name       varchar not null,
    country_id integer not null
        constraint artist_country_id_fkey
            references country,
    picture    varchar
);

alter table artist
    owner to postgres;

create table category
(
    id   serial      not null
        constraint category_pkey
            primary key,
    name varchar(40) not null
);

alter table category
    owner to postgres;

create table album
(
    id           serial  not null
        constraint album_pkey
            primary key,
    name         varchar not null,
    artist_id    integer not null
        constraint album_artist_id_fkey
            references artist,
    release_date date,
    category_id  integer not null
        constraint album_category_id_fkey
            references category,
    picture      varchar
);

alter table album
    owner to postgres;

create table song
(
    id           serial  not null
        constraint song_pkey
            primary key,
    name         varchar not null,
    album_id     integer not null
        constraint song_album_id_fkey
            references album,
    length       integer not null,
    release_date date,
    audio        varchar
);

alter table song
    owner to postgres;

create table song_artist
(
    song_id   integer not null
        constraint song_artist_song_id_fkey
            references song,
    artist_id integer not null
        constraint song_artist_artist_id_fkey
            references artist,
    constraint song_artist_pkey
        primary key (song_id, artist_id)
);

alter table song_artist
    owner to postgres;


create table streams
(
    song_id    integer not null
        constraint streams_song_id_fkey
            references song,
    country_id integer not null
        constraint streams_country_id_fkey
            references country,
    streams    bigint  not null,
    constraint streams_pkey
        primary key (song_id, country_id)
);

alter table streams
    owner to postgres;

create table playlist
(
    id          serial  not null
        constraint playlist_pkey
            primary key,
    user_id     integer not null
        constraint playlist_user_id_fkey
            references app_user,
    category_id integer not null
        constraint playlist_category_id_fkey
            references category
);

alter table playlist
    owner to postgres;

create table playlist_song
(
    playlist_id integer not null
        constraint playlist_song_playlist_id_fkey
            references playlist,
    song_id     integer not null
        constraint playlist_song_song_id_fkey
            references song,
    constraint playlist_song_pkey
        primary key (playlist_id, song_id)
);

alter table playlist_song
    owner to postgres;

