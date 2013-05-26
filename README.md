# Achilles BBL


Démo Twitter-like avec Achilles pour le Brown Bag Lunch

## Lancer l'application en back-end

### Pré-requis

Pour lancer l'application en back-end, vous devez télécharger et installler Cassandra server version 1.2 **[ici]**

### Création du keyspace

* Se connecter à Cassandra avec le client **cassandra-cli**
* Créer le keyspace **achilles** avec la commande suivante : 
 
 `CREATE KEYSPACE achilles`
 
 `WITH placement_strategy = 'SimpleStrategy'` 

 `AND strategy_options = {replication_factor:1};`


### Lancer la démo

* Récupérez le code source avec `git clone https://github.com/doanduyhai/Achilles_BBL.git`
* Allez dans le répertoire `Achilles_BBL` puis exécutez: `mvn jetty:run`

### Lancer le client de test

 Pour pouvoir interroger l'application back-end, il faut un client REST. Vous pouvez installer l'extension **[Postman REST Client]** pour le navigateur Chrome.
 
 Ensuite, il faut charger les scénarios pré-enregistrés. Ces scénarios se trouvent dans le répertoire `Postman_Client_Script` du projet.
 
 * Achilles_BBL_Scenario1.json
 * Achilles_BBL_Scenario2.json
 * Achilles_BBL_Scenario3.json

Il faut importer les 3 scripts ci-dessus pour tester les 3 scénarios ( _Collections/Import collections_ ).
 
De plus, vous aurez besoin d'importer les paramètres d'environnement ( _Manage environments/Import_ ) depuis le fichier `AchillesDemo-environment.json`

## Hands-on

### Scénario1: gérer les utilisateurs et les suiveurs

 Tout d'abord, récupérer le code source pour ce scénario avec `git checkout Scenario1`

 L'objectif de ce scénario est d'implémenter la fonctionnalité "Suivre" de Twitter
 
 Pour cela:
 
 1. Créer une entité **User** avec les champs suivants:
     * userId (primary key)
     * firstname
     * lastname
     * list of friends (TODO)
     * list of followers (TODO)
     * friends counter (TODO)
     * followers counter (TODO)
 
 2. Implémenter le **UserService** pour pouvoir:
     * Créer un utilisateur
     * Ajouter un ami à un utilisateur
     * Retirer un ami à un utilisateur
     * Donner les détails sur un utilisateur
     * Lister tous les amis d'un utlisateur
     * Lister tous les suiveurs d'un utlisateur

### Scénario2: créer des tweets

 Récupérer le code avec `git checkout Scenario2`

 Dans ce scénario, on cherche à implémenter la fonctionnalité "Envoyer un tweet".
 Quand un utlisateur envoie un tweet, celui-ci doit être visible dans:
 
 * sa propre liste de tweets ( **userline** )
 * sa timeline
 * la timeline de tous ses suiveurs

 Pour ce faire, il faut:
 
 1. Modifier l'entité **User** pour ajouter:
     * une liste de tous les tweets  ( **userline** )
     * une liste de tous les tweets dans sa timeline
     * un compteur pour ses propres tweets ( **tweetsCount** )
 
 2. Implémenter le **TweetService** pour
     * envoyer un tweet et le disperser dans la timeline de tous les suiveurs

### Scénario3: supprimer un tweet

 Récupérer le code avec `git checkout Scenario3`
 
 Dans ce scénario, il faut gérer la suppression d'un tweet. Il faut pouvoir supprimer le tweet dans:
 * l'userline du créateur
 * la timeline du créateur
 * la timeline de chaque suiveur du créateur

Pour cela, il faut modifier l'entité **TweetIndex** pour retenir les utilisateurs chez qui le tweet a été dispatché


[ici]: http://cassandra.apache.org/download/
[Postman REST Client]: https://chrome.google.com/webstore/detail/postman-rest-client/fdmmgilgnpjigdojojpjoooidkmcomcm
