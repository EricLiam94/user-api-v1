CREATE TABLE users {
    id BIGINT AUTO_INCREMENT PRIMARY key,
    username VARCHAR(250) NOT NULL,
    password VARCHAR(250) NOT NULL,
    type VARCHAR(100) NOT NULL,
    first_name VARCHAR(250) ,
    last_name VARCHAR(250) ,
    mobile VARCHAR(250) ,
    email VARCHAR(250)
};