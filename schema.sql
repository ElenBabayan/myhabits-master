drop table if exists follower_following;
drop table if exists reminder;
drop table if exists progress;
drop table if exists habit;
drop table if exists "users";
drop table if exists "user";

create table if not exists "users"
(
    id            bigserial primary key,
    name          varchar(255) not null,
    email         varchar(255) not null unique,
    password      varchar(255) not null,
    date_of_birth timestamp    not null,
    role          varchar(20)  not null
);

create table if not exists habit
(
    id          bigserial primary key,
    name        varchar(255) not null,
    description varchar(255) not null,
    wiki_link   varchar(255),
    progress    integer default 0,
    user_id     bigint       not null,
    constraint fk_user foreign key (user_id) references "users" (id) on delete cascade
);
CREATE INDEX idx_habit_user_id ON habit (user_id);

create table if not exists progress
(
    id             bigserial primary key,
    user_id        bigint not null,
    habit_id       bigint not null,
    total_count    int    not null default 0,
    current_streak int    not null default 0,
    longest_streak int    not null default 0,
    constraint fk_user foreign key (user_id) references "users" (id) on delete cascade,
    constraint fk_habit foreign key (habit_id) references habit (id) on delete cascade,
    unique (user_id, habit_id)
);

create table if not exists reminder
(
    id          bigserial primary key,
    user_id     bigint       not null,
    habit_id    bigint       not null,
    title       varchar(255) not null,
    description varchar(255),
    due_date    timestamp,
    completed   boolean default false,
    constraint fk_user foreign key (user_id) references "users" (id) on delete cascade,
    constraint fk_habit foreign key (habit_id) references habit (id) on delete cascade
);
CREATE INDEX idx_reminder_userid_duedate ON reminder (user_id, due_date);

create table if not exists follower_following
(
    id           bigserial primary key,
    follower_id  bigint not null,
    following_id bigint not null,
    foreign key (follower_id) references "users" (id) on delete cascade,
    foreign key (following_id) references "users" (id) on delete cascade,
    unique (follower_id, following_id)
);

/*
    total_count stores the total number of times the user has tracked the habit.
    current_streak stores the current streak of the habit for the user (i.e., the number of consecutive days that the habit has been tracked).
    longest_streak stores the longest streak of the habit for the user.
    last_tracked stores the date and time when the habit was last tracked by the user.
    The combination of user_id and habit_id is marked as unique to ensure that each user can only have one set of statistics for each habit.

    due_date represents the date and time by which a reminder needs to be completed.
    completed indicates whether or not a reminder has been completed.
 */
