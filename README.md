Dans le projet cryptator (la branche : https://github.com/BelotFlorent/cryptator du projet original : https://github.com/arnaud-m/cryptator): 
mvn clean install  


Dans l'api cryptator-api :  
mvn clean package  
java -jar target\cryptator-api-1.0.1-SNAPSHOT.jar  

Swagger : http://localhost:8090/swagger-ui/index.html  


# Cryptator-API

API REST développée en **Spring Boot** pour exposer les fonctionnalités du projet [cryptator](https://github.com/BelotFlorent/cryptator) (fork du projet original : https://github.com/arnaud-m/cryptator).

---

## Description

**cryptator-api** est une API REST permettant d’interagir avec le projet **cryptator** via des endpoints HTTP.

L’API est documentée avec **Swagger (OpenAPI)** afin de faciliter les tests et l’intégration.

---

## Prérequis

Avant de lancer le projet, assurez-vous d’avoir installé :

-  Java 17+
-  Maven 3.6+
-  Git

---

##  Installation & Exécution

### 1️⃣ Compiler le projet Cryptator

Le projet `cryptator` doit être construit avant de compiler l’API :

```bash
git clone https://github.com/BelotFlorent/cryptator
cd cryptator
mvn clean install
```

### 2️⃣ Compiler l’API

```bash
git clone https://github.com/BelotFlorent/cryptator-api
cd cryptator-api
mvn clean package
```

### 3️⃣ Lancer l’application

```bash
java -jar target/cryptator-api-1.0.1-SNAPSHOT.jar
```
L’application démarre par défaut sur le port 8090.

## Documentation Swagger

Une fois l’application démarrée, la documentation interactive est accessible à l’adresse suivante :

## Licence

Ce projet est distribué sous licence MIT.

