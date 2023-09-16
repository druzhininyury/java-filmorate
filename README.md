# java-filmorate
Filmorate project.

## Database
![database diagram](/database_er.png)

###Examples of usage:

```sql
-- Get all films
SELECT name
FROM films;
```

```sql
-- Get all users
SELECT name,
       email
FROM users;
```

```sql
-- Get top 10 films.
SELECT f.name
FROM films AS f
INNER JOIN films_likes AS fl ON f.id = fl.film_id
GROUP BY f.id
SORT BY COUNT(fl.user_id) DESC
LIMIT 10;
```