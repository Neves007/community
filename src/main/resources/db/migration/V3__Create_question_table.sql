CREATE TABLE question
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title varchar(50),
    description TEXT,
    gmt_create bigint,
    gmt_modified bigint,
    creator BIGINTz,
    comment_count int DEFAULT 0,
    view_count int DEFAULT 0,
    like_count int DEFAULT 0,
    tag varchar(256)
);
