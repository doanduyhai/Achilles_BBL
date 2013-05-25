# Achilles BBL


Twitter Like Demo with Achilles for Brown Bag Lunch

## Running the back-end

### Prerequisites

To be able to run the back-end, you need to download and install the Cassandra server version 1.2. It can be downloaded **[here]**


### Keyspace creation

* Connect to the server using the **cassandra-cli** client
* Create the **achilles** keyspace with the following command: 
 
 `CREATE KEYSPACE achilles`
 
 `WITH placement_strategy = 'SimpleStrategy'` 

 `AND strategy_options = {replication_factor:1};`


### Running the demo

* Checkout the source code of the demo with `git clone https://github.com/doanduyhai/Achilles_BBL.git`
* Go to the demo folder and run it with `mvn jetty:run`


## Hands-on

### Step1: managing users

 First checkout the initial code with `git checkout Step1`

 The objectives of this steps is to be able to implement the "Follow" feature of Tweeter.
 
 Below are the specs:
 
 1. Create **User** entity with the following fields:
     * userId (primary key)
     * firstname
     * lastname
     * list of friends (TODO)
     * list of followers (TODO)
     * friends counter (TODO)
     * followers counter (TODO)
 
 2. Implement the **UserService** to 
     * Create an user 
     * Add a friend to an user
     * Remove a friend from an user
     * Get user information
     * List all user friends
     * List all user followers

### Step2: creating tweets

 Checkout the code with `git checkout Step2`

 In this step, we want to implement the main "Send tweet" feature. When an user posts a tweet, this tweet should be visible:
 
 * In his own list of tweet ( **userline** )
 * In his timeline 
 * In the timeline of all his followers

 1. Modify **User** entity to add:
     * a list of all his tweets  ( **userline** )
     * a list of tweets for his timeline
     * a counter for his own tweets ( **tweetsCount** )
 2. Implement the **TweetService** to 
     * allow an user to post a tweet and spread the tweet to all his followers

 

[here]: http://cassandra.apache.org/download/
