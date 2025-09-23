# Types of Databases

Understand various types of databases and their use cases in system design.

As we discussed earlier, databases are divided into two types: **relational** and **non-relational**. Let’s discuss these types in detail.

---

## Relational Databases

Relational databases adhere to particular **schemas** before storing the data. The data stored in relational databases has prior structure.  
Mostly, this model organizes data into one or more **relations** (also called tables), with a **unique key** for each tuple (instance).  

- Each entity of the data consists of **instances** and **attributes**.  
- Instances are stored in **rows**, and the attributes of each instance are stored in **columns**.  
- Since each tuple has a unique key, a tuple in one table can be linked to a tuple in other tables by storing the **primary keys** in other tables (generally known as **foreign keys**).  

A **Structured Query Language (SQL)** is used for manipulating the database. This includes insertion, deletion, and retrieval of data.

---

### Why Relational Databases Are Popular
Relational databases are widely used because of their:  
- Simplicity  
- Robustness  
- Flexibility  
- Performance  
- Scalability  
- Compatibility in managing generic data  

---

## ACID Properties

Relational databases provide the **Atomicity, Consistency, Isolation, and Durability (ACID)** properties to maintain the integrity of the database.  

ACID is a powerful abstraction that:  
- Simplifies complex interactions with the data  
- Hides many anomalies (dirty reads, dirty writes, read skew, lost updates, write skew, phantom reads) behind a simple **transaction abort**

⚠️ However, ACID is like a **big hammer** by design, making it generic enough for all problems.  
If an application only needs to deal with a few anomalies, there is an opportunity to use a **custom solution** for higher performance, though with added complexity.

---

### Let’s Discuss ACID in Detail:

- **Atomicity**:  
  A transaction is considered an atomic unit.  
  Either **all the statements** within a transaction will successfully execute, or **none** of them will execute.  
  If a statement fails within a transaction, it should be aborted and rolled back.  

- **Consistency**:  
  At any given time, the database should be in a **consistent state**, and it should remain consistent after every transaction.  
  Example: If multiple users want to view a record from the database, it should return the same result each time.  

- **Isolation**:  
  If multiple transactions run concurrently, they shouldn’t interfere with each other.  
  The final state of the database should be the same as if the transactions were executed **sequentially**.  

- **Durability**:  
  The system should guarantee that **completed transactions will survive permanently**, even in case of system failures.  

---

## Popular Relational DBMS

Various **Database Management Systems (DBMS)** are used to define relational database schemas and perform operations like storing, retrieving, and running SQL queries on data.  

Some popular DBMS include:  
- MySQL  
- Oracle Database  
- Microsoft SQL Server  
- IBM DB2  
- Postgres  
- SQLite
